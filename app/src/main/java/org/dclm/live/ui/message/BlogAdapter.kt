package org.dclm.live.ui.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.blog_list.view.*
import org.dclm.live.R



class BlogAdapter(var onClicked: OnClicked, var context: Context) : RecyclerView.Adapter<BlogAdapter.BlogHolder>() {
    private var blogs: MutableList<Blog> = ArrayList<Blog>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogHolder {
      // val binding = DataBindingUtil.inflate<BlogListBinding>(LayoutInflater.from(parent.context), R.layout.blog_list, parent, false)
        val view: View = LayoutInflater.from(context).inflate(R.layout.blog_list, parent, false)
        return BlogHolder(view)
    }

    override fun onBindViewHolder(holder: BlogHolder, position: Int) {
        val blog = blogs[position]
        holder.item.image.setImageResource(R.drawable.blog_one)
        holder.item.doctrine.text = blog.heading
        holder.item.setOnClickListener {
            blog.let { it1 -> onClicked.clicked(it, it1) }
        }
    }

    inner class BlogHolder(var item: View): RecyclerView.ViewHolder(item) {

    }

    fun setBlog(blog: MutableList<Blog>){
        this.blogs = blog
    }

companion object{
    private val diffCallback: DiffUtil.ItemCallback<Blog> = object : DiffUtil.ItemCallback<Blog>() {
        override fun areItemsTheSame(oldItem: Blog, newItem: Blog): Boolean {
            return oldItem.heading === newItem.heading
        }

        override fun areContentsTheSame(oldItem: Blog, newItem: Blog): Boolean {
            return oldItem == newItem
        }
    }
}


    interface OnClicked {
        fun clicked(view: View, blog: Blog)
    }

    override fun getItemCount(): Int {
        return blogs.size
    }
}