package org.dclm.live.ui.sermon

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SermonFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SermonViewModel::class.java)) {
            return SermonViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}