package org.dclm.live.ui.experience

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.dclm.live.R
import org.dclm.live.databinding.ExperienceFragmentBinding

class ExperienceFragment : Fragment() {
    private lateinit var binding: ExperienceFragmentBinding

    companion object {
        fun newInstance() = ExperienceFragment()
    }

    private lateinit var viewModel: ExperienceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.experience_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExperienceViewModel::class.java)
        binding.viewmodel = viewModel
        binding.view = binding.root
    }

}