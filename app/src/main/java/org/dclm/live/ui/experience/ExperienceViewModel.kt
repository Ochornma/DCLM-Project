package org.dclm.live.ui.experience

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import org.dclm.live.R

class ExperienceViewModel : ViewModel() {

    fun navigateHelp(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_experienceFragment_to_helpFragment)
    }

    fun navigateTestimony(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_experienceFragment_to_testimonyFragment)
    }

    fun navigatePrayer(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_experienceFragment_to_prayerFragment)
    }
}