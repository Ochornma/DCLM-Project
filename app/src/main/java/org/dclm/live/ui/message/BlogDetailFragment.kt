package org.dclm.live.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.dclm.live.R
import org.dclm.live.databinding.BlogDetailFragmentBinding

class BlogDetailFragment : Fragment() {
    private lateinit var binding:BlogDetailFragmentBinding

    companion object {
        fun newInstance() = BlogDetailFragment()
    }

    private lateinit var viewModel: BlogDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.blog_detail_fragment, container, false)
        val blog: Blog? = arguments?.let { BlogDetailFragmentArgs.fromBundle(it).blog }
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadDataWithBaseURL(null, blog?.body, "text/html", "UTF-8", null)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}