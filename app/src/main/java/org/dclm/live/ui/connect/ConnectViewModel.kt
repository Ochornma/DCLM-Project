package org.dclm.live.ui.connect

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import org.dclm.live.R

class ConnectViewModel : ViewModel() {
    fun socialMedia(context: Context, socialMedia: String) {
        val intent2 = Intent(Intent.ACTION_VIEW, Uri.parse(socialMedia))
        context.startActivity(intent2)
    }

    fun email(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.type = "message/rfc822"
        emailIntent.data = Uri.parse("mailto:ict@dclm.org") // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "ict@dclm.org")
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.inquiry))
        try {
            context.startActivity(Intent.createChooser(emailIntent, context.resources.getString(R.string.chose_app)))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, context.resources.getString(R.string.no_app), Toast.LENGTH_LONG).show()
        }
    }

    fun locate(context: Context){
        val loc: String = context.resources.getString(R.string.deeper_life)
        // Parse the location and create the intent.
        // Parse the location and create the intent.
        val addressUri = Uri.parse("geo:0,0?q=$loc")
        val intent3 = Intent(Intent.ACTION_VIEW, addressUri)

        // Find an activity to handle the intent, and start that activity.

        // Find an activity to handle the intent, and start that activity.
        if (intent3.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent3)
        } else {
            Toast.makeText(context, context.resources.getString(R.string.no_app), Toast.LENGTH_SHORT).show()
        }
    }
}