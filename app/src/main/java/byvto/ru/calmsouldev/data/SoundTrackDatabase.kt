package byvto.ru.calmsouldev.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SoundTrack::class],
    version = 1,
    exportSchema = false
)
abstract class SoundTrackDatabase: RoomDatabase() {
    abstract fun soundTrackDao(): SoundTrackDao
}