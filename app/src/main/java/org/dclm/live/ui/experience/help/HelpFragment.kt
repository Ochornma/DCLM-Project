package org.dclm.live.ui.experience.help

import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.dclm.live.R
import org.dclm.live.databinding.HelpFragmentBinding


class HelpFragment : Fragment() {
    private lateinit var binding:HelpFragmentBinding
    private var errorState = false

    companion object {
        fun newInstance() = HelpFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.help_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       setupWebView("put your url")

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
                binding.progressBar.visibility = View.VISIBLE
                binding.text.visibility =View.GONE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Log.i("check", "2")
                binding.progressBar.visibility = View.GONE
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
                binding.progressBar.visibility = View.GONE
                binding.text.visibility =View.VISIBLE
                errorState = true
            }
        }
       binding.webview.loadUrl(aboutUrl)
    }

}