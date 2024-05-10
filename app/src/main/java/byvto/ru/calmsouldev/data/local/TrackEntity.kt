package byvto.ru.calmsouldev.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import byvto.ru.calmsouldev.domain.model.Track

@Entity
data class TrackEntity(

    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val fileName: String,
    val isFinished: Boolean,
    val order: Int
) {
    fun toTrack() : Track {
        return Track(
            id = id,
            fileName = fileName,
            isFinished = isFinished,
            order = order
        )
    }
}
