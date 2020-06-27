package org.dclm.live.ui.experience.testimony

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.testimony_fragment.*
import org.dclm.live.R
import org.dclm.live.databinding.TestimonyFragmentBinding
import org.dclm.live.ui.util.TestimonyRecieved

class TestimonyFragment : Fragment(), TestimonyRecieved{
    private lateinit var adapter: TestimonyAdapter
    private lateinit var binding: TestimonyFragmentBinding

    companion object {
        fun newInstance() = TestimonyFragment()
    }

    private lateinit var viewModel: TestimonyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.testimony_fragment, container, false)
        binding.recyclerView.setHasFixedSize(true)
        adapter = TestimonyAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = context?.let { TestimonyFactory(this, it) }
        viewModel = factory?.let { ViewModelProvider(this, it).get(TestimonyViewModel::class.java) }!!
        viewModel.getTestimony()
        binding.share.setOnClickListener {
            invisibility()
        }

        binding.submitTestimony.setOnClickListener {
            visibility()
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val subject = binding.subject.text.toString()
            val testimony = binding.yourTestimony.text.toString()
            val region = binding.region.text.toString()
            val state = binding.state.text.toString()
            val district = binding.district.text.toString()
            viewModel.postTestimony(name, email, subject, testimony, state, region, district)
            binding.name.setText("")
            binding.email.setText("")
            binding.subject.setText("")
            binding.yourTestimony.setText("")
            binding.region.setText("")
            binding.state.setText("")
            binding.district.setText("")
        }
    }
    override fun testimontRecieved(testimony: MutableList<Testimony>) {
        adapter.setTestimony(testimony)
        binding.recyclerView.adapter = adapter
    }

    override fun error() {
        Toast.makeText(context, activity?.resources?.getString(R.string.error), Toast.LENGTH_LONG).show()
    }

    private fun invisibility() {
        binding.apply {
            share.visibility = View.GONE
            recyclerView.visibility =View.GONE
            shareTesatimony.visibility = View.VISIBLE
            invalidateAll()
        }
    }

    private fun visibility() {
        binding.apply {
            share.visibility = View.VISIBLE
            recyclerView.visibility =View.VISIBLE
            shareTesatimony.visibility = View.GONE
            invalidateAll()
        }
    }


}