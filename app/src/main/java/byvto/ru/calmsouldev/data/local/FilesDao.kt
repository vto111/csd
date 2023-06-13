package byvto.ru.calmsouldev.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(id: FilesEntity)

    @Query("SELECT * FROM filesentity")
    suspend fun getAll(): List<FilesEntity>

    @Query("SELECT * FROM filesentity WHERE id IS :id")
    suspend fun getById(id: Int): FilesEntity
}