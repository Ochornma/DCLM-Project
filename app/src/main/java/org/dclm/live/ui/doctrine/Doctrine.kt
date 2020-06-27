package org.dclm.live.ui.doctrine

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class Doctrine(var content: String,
var heading: String,
var body: String): Parcelable