package org.dclm.live.ui.video

import android.content.*
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast

import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.exo_controler_video.view.*
import org.dclm.live.R
import org.dclm.live.databinding.VideoFragmentBinding
import org.dclm.live.ui.radio.DCLMRadioService

class VideoFragment : Fragment() {

    private lateinit var binding: VideoFragmentBinding
    private var dclmRadioService: DCLMRadioService? = null
    var mbound = false
    val PREFRENCES = "org.dclm.radio"
    val destination = "destination"
    val title = "title"
    val describe = "describe"
    val LINK = "LINK"
    val IMAGE = "image"
    val TYPE = "TYPE"
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private lateinit  var bootom:BottomNavigationView
    private lateinit var toolbar: Toolbar


    val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mbound = false
            binding.player.player = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
           val binder = service as DCLMRadioService.RadioLocalBinder
            dclmRadioService = binder.getService()
            binding.player.player = binder.getPlayer()
            mbound = true
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(context, activity?.resources?.getString(R.string.select_language_watch), Toast.LENGTH_SHORT).show()

        binding = DataBindingUtil.inflate(inflater, R.layout.video_fragment, container, false)
        sharedPreferences = activity?.getSharedPreferences(PREFRENCES, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
        editor?.putBoolean(TYPE, true)
        editor?.putInt(destination, R.id.videoFragment)
        editor?.putString(title, activity?.resources?.getString(R.string.app_stream))
        editor?.putString(describe, activity?.resources?.getString(R.string.app_describe))
        editor?.putString(LINK, activity?.resources?.getString(R.string.video_url))
        editor?.putInt(IMAGE, R.drawable.baba_video)
        editor?.apply()
        bootom = activity?.findViewById(R.id.main_bottom_navigation_view)!!
        toolbar = activity?.findViewById(R.id.main_toolbar)!!

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        editor?.putBoolean(TYPE, true)
        editor?.putInt(destination, R.id.videoFragment)
        editor?.putString(title, activity?.resources?.getString(R.string.app_stream))
        editor?.putString(describe, activity?.resources?.getString(R.string.app_describe))
        editor?.putString(LINK, activity?.resources?.getString(R.string.video_url))
        editor?.putInt(IMAGE, R.drawable.baba_video)
        editor?.apply()
        binding.player.findViewById<ImageButton>(R.id.exo_play).setOnClickListener {
            val intent = Intent(activity, DCLMRadioService::class.java)
            context?.let { it1 -> Util.startForegroundService(it1, intent) }
            dclmRadioService?.startPlayer()
        }

        binding.player.findViewById<ImageButton>(R.id.exo_pause).setOnClickListener {
            dclmRadioService?.pausePlayer()
        }

            binding.english.setOnClickListener {
                editor?.putString(LINK, " ")
                editor?.apply()
                val intent = Intent(activity, DCLMRadioService::class.java)
                context?.let { it1 -> Util.startForegroundService(it1, intent) }
                dclmRadioService?.startPlayer()
                english()
        }

        binding.french.setOnClickListener {
            editor?.putString(LINK, " ")
            editor?.apply()
            val intent = Intent(activity, DCLMRadioService::class.java)
            context?.let { it1 -> Util.startForegroundService(it1, intent) }
            dclmRadioService?.startPlayer()
            french()
        }

        binding.player.exo_fullscreen_exit.setOnClickListener {
            fullscreen()
        }
        binding.player.exo_fullscreen_icon.setOnClickListener {
            fullscreenExit()
        }
    }

    private fun english(){
        binding.apply {
            selectEnglish.visibility = View.VISIBLE
            selectFrench.visibility = View.GONE
            player.player = dclmRadioService?.player
            invalidateAll()
        }
    }

    private fun french(){
        binding.apply {
            selectEnglish.visibility = View.GONE
            selectFrench.visibility = View.VISIBLE
            player.player = dclmRadioService?.player
            invalidateAll()
        }
    }

    private fun fullscreen(){
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

        bootom.visibility = View.VISIBLE
        binding.language.visibility = View.VISIBLE
        toolbar.visibility =View.VISIBLE


        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val params = binding.player.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = (400 * activity?.applicationContext?.resources
            ?.displayMetrics?.density!!).toInt()
        binding.player.layoutParams = params
    }



    private fun fullscreenExit(){

        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)


        binding.language.visibility = View.GONE
        bootom.visibility = View.GONE
        toolbar.visibility =View.GONE

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val params =
            binding.player.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.player.layoutParams = params
    }

    override fun onStart() {
        super.onStart()
        editor?.putBoolean(TYPE, true)
        editor?.putInt(destination, R.id.videoFragment)
        editor?.putString(title, activity?.resources?.getString(R.string.app_stream))
        editor?.putString(describe, activity?.resources?.getString(R.string.app_describe))
        editor?.putString(LINK, activity?.resources?.getString(R.string.video_url))
        editor?.putInt(IMAGE, R.drawable.baba_video)
        editor?.apply()
        val intent = Intent(activity, DCLMRadioService::class.java)
        activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        binding.player.player = dclmRadioService?.player
    }

    override fun onResume() {
        super.onResume()
        binding.player.player = dclmRadioService?.player
    }

    override fun onStop() {
        super.onStop()
        activity?.unbindService(connection)
        binding.player.player = null
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}