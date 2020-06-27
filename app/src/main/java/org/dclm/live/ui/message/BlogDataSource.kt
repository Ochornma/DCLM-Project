package org.dclm.live.ui.message

import android.content.Context
import android.widget.Toast
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.dclm.live.ui.util.VolleyRequest

class BlogDataSource(var context: Context) : PageKeyedDataSource<Int, Blog>() {
    private var mQuene = VolleyRequest.getVolley(context)
    private val firstPage = 1
     //var blog: MutableList<Blog> = ArrayList()
    private val blog: List<Blog> = java.util.ArrayList()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Blog>) {
        val request = JsonArrayRequest(Request.Method.GET, "https://dclm.org/wp-json/wp/v2/posts?page=$firstPage", null, Response.Listener {
            for (i in 0 until it.length()){
                try {
                    val heading = it.getJSONObject(i)
                    val jsonObject = heading.getJSONObject("title")
                    val title = jsonObject.getString("rendered")
                    val time = heading.getString("date_gmt")
                    val jsonObject1 = heading.getJSONObject("content")
                    val content = jsonObject1.getString("rendered")
                   // blog.add(Blog(title = title, date = time, body = content))
                    blog.plus(Blog(title = title, date = time, body = content))
                    //Toast.makeText(context, blog[i].body, Toast.LENGTH_SHORT).show()
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
            callback.onResult(blog, null, firstPage + 1)
        }, Response.ErrorListener {

        })
        mQuene?.add(request)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Blog>) {
        val request = JsonArrayRequest(Request.Method.GET, "https://dclm.org/wp-json/wp/v2/posts?page=${params.key}", null, Response.Listener {

            for (i in 0 until it.length()){
                try {
                    val heading = it.getJSONObject(i)
                    val jsonObject = heading.getJSONObject("title")
                    val title = jsonObject.getString("rendered")
                    val time = heading.getString("date_gmt")
                    val jsonObject1 = heading.getJSONObject("content")
                    val content = jsonObject1.getString("rendered")
                 //   blog.add(Blog(title = title, date = time, body = content))
                    Toast.makeText(context, blog[i].body, Toast.LENGTH_SHORT).show()
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }

            callback.onResult(blog, params.key + 1)
        }, Response.ErrorListener {

        })
        mQuene?.add(request)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Blog>) {
        val request = JsonArrayRequest(Request.Method.GET, "https://dclm.org/wp-json/wp/v2/posts?page=${params.key}", null, Response.Listener {
            for (i in 0 until it.length()){
                try {
                    val heading = it.getJSONObject(i)
                    val jsonObject = heading.getJSONObject("title")
                    val title = jsonObject.getString("rendered")
                    val time = heading.getString("date_gmt")
                    val jsonObject1 = heading.getJSONObject("content")
                    val content = jsonObject1.getString("rendered")
                   // blog.add(Blog(title = title, date = time, body = content))
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
            val key =
            if(params.key > 1){
                params.key - 1
            } else{
                null
            }
            callback.onResult(blog, key)
        }, Response.ErrorListener {

        })
        mQuene?.add(request)
    }
}