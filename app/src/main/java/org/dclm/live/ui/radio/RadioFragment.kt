package org.dclm.live.ui.radio

import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.util.Util
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import org.dclm.live.R
import org.dclm.live.databinding.RadioFragmentBinding
import org.dclm.live.ui.util.SubtitleReceived
import java.util.*
import kotlin.collections.ArrayList

class RadioFragment : Fragment(), SubtitleReceived {

    private var link: String? = null
    private lateinit var mViewModel:RadioViewModel
    private var url: String? = null
    private var dclmRadioService: DCLMRadioService? = null
    private var mBound = false

    private lateinit var binding: RadioFragmentBinding
    var stateStart = false
    private var sharedPreferences: SharedPreferences? = null
    private  var prefs:SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var nowLanguage: String? = null
    var languageList: MutableList<String> = ArrayList()
    private var languageNumber = 0
    private var mbound = false
    val PREFRENCES = "org.dclm.radio"
    val destination = "destination"
    val title = "title"
    val describe = "describe"
    val LINK = "LINK"
    val IMAGE = "image"
    val TYPE = "TYPE"
    var stateOfPlay:Boolean = false
    var LANGUAGE = "language"
    var URL = "url"
    var timer:Timer? = null

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mbound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as DCLMRadioService.RadioLocalBinder
            dclmRadioService = binder.getService()
            getState()
            mbound = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.radio_fragment, container, false)
        sharedPreferences = activity?.getSharedPreferences(PREFRENCES, Context.MODE_PRIVATE)
        prefs = activity?.getSharedPreferences(PREFRENCES, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
        editor?.putBoolean(TYPE, false)
        editor?.putInt(destination, R.id.homeFragment)
        editor?.putString(title, activity?.resources?.getString(R.string.app_name))
        editor?.putString(describe, activity?.resources?.getString(R.string.app_describe))
        editor?.putInt(IMAGE, R.drawable.blog_one)
        editor?.apply()
        languageList = arrayOf("ENGLISH", "FRENCH", "PORTUGUESE/EGUN", "YORUBA", "IGBO", "HAUSA").toMutableList()
        binding.selectLanguage.setOnClickListener {
            binding.languageSelect.visibility =View.VISIBLE
            binding.image.visibility = View.GONE
        }

        binding.languageSelect.visibility = View.GONE

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = activity?.let { RadioFactory(it, this) }
        mViewModel = ViewModelProvider(this, factory!!).get(RadioViewModel::class.java)
        url = prefs!!.getString(URL, activity?.resources?.getString(R.string.dclm_api))
        link = prefs!!.getString(LINK, activity?.resources?.getString(R.string.radio_link)).toString()
       // setRepeatingAsyncTask()
        binding.viewModel = mViewModel
        val intent = Intent(activity, DCLMRadioService::class.java)

        mViewModel.checkButton!!.observe(viewLifecycleOwner, Observer { aBoolean ->
            editor?.putBoolean(TYPE, false)
            editor?.putInt(destination, R.id.homeFragment)
            editor?.putString(title, activity?.resources?.getString(R.string.app_name))
            editor?.putString(describe, activity?.resources?.getString(R.string.app_describe))
            editor?.putInt(IMAGE, R.drawable.blog_one)
            editor?.apply()
                if (aBoolean!!) {

                    context?.let { Util.startForegroundService(it, intent) }
                    dclmRadioService!!.startPlayer()
                    val handler = Handler()
                    handler.post { binding.play.setImageResource(R.drawable.ic_pause) }
                } else {
                   dclmRadioService!!.pausePlayer()
                    mBound = false
                    val handler = Handler()
                    handler.post { binding.play.setImageResource(R.drawable.ic_play) }
                }
                stateStart = !stateStart
            })
        mViewModel.link?.postValue(prefs!!.getString(LANGUAGE, activity?.resources?.getString(R.string.select_language_a)))

        mViewModel.link?.observe(viewLifecycleOwner, Observer {
            when (it) {
                "ENGLISH", "ANGLAISE" -> {
                    url = "put your url"
                    link = "put your url"
                    nowLanguage = "ENGLISH"
                }
                "FRENCH", "FRANÇAISE" -> {
                    url = "put your url"
                    link = "phut your url"
                    nowLanguage = "FRENCH"
                }
                "YORUBA" -> {
                    url = "put your url"
                    link = "put your url"
                    nowLanguage = "YORUBA"
                }
                "IGBO" -> {
                    url = "put your url"
                    link = "put your url"
                    nowLanguage = "IGBO"
                }
                "HAUSA" -> {
                    url = "put your url"
                    link = "put your url"
                    nowLanguage = "HAUSA"
                }
                "PORTUGUESE/EGUN", "PORTUGAIS/EGUN" -> {
                    url = "put your url"
                   link = "put your url"
                    nowLanguage = "PORTUGUESE/EGUN"
                }
            }
            binding.languageSelect.visibility = View.GONE
            binding.image.visibility = View.VISIBLE

            editor!!.putString(URL, url)
            editor!!.putString(LINK, link)
            editor!!.putString(LANGUAGE, nowLanguage)
            editor!!.apply()

            if (stateOfPlay) {
              //  activity!!.startService(intent)
                context?.let { it1 -> Util.startForegroundService(it1, intent) }
                //Util.startForegroundService(getActivity(), intent);
            }


        })

        binding.next.setOnClickListener { v ->
            val handler = Handler()
            handler.post { skipNext()
                 }
        }

        binding.previous.setOnClickListener { v ->
            val handler = Handler()
            handler.post { skipPrevious()
                }
        }
        binding.play.setOnClickListener {
            stateOfPlay = !stateOfPlay
            mViewModel.checkButton?.postValue(stateOfPlay)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            requireView().viewTreeObserver.addOnWindowFocusChangeListener { hasFocus: Boolean ->
                    if (hasFocus) {
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
            }
        }

    }

    override fun onStart() {
        super.onStart()
        setRepeatingAsyncTask()
        //put a url for getting pictures
        Picasso.get().load("put your url").networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.nlogo).error(R.drawable.nlogo).fit().into(binding.imageView)
        getState()
        val intent = Intent(context, DCLMRadioService::class.java)
        activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)

        if (stateOfPlay){
            binding.play.setImageResource(R.drawable.ic_pause)
        }else{
            binding.play.setImageResource(R.drawable.ic_play)
        }
        editor?.putBoolean(TYPE, false)
        editor!!.putString(URL, url)
        editor!!.putString(LINK, link)
        editor?.putInt(destination, R.id.homeFragment)
        editor?.putString(title, activity?.resources?.getString(R.string.app_name))
        editor?.putString(describe, activity?.resources?.getString(R.string.app_describe))
        editor?.putInt(IMAGE, R.drawable.blog_one)
        editor?.apply()
    }

    private fun getState(){
        dclmRadioService?.playState?.observe(viewLifecycleOwner, Observer {
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

    override fun onStop() {
        super.onStop()
       // binding.player.player = null
        timer?.cancel()
        mbound = false
        activity?.unbindService(connection)
        getState()
    }

    override fun onResume() {
        super.onResume()
        if (stateOfPlay){
            binding.play.setImageResource(R.drawable.ic_pause)
        }else{
            binding.play.setImageResource(R.drawable.ic_play)
        }
        getState()
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
                handler.post { mViewModel.jsonParse(url!!) }
            }
        }

        timer?.schedule(task, 0, 60 * 1000.toLong()) // interval of one minute

    }
    private fun skipNext() {
        if (languageNumber < languageList.size && languageNumber != languageList.size - 1) {
            languageNumber += 1
            mViewModel.link!!.postValue(languageList[languageNumber])
        } else if (languageNumber == languageList.size - 1) {
            languageNumber = 0
            mViewModel.link!!.postValue(languageList[languageNumber])
        }
    }

    private fun skipPrevious() {
        if (languageNumber == 0) {
            languageNumber = languageList.size - 1
            mViewModel.link!!.postValue(languageList[languageNumber])
        } else if (languageNumber > 0) {
            languageNumber -= 1
            mViewModel.link!!.postValue(languageList[languageNumber])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mbound){
            activity?.unbindService(connection)
        }
    }

    override fun subtitle(subTitles: SubTitle?) {
        binding.data = subTitles
    }

    override fun error(subTitles: SubTitle?) {
        binding.data = subTitles
    }
}