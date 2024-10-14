package com.eryigit.deardiary.di

import android.content.Context
import androidx.room.Room
import com.eryigit.deardiary.connectivity.NetworkConnectivityObserver
import com.eryigit.deardiary.data.database.ImagesDatabase
import com.eryigit.deardiary.util.Constants.IMAGES_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ImagesDatabase {
        return Room.databaseBuilder(context, ImagesDatabase::class.java, IMAGES_DATABASE).build()
    }

    @Provides
    @Singleton
    fun provideFirstDao(database: ImagesDatabase) = database.imageToUploadDao()

    @Provides
    @Singleton
    fun provideSecond(database: ImagesDatabase) = database.imageToDeleteDao()

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context) = NetworkConnectivityObserver(context)
}