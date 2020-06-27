package org.dclm.live.ui.util

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.dclm.live.R
import org.dclm.live.ui.experience.testimony.Testimony
import org.dclm.live.ui.message.Blog
import org.dclm.live.ui.message.BlogDao
import org.dclm.live.ui.message.BlogDataFactory
import org.dclm.live.ui.radio.SubTitle
import org.dclm.live.ui.sermon.Note
import org.dclm.live.ui.sermon.NoteDao
import org.dclm.live.ui.util.db.DCLMDataBase
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class DCLMRepository(val context: Context) {
    private var mQueue: RequestQueue? = null
    private lateinit var subtitleReceived: SubtitleReceived
    private lateinit var testimonyRecieved: TestimonyRecieved
    private lateinit var prayerSubmitted: PrayerSubmitted
    private lateinit var blogRecieved: BlogRecieved
    private var itemPagedList: LiveData<PagedList<Blog>>? = null
    private var liveDataSource: LiveData<PageKeyedDataSource<Int, Blog>>? = null
    private var noteDAO: NoteDao? = null
   // private var blogDAO: BlogDao? = null
    private var allNotes: LiveData<MutableList<Note>>? = null
    private  lateinit var blog: MutableList<Blog>
   // private var allBlogs: LiveData<MutableList<Blog>>? = null

    init {
        val database = DCLMDataBase.getAppDataBase(context)
        noteDAO = database?.noteDAO()
       // blogDAO = database?.blogDAO()
        allNotes = noteDAO?.getAllNotes()
      // allBlogs = blogDAO?.getAllBlogs()
    }

    constructor(context: Context, subtitleReceived: SubtitleReceived) : this(context) {
        this.subtitleReceived = subtitleReceived
        mQueue = VolleyRequest.getVolley(context)
    }

    constructor(context: Context, testimonyRecieved: TestimonyRecieved):this(context){
        this.testimonyRecieved = testimonyRecieved
        mQueue = VolleyRequest.getVolley(context)
    }

    constructor(context: Context,  blogRecieved: BlogRecieved):this(context){
        this.blogRecieved = blogRecieved
        mQueue = VolleyRequest.getVolley(context)
    }

    constructor(context: Context,  prayerSubmitted: PrayerSubmitted):this(context){
        this.prayerSubmitted = prayerSubmitted
        mQueue = VolleyRequest.getVolley(context)
    }

    init {
        val dataSource = BlogDataFactory(context)
        liveDataSource = dataSource.getBlog()
        val config = (PagedList.Config.Builder())
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()
        itemPagedList = (LivePagedListBuilder(dataSource, config)).build()
    }

    fun jsonParse(url: String) {
        val request: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response: JSONObject ->
                    try {
                        val nowPlaying = response.getJSONObject("now_playing")
                        val song = nowPlaying.getJSONObject("song")
                        val minister = song.getString("artist")
                        val topic = song.getString("title")
                        val listeners = response.getJSONObject("listeners")
                        val number = listeners.getString("total")
                        val listining = context.resources.getString(R.string.listning) + number
                        val subTitle = SubTitle(topic, minister, listener = listining)
                        subtitleReceived.subtitle(subTitle)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError ->
                    error.printStackTrace()
                    val subTitle = SubTitle(
                        context.resources.getString(R.string.message),
                        context.resources.getString(R.string.ministering),
                        " "
                    )
                    subtitleReceived.error(subTitle)
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers: MutableMap<String, String> =
                        HashMap()
                    headers["dclm header not for public"] =
                        "dclm header not for public"
                    headers["dclm header not for public"] = "dclm header not for public"
                    return headers
                }
            }
        mQueue!!.add(request)
    }

    fun parseTestimony() {
        val testimonies: MutableList<Testimony> = ArrayList<Testimony>()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, "dclm api not for public", null, Response.Listener { response: JSONObject ->
                    try {
                        val meta = response.getJSONObject("meta")
                        val count = meta.getInt("count")
                        for (i in count downTo count - 25) {
                            val result = response.getJSONObject("result")
                            val data = result.getJSONObject("data")
                            try {
                                val id = data.getJSONObject(i.toString())
                                val name = id.getString("name")
                                val subject = id.getString("subject")
                                val testimony = id.getString("testimony")
                                testimonies.add(Testimony(name, subject, testimony))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    testimonyRecieved.testimontRecieved(testimonies)
                },
                Response.ErrorListener {

                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers: MutableMap<String, String> =
                        HashMap()
                    headers["dclm header not for public"] =
                        "dclm header not for public"
                    headers["dclm header not for public"] = "dclm header not for public"
                    return headers
                }
            }
        mQueue!!.add(jsonObjectRequest)
    }

    fun parseTestimonyPost(name: String, email:String, subject: String, testimony: String, state: String, region: String, district: String) {
        val jsonBody = JSONObject()
        try {
            jsonBody.put("name", name)
            jsonBody.put("email", email)
            jsonBody.put("subject", subject)
            jsonBody.put("district", district)
            jsonBody.put("state", state)
            jsonBody.put("region", region)
            jsonBody.put( "testimony", testimony)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val testimonies: MutableList<Testimony> = ArrayList<Testimony>()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "dclm api not for public", jsonBody, Response.Listener {
            parseTestimony()
           // testimonyRecieved.testimontRecieved(testimonies)
        },
            Response.ErrorListener {
                testimonyRecieved.error()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> =
                    HashMap()
                headers["dclm header not for public"] =
                    "dclm header not for public"
                headers["dclm header not for public"] = "dclm header not for public"
                return headers
            }
        }
        mQueue!!.add(jsonObjectRequest)
    }
    fun parsePrayerPost(name: String, email:String, subject: String, request: String) {
        val jsonBody = JSONObject()
        try {
            jsonBody.put("name", name)
            jsonBody.put("email", email)
            jsonBody.put("subject", subject)
            jsonBody.put( "request", request)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "dclm api not for public", jsonBody, Response.Listener {

            prayerSubmitted.submitted()
        },
            Response.ErrorListener {
                prayerSubmitted.errorInSubmit()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> =
                    HashMap()
                headers["dclm header not for public"] =
                    "dclm header not for public"
                headers["dclm header not for public"] = "dclm header not for public"
                return headers
            }
        }
        mQueue!!.add(jsonObjectRequest)
    }

    fun getBlog(){
        blog = ArrayList<Blog>()
        val request = JsonArrayRequest(Request.Method.GET, "dclm api not for public", null, Response.Listener {
            for (i in 0 until it.length()){
                try {
                    val heading = it.getJSONObject(i)
                    val jsonObject = heading.getJSONObject("title")
                    val title = jsonObject.getString("rendered")
                    val time = heading.getString("date_gmt")
                    val jsonObject1 = heading.getJSONObject("content")
                    val content = jsonObject1.getString("rendered")
                    blog.add(Blog(title = title, date = time, body = content))
                    //Toast.makeText(context, blog[i].body, Toast.LENGTH_SHORT).show()
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
            //blogRecieved.blogCame(blog)
            getBlog2()

        }, Response.ErrorListener {

        })
        mQueue?.add(request)
    }

    fun getBlog2(){
        val request = JsonArrayRequest(Request.Method.GET, "dclm api not for public", null, Response.Listener {
            for (i in 0 until it.length()){
                try {
                    val heading = it.getJSONObject(i)
                    val jsonObject = heading.getJSONObject("title")
                    val title = jsonObject.getString("rendered")
                    val time = heading.getString("date_gmt")
                    val jsonObject1 = heading.getJSONObject("content")
                    val content = jsonObject1.getString("rendered")
                    blog.add(Blog(title = title, date = time, body = content))
                    //Toast.makeText(context, blog[i].body, Toast.LENGTH_SHORT).show()
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
            blogRecieved.blogCame(blog)

        }, Response.ErrorListener {

        })
        mQueue?.add(request)
    }

     suspend fun insert(note: Note) {
        noteDAO?.insert(note)
    }

    suspend fun update(note: Note) {
        noteDAO?.update(note)
    }

 /*   suspend fun insertBlog(blog: Blog) {
        blogDAO?.insert(blog)
    }

   suspend fun updateBlog(blog: Blog) {
        blogDAO?.update(blog)
    }
   suspend fun count(): Long? {
        return blogDAO?.countDataBase()
    }*/
    suspend fun delete(note: Note) {
        noteDAO?.delete(note)
    }

    suspend fun deleteAll() {
        noteDAO?.deleteAllNotes()
    }

    fun getAllNotes(): LiveData<MutableList<Note>>? {
        return allNotes
    }

   /* fun getAllBlogs(): LiveData<MutableList<Blog>>? {
        return allBlogs
    }*/
}