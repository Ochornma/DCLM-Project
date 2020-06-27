package org.dclm.live.ui.message

import android.os.Build
import android.os.Parcelable
import android.text.Html
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Blog(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @NonNull
    var title: String,
    @NonNull
    var date: String,
    @NonNull
    var body: String) :
    Parcelable {

    val heading:String
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml("<p align=\"justify\"> $title</p>", Html.FROM_HTML_MODE_COMPACT).toString()
            } else {
                Html.fromHtml("<p align=\"justify\"> $title</p>").toString()
            }
    }

}