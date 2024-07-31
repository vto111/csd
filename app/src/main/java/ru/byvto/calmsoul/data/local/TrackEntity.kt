package ru.byvto.calmsoul.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.byvto.calmsoul.domain.model.Track

@Entity
data class TrackEntity(

    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val description: String,
    val fileName: String,
    val isFinished: Boolean,
    val order: Int
) {
    fun toTrack() : Track {
        return Track(
            id = id,
            description = description,
            fileName = fileName,
            isFinished = isFinished,
            order = order
        )
    }
}
