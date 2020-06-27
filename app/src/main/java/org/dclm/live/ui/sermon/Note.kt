package org.dclm.live.ui.sermon

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "note_table")
@Parcelize
@Keep
data class Note(@PrimaryKey(autoGenerate = true)
                var id: Int = 0,
                var date: String?, var speaker: String?, var service: String?,
                var topic: String?, var description: String?,
                var point1: String?, var point1_description: String?,
                var point2: String?, var point2_description: String?,
                var point3: String?, var point3_description: String?,
                var point4: String?, var point4_description: String?,
                var decision: String?
) : Parcelable

/*
private const val id = 0

private val date: String? = null

private val speaker: String? = null

private val service: String? = null

private val topic: String? = null
private val description: String? = null

private val point1: String? = null
private val point1_description: String? = null

private val point2: String? = null
private val point2_description: String? = null

private val point3: String? = null
private val point3_description: String? = null

private val point4: String? = null
private val point4_description: String? = null

private val decision: String? = null*/
