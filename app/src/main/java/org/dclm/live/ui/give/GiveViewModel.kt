package org.dclm.live.ui.give

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel

class GiveViewModel : ViewModel() {

    fun give(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://dclm.org/give-online/"))
        context.startActivity(intent)
    }
}