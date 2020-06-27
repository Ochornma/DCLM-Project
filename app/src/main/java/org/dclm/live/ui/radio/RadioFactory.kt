package org.dclm.live.ui.radio

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.dclm.live.ui.util.SubtitleReceived

class RadioFactory(val context: Context, private val subtitleReceived: SubtitleReceived) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RadioViewModel::class.java)) {
            return RadioViewModel(context, subtitleReceived) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}