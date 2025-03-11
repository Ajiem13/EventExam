package com.example.eventexam.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import okio.Closeable

@Entity(tableName = "events")
@Parcelize
data class EventEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id : Int = 0,

    @ColumnInfo(name = "name")
    val name: String = "Unknown",

    @field:ColumnInfo(name = "description")
    val description: String = "Unknown",

    @field:ColumnInfo(name = "summary")
    val summary: String = "",

    @field:ColumnInfo(name = "imageLogo")
    val imageLogo: String = ""


) : Parcelable


