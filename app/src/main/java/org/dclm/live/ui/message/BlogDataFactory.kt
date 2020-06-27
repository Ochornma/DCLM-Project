package org.dclm.live.ui.message

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource


class BlogDataFactory(var context: Context) : DataSource.Factory<Int, Blog>() {
    private val itemLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, Blog>> =
        MutableLiveData<PageKeyedDataSource<Int, Blog>>()

    override fun create(): DataSource<Int, Blog> {
       val dataSource = BlogDataSource(context)
        itemLiveDataSource.postValue(dataSource)

        return dataSource
    }

    fun getBlog() :  MutableLiveData<PageKeyedDataSource<Int, Blog>>{
        return itemLiveDataSource
    }
}