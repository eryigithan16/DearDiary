package com.example.deardiary.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.deardiary.util.Constants.IMAGE_TO_DELETE_TABLE

@Entity(tableName = IMAGE_TO_DELETE_TABLE)
data class ImageToDelete(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val remoteImagePath: String,

)