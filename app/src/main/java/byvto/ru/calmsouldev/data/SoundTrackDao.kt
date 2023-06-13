package byvto.ru.calmsouldev.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Objects

@Dao
interface SoundTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    fun insertAllSoundTrack(objects: List<SoundTrack>)

    @Query("SELECT * FROM sound_track ORDER BY id ASC")
    fun readAllSoundTrack(): LiveData<List<SoundTrack>>
}