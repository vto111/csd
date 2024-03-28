package byvto.ru.calmsouldev.data.local

import androidx.room.Dao
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

    @Query("UPDATE trackentity SET isFinished = :flag WHERE id IS :id")
    suspend fun finishedById(id: Int, flag: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM trackentity)")
    suspend fun checkNewDevice(): Boolean
}