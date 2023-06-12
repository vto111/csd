package byvto.ru.calmsouldev.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FilesEntity::class],
    version = 1
)
abstract class FilesDatabase: RoomDatabase() {

    abstract val dao: FilesDao
}