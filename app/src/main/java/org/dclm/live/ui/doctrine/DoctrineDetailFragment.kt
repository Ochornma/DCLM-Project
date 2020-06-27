package org.dclm.live.ui.doctrine

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.dclm.live.R
import org.dclm.live.databinding.DoctrineDetailFragmentBinding

class DoctrineDetailFragment : Fragment() {
    private lateinit var binding: DoctrineDetailFragmentBinding

    companion object {
        fun newInstance() = DoctrineDetailFragment()
    }

    private lateinit var viewModel: DoctrineDetailViewModel

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.doctrine_detail_fragment, container, false)
        val doctrine = arguments?.let { DoctrineDetailFragmentArgs.fromBundle(it).doctrine }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.body.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
        binding.doctrine = doctrine
      /*  binding.heading.text = doctrine?.heading
        binding.content.text = doctrine?.content
        binding.body.text = doctrine?.body*/

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}