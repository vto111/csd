package byvto.ru.calmsouldev.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import byvto.ru.calmsouldev.model.Files

@Entity
data class FilesEntity(

    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val fileName: String,
    val finished: Boolean
    // что еще может быть нужно в таблице?
) {
    fun toFiles() : Files {
        return Files(
            id = id,
            fileName = fileName,
            finished = finished
        )
    }
}
