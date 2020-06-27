package org.dclm.live.ui.doctrine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.dclm.live.R
import org.dclm.live.databinding.DoctrineItemBinding

class DoctrineAdapter( val topic: Array<String>, val doctrine: Array<String>, val body: Array<String>, val context: Context, val onclick: ClickListener) : RecyclerView.Adapter<DoctrineAdapter.DoctrineHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctrineAdapter.DoctrineHolder {
        val binding = DataBindingUtil.inflate<DoctrineItemBinding>(LayoutInflater.from(parent.context), R.layout.doctrine_item, parent, false)
        return DoctrineHolder(binding)
    }

    override fun getItemCount(): Int = topic.size

    override fun onBindViewHolder(holder: DoctrineAdapter.DoctrineHolder, position: Int) {
        val doctrineNumber = context.resources.getString(R.string.doctrine) + " " + position.toString()
        holder.binding.heading = topic[position]

        if (position == 0) {
            holder.binding.doctrineNumber.text = " "
        } else {
            holder.binding.doctrineNumber.text = doctrineNumber
        }

        holder.binding.root.setOnClickListener {
            onclick.onClicked(it, position)
        }

    }

    inner class DoctrineHolder(val binding: DoctrineItemBinding): RecyclerView.ViewHolder(binding.root)

    interface ClickListener {
        fun onClicked(view: View?, mposition: Int)
    }
}