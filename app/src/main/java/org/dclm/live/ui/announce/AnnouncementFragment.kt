package org.dclm.live.ui.announce

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
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.dclm.live.R
import org.dclm.live.databinding.AnnouncementFragmentBinding


class AnnouncementFragment : Fragment() {
    private var documentReference: DocumentReference? = null
    private val db = FirebaseFirestore.getInstance()
    private var registration: ListenerRegistration? = null
    private var sharedpreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    val PREFRENCES = "org.dclm.radio"
    private lateinit var binding: AnnouncementFragmentBinding
    private var errorState = false

    companion object {
        fun newInstance() = AnnouncementFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.announcement_fragment, container, false)
        sharedpreferences = activity?.applicationContext?.getSharedPreferences(PREFRENCES, Context.MODE_PRIVATE)
        editor = sharedpreferences?.edit()
        documentReference = activity?.resources?.getString(R.string.announcement)?.let { db.document(it) }
        sharedpreferences?.getString("comment", " ")?.let { setupWebView(it) }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        //adapter.startListening();
        registration = documentReference!!.addSnapshotListener { documentSnapshot, e ->
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        val comment = activity?.resources?.getString(R.string.comment)?.let {
                                documentSnapshot.getString(it).toString()
                            }
                        if (comment!!.isNotEmpty()) {
                            // constraintLayout.setVisibility(View.VISIBLE);
                            editor!!.putString("comment", comment)
                            editor!!.apply()
                            setupWebView(comment)
                        }
                    } else e?.printStackTrace()
                }
            }
    }

    override fun onStop() {
        super.onStop()
        if (registration != null) {
            registration!!.remove()
            registration = null
        }
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


                binding.text.visibility =View.GONE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                if (errorState){
                    binding.text.visibility = View.VISIBLE
                } else{
                    binding.text.visibility = View.GONE
                }
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)

                binding.webview.visibility = View.INVISIBLE
                binding.text.visibility =View.VISIBLE
                errorState = true
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(url)
                    binding.imageView2.visibility =View.GONE
                    return false
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.url.toString())
                    binding.imageView2.visibility =View.GONE
                    return false
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }


        binding.webview.loadDataWithBaseURL(null, aboutUrl, "text/html", "UTF-8", null)
    }

}