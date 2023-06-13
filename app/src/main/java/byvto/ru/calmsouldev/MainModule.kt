package byvto.ru.calmsouldev

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import byvto.ru.calmsouldev.data.local.FilesDatabase
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
    fun provideDb(app: Application): FilesDatabase {
        return Room.databaseBuilder(
            app, FilesDatabase::class.java, "file_db"
        ).build()
    }
}