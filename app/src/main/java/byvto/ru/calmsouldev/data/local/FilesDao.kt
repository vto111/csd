package byvto.ru.calmsouldev.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFile(id: FilesEntity)

    @Query("SELECT * FROM filesentity")
    fun getAllFiles(): List<FilesEntity>

    @Query("SELECT * FROM filesentity WHERE id IS :id")
    fun getFile(id: Int): FilesEntity
}