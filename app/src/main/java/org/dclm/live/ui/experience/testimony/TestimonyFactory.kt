package org.dclm.live.ui.experience.testimony

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.dclm.live.ui.util.TestimonyRecieved

class TestimonyFactory(val testimonyRecieved: TestimonyRecieved, val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestimonyViewModel::class.java)) {
            return TestimonyViewModel(testimonyRecieved, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}