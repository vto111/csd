package byvto.ru.calmsouldev.di

import android.app.Application
import androidx.room.Room
import byvto.ru.calmsouldev.data.local.TracksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun provideDb(app: Application): TracksDatabase {
        return Room.databaseBuilder(
            app, TracksDatabase::class.java, "tracks_db"
        ).build()
    }
}