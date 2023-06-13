package byvto.ru.calmsouldev.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sound_track")
data class SoundTrack(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val soundTrackFileName: String,
    val soundTrackNumber: Int,
    val soundTrackState: Int,
    val soundTrackColor: String
)
