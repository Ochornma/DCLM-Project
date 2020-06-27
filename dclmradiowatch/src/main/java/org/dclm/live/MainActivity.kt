package org.dclm.live

import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.wear.ambient.AmbientModeSupport
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.exoplayer2.util.Util
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import org.dclm.live.databinding.ActivityMainBinding
import org.json.JSONException
import java.util.*


class MainActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {
    private var bound = false
    private var dclmRadioService: DCLMRadioService? = null
    //private lateinit var registry: LifecycleRegistry
    private var stateOfPlay = false
    private lateinit var binding: ActivityMainBinding
    var timer: Timer? = null
    private lateinit var mQueue:RequestQueue
    private lateinit var preferences: SharedPreferences
    val PREFRENCES = "org.dclm.radio"
    private lateinit var editor: SharedPreferences.Editor
    val LINK = "LINK"

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as DCLMRadioService.RadioLocalBinder
            dclmRadioService = binder.getService()
            getState()
            bound = true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mQueue = Volley.newRequestQueue(this)
        preferences = applicationContext.getSharedPreferences(PREFRENCES, Context.MODE_PRIVATE)
        editor = preferences.edit()
        editor.putString(LINK, getString(R.string.radio_link))
        editor.apply()
      //  registry = LifecycleRegistry(this)
      //  registry.currentState = Lifecycle.State.CREATED
        // Enables Always-on
       // setAmbientEnabled()

        binding.play.setOnClickListener {
            if (speakerIsSupported()){
                if (stateOfPlay){
                    dclmRadioService?.pausePlayer()
                    binding.play.setImageResource(R.drawable.ic_play)
                } else{
                    val intent = Intent(this, DCLMRadioService::class.java)
                    Util.startForegroundService(this, intent)
                    binding.play.setImageResource(R.drawable.ic_pause)
                    stateOfPlay
                }
                stateOfPlay = !stateOfPlay
            } else{
                Toast.makeText(this, getString(R.string.no_speaker), Toast.LENGTH_LONG).show()
            }
        }
        subscribe()
    }

    fun subscribe(){
        if (!preferences.getBoolean("subscribe", false)){
            FirebaseMessaging.getInstance().subscribeToTopic("Notification")
                .addOnCompleteListener {
                    if (it.isSuccessful){
                    editor.putBoolean("subscribe", true)
                        editor.apply()
                    }else{
                        editor.putBoolean("subscribe", false)
                        editor.apply()
                    }
                }
        }


    }

    private fun getState(){
        dclmRadioService?.playState?.observe(this, Observer {
            stateOfPlay = if (it){
                binding.play.setImageResource(R.drawable.ic_pause)
                true
            } else{
                binding.play.setImageResource(R.drawable.ic_play)
                false
                // mViewModel.checkButton?.postValue(true)
            }
        })
    }


    override fun onStart() {
        super.onStart()
       // registry.currentState = Lifecycle.State.STARTED
        Picasso.get().load("put your url").placeholder(R.drawable.nlogo).error(R.drawable.nlogo).fit().into(binding.background)
        getState()
        val intent = Intent(this, DCLMRadioService::class.java)
       bindService(intent, connection, Context.BIND_AUTO_CREATE)
        setRepeatingAsyncTask()
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
        bound = false
        unbindService(connection)
    }

    override fun onResume() {
        super.onResume()
      //  registry.currentState = Lifecycle.State.RESUMED
        if (dclmRadioService != null){
            if (dclmRadioService?.buttonState!!) {
                val handler1 = Handler()
                handler1.post { binding.play.setImageResource(R.drawable.ic_pause) }
            } else {
                val handler2 = Handler()
                handler2.post { binding.play.setImageResource(R.drawable.ic_play) }
            }
        }
    }

    private fun setRepeatingAsyncTask(){
        val handler = Handler()
        timer = Timer()

        val task: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post {jsonParse() }
            }
        }

        timer?.schedule(task, 0, 60 * 1000.toLong()) // interval of one minute

    }

    private fun jsonParse() {
        val request =
            JsonObjectRequest(
                Request.Method.GET, getString(R.string.dclm_api), null,
                Response.Listener { response ->
                    try {
                        val nowPlaying = response.getJSONObject("now_playing")
                        val song = nowPlaying.getJSONObject("song")
                        val topic = song.getString("title")
                        val listeners = response.getJSONObject("listeners")
                        val number = listeners.getString("total")
                        val listen = getString(R.string.listning) + " :" + number
                       val subTitle = SubTitle(topic, listen)
                        binding.data = subTitle
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                    error.printStackTrace()
                    val subTitle = SubTitle(" "," ")
                    binding.data = subTitle
                })
        mQueue.add(request)
    }


    override fun onDestroy() {
        super.onDestroy()
       // registry.currentState = Lifecycle.State.DESTROYED
    }

 /*   override fun getLifecycle(): Lifecycle {
        return registry
    }
*/

    fun speakerIsSupported(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageManager = packageManager
            // The results from AudioManager.getDevices can't be trusted unless the device
            // advertises FEATURE_AUDIO_OUTPUT.
            if (!packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
                return false
            }
            val audioManager =
                getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val devices =
                audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            for (device in devices) {
                if (device.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                    return true
                }
            }
        }
        return false
    }


    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
       return MyAmbient()
    }

    private inner class MyAmbient: AmbientModeSupport.AmbientCallback() {
        override fun onExitAmbient() {
            super.onExitAmbient()
            binding.topic.setTextColor(Color.parseColor("#BCFFFFFF"))
            binding.listener.setTextColor(Color.parseColor("#BCFFFFFF"))
            binding.topic.paint.isAntiAlias = false
            binding.listener.paint.isAntiAlias = false
        }



        override fun onEnterAmbient(ambientDetails: Bundle?) {
            super.onEnterAmbient(ambientDetails)
            binding.topic.setTextColor(Color.parseColor("#BCFFFFFF"))
            binding.listener.setTextColor(Color.parseColor("#BCFFFFFF"))
            binding.topic.paint.isAntiAlias = false
            binding.listener.paint.isAntiAlias = false

        }
    }


}
