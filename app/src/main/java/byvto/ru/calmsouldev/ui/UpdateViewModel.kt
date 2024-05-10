package byvto.ru.calmsouldev.ui

import android.app.DownloadManager
import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import byvto.ru.calmsouldev.data.local.TracksDatabase
import byvto.ru.calmsouldev.domain.repository.CalmSoulRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateViewModel @Inject constructor(
                       private val repo: CalmSoulRepo,
                      private val db: TracksDatabase,
                      @ApplicationContext context: Context
): ViewModel() {

    init {
        downloadFromPath(context, 	"08e8d143-dadf-4aa4-a0a8-69ede201982e.ogg")
    }

    private fun checkApi() {
        viewModelScope.launch {
            val list = repo.tracks()
            println(list)
        }
    }

    fun downloadFromPath(context: Context, filename: String): Long {
        val downloadManager = context.getSystemService(DownloadManager::class.java)
        val request = DownloadManager.Request("https://api.byvto.ru/uploads/".plus(filename).toUri())
            .apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                setDestinationInExternalFilesDir(context, "/tracks", filename)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            }
        return downloadManager.enqueue(request)
    }

}