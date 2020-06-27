package org.dclm.live.ui.experience.prayer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.dclm.live.ui.util.PrayerSubmitted

class PrayerFactory (val prayerSubmitted: PrayerSubmitted, val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrayerViewModel::class.java)) {
            return PrayerViewModel(prayerSubmitted, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}