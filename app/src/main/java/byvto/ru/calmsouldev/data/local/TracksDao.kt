package byvto.ru.calmsouldev.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM trackentity")
    suspend fun getAll(): List<TrackEntity>

    @Query("SELECT * FROM trackentity WHERE id IS :id")
    suspend fun getById(id: Int): TrackEntity

    @Query("UPDATE trackentity SET isFinished = 1 WHERE id IS :id")
    suspend fun setFinished(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM trackentity)")
    suspend fun checkNewDevice(): Boolean

    @Query("UPDATE trackentity SET isFinished = 0")
    suspend fun resetFinished()

    @Query("DELETE FROM trackentity WHERE id IS :id")
    suspend fun deleteById(id: Int)
}