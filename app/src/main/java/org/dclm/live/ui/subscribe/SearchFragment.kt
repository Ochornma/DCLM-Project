package org.dclm.live.ui.subscribe

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.messaging.FirebaseMessaging
import org.dclm.live.R
import org.dclm.live.databinding.SearchFragmentBinding


class SearchFragment : Fragment() {
    private lateinit var binding:SearchFragmentBinding
    var html = """<script type="text/javascript">
   dclm http page not for public

"""

    private var errorState = false
    val PREFRENCES = "org.dclm.radio"
    var sharedpreferences: SharedPreferences? = null
    companion object {
        fun newInstance() = SearchFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false)
        sharedpreferences = activity?.getSharedPreferences(PREFRENCES, Context.MODE_PRIVATE)
        binding.switchButton.isChecked = sharedpreferences?.getBoolean("notify", false)!!
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val editor = sharedpreferences!!.edit()
        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                editor.putBoolean("notify", true)
                FirebaseMessaging.getInstance().subscribeToTopic("Notification")
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(context, "Successfully Subscribed", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Failed to Subscribe", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else{
                editor.putBoolean("notify", false)
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Notification")
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(context, "Successful Unsubscribed", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Failed to unsubscribed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            editor.apply()
        }
        setupWebView(html)
    }

    private fun setupWebView(aboutUrl: String) {
        binding.webview.getSettings().setJavaScriptEnabled(true)
        binding.webview.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                // We may want to show the user some dialog here
                val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
                builder?.setPositiveButton(activity?.resources?.getString(R.string.proceed)
                ) { dialog, _ ->
                    handler.proceed()
                    dialog.dismiss()
                }
                builder?.setNegativeButton(activity?.resources?.getString(R.string.cancel)) { dialog, _ ->
                    handler.cancel()
                    dialog.dismiss()
                    // Since we cannot proceed, we should finish the activity
                }
                builder?.setMessage(activity?.resources?.getString(R.string.ssl_error_message))
                builder?.create()?.show()
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.i("check", "1")

                binding.text.visibility =View.GONE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Log.i("check", "2")
                if (errorState){
                    binding.text.visibility = View.VISIBLE
                } else{
                    binding.text.visibility = View.GONE
                }
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                Log.i("check", "3")
                binding.webview.visibility = View.INVISIBLE
                binding.text.visibility =View.VISIBLE
                errorState = true
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(url)
                    return false
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.url.toString())
                    return false
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }


        binding.webview.loadDataWithBaseURL(null, aboutUrl, "text/html", "UTF-8", null)
    }
}