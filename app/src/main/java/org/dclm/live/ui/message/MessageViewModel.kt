package org.dclm.live.ui.message

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import kotlinx.coroutines.*
import org.dclm.live.ui.util.BlogRecieved
import org.dclm.live.ui.util.DCLMRepository


class MessageViewModel(val context: Context, val blogRecieved: BlogRecieved) : ViewModel() {
    private val repository = DCLMRepository(context, blogRecieved)
    private var getstack: LiveData<PagedList<Blog>>? = null
    var itemPagedList: LiveData<PagedList<Blog>>? = null
    private var liveDataSource: LiveData<PageKeyedDataSource<Int, Blog>>? = null

    init {

        val dataSource = BlogDataFactory(context)
        liveDataSource = dataSource.getBlog()
        val config = (PagedList.Config.Builder())
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()
        itemPagedList = (LivePagedListBuilder(dataSource, config)).build()
    }

    fun getBlog(){
        return repository.getBlog()
    }

    /*fun getAllBlog(): LiveData<MutableList<Blog>>? {
        return repository.getAllBlogs()
    }*/

/*   suspend fun count(): Long? = withContext(Dispatchers.IO){
       getcount()
   }*/

 /* fun getcount(): Deferred<Long?> {
       return viewModelScope.async(Dispatchers.IO) {
           return@async repository.count()
       }
    }*/

 /*   fun insert(blog: Blog){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.insertBlog(blog)
            }
        }
    }*/

  /*  fun update(blog: Blog){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.updateBlog(blog)
            }
        }
    }*/

}