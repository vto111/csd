package byvto.ru.calmsouldev.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FilesEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val fileName: String,
    val finished: Boolean
    // что еще может быть нужно в таблице?
)
