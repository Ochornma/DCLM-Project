package org.dclm.live.ui.experience.prayer

import android.content.Context
import androidx.lifecycle.ViewModel
import org.dclm.live.ui.util.DCLMRepository
import org.dclm.live.ui.util.PrayerSubmitted

class PrayerViewModel(prayerSubmitted: PrayerSubmitted, val context: Context) : ViewModel() {
    private var repository: DCLMRepository = DCLMRepository(context, prayerSubmitted)

    fun postPrayer(name: String, email:String, subject: String, request: String){
        repository.parsePrayerPost(name, email, subject, request)
    }

}