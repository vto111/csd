package byvto.ru.calmsouldev.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TrackEntity::class],
    version = 1
)
abstract class TracksDatabase: RoomDatabase() {

    abstract val dao: TracksDao
}