package org.dclm.live.ui.util

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

object VolleyRequest {
    private var volley: RequestQueue? = null

    @Synchronized
    fun getVolley(context: Context): RequestQueue? {
        if (volley == null) {
            volley = Volley.newRequestQueue(context.applicationContext)
        }
        return volley
    }
}
