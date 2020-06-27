package org.dclm.live.ui.message

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import org.dclm.live.R
import org.dclm.live.databinding.MessageFragmentBinding
import org.dclm.live.ui.util.BlogRecieved

class MessageFragment : Fragment(), BlogAdapter.OnClicked, BlogRecieved {
    private lateinit var binding: MessageFragmentBinding
    private lateinit var adapter: BlogAdapter
    private var sharedPreferences: SharedPreferences? = null
    private  var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
   val PREFRENCES = "org.dclm.radio"

    companion object {
        fun newInstance() = MessageFragment()
    }

    private lateinit var viewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.message_fragment, container, false)
        sharedPreferences = activity?.getSharedPreferences(PREFRENCES, Context.MODE_PRIVATE)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.progressBar.visibility =View.VISIBLE
        //adapter = context?.let { BlogAdapter(this, it) }!!
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = context?.let { BlogAdapter(this, it) }!!
        val factory = context?.let { BlogModelFactory(it, this) }
        viewModel = factory?.let { ViewModelProvider(this, it).get(MessageViewModel::class.java) }!!
        viewModel.getBlog()
   /*     viewModel.getAllBlog()?.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility =View.GONE
            adapter.setBlog(it)
            binding.recyclerView.adapter = adapter
        })*/
        viewModel.itemPagedList?.observe(viewLifecycleOwner, Observer {
           /* adapter?.submitList(it)
            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show()
            Log.i("confirm", "1")*/
        })


    }

    override fun clicked(view: View, blog: Blog) {
        val action = MessageFragmentDirections.actionMessageFragmentToBlogDetailFragment(blog)
        Navigation.findNavController(view).navigate(action)
    }

    override fun blogCame(blog: MutableList<Blog>) {

        if (sharedPreferences?.getBoolean("blog", false) == false){
            for (i in 0 until blog.size){
               // viewModel.insert(blog[i])
            }
            editor = sharedPreferences?.edit()
            editor?.putBoolean("blog", true)
            editor?.apply()
        } else{
            for (i in 0 until blog.size){
                val blog1 = blog[i]
                blog1.id = i + 1
                //viewModel.update(blog1)

            }
        }
    }

}