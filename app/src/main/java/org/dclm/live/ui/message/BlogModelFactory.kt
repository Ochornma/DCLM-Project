package org.dclm.live.ui.message

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.dclm.live.ui.util.BlogRecieved

class BlogModelFactory(var context: Context, var blogRecieved: BlogRecieved): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            return MessageViewModel(context, blogRecieved) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}