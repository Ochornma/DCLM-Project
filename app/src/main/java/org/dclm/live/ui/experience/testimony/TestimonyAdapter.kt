package org.dclm.live.ui.experience.testimony

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.dclm.live.R
import org.dclm.live.databinding.TestimonyListBinding

class TestimonyAdapter: RecyclerView.Adapter<TestimonyAdapter.TestimonyHolder>() {

    private lateinit var  testiony:MutableList<Testimony>

    fun setTestimony(testimony:MutableList<Testimony>){
        this.testiony = testimony
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestimonyHolder {
        val binding = DataBindingUtil.inflate<TestimonyListBinding>(LayoutInflater.from(parent.context), R.layout.testimony_list, parent, false)
        return TestimonyHolder(binding)
    }

    override fun getItemCount(): Int {
        return testiony.size
    }

    override fun onBindViewHolder(holder: TestimonyHolder, position: Int) {
        holder.binding.testimony = testiony[position]

    }

    inner class TestimonyHolder(val binding: TestimonyListBinding): RecyclerView.ViewHolder(binding.root) {

    }

}