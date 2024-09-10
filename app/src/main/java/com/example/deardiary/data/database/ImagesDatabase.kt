package com.example.deardiary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.deardiary.data.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class],
    version = 1,
    exportSchema = false
)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
}