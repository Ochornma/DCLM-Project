package org.dclm.live.ui.doctrine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import org.dclm.live.R
import org.dclm.live.databinding.DoctrineFragmentBinding

class DoctrineFragment : Fragment(), DoctrineAdapter.ClickListener{
    private lateinit var binding: DoctrineFragmentBinding
    private val topicText: Array<String>
        get() {
       return context?.resources?.getStringArray(R.array.heading) as Array<String>
        }
    private val doctrineText: Array<String>
        get() {
            return context?.resources?.getStringArray(R.array.body) as Array<String>
        }
    private val bodyText: Array<String>
        get() {
            return context?.resources?.getStringArray(R.array.doctrine) as Array<String>
        }

    companion object {
        fun newInstance() = DoctrineFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.doctrine_fragment, container, false)
        val adapter = context?.let { DoctrineAdapter(topicText,doctrineText, bodyText, it, this) }
        binding.recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onClicked(view: View?, mposition: Int) {
        val doctrine = Doctrine(doctrineText[mposition], topicText[mposition], bodyText[mposition])
        val action = DoctrineFragmentDirections.actionDoctrineFragmentToDoctrineDetailFragment(doctrine)
        view?.let { Navigation.findNavController(it).navigate(action) }
    }

}