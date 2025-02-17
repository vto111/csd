package ru.byvto.calmsoul.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TrackEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TracksDatabase: RoomDatabase() {

    abstract val dao: TracksDao
}