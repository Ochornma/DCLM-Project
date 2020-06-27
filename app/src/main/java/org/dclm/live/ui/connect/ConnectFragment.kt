package org.dclm.live.ui.connect

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.dclm.live.R
import org.dclm.live.databinding.ConnectFragmentBinding

class ConnectFragment : Fragment() {

    companion object {
        fun newInstance() = ConnectFragment()
    }
    private lateinit var binding:ConnectFragmentBinding

    private lateinit var viewModel: ConnectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.connect_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConnectViewModel::class.java)
       // binding.contexta = context
       binding.viewmodel = viewModel
    }

}