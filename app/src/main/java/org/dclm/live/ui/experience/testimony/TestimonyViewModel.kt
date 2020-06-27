package org.dclm.live.ui.experience.testimony

import android.content.Context
import androidx.lifecycle.ViewModel
import org.dclm.live.ui.util.DCLMRepository
import org.dclm.live.ui.util.TestimonyRecieved

class TestimonyViewModel(testimonyRecieved: TestimonyRecieved, val context: Context) : ViewModel() {
    private var repository:DCLMRepository = DCLMRepository(context, testimonyRecieved)

    fun getTestimony(){
        repository.parseTestimony()
    }

    fun postTestimony(name: String, email:String, subject: String, testimony: String, state: String, region: String, district: String){
        repository.parseTestimonyPost(name, email, subject, testimony, state, region, district)
    }
}