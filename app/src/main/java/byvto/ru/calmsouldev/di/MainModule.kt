package byvto.ru.calmsouldev.di

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.room.Room
import byvto.ru.calmsouldev.data.local.TracksDatabase
import byvto.ru.calmsouldev.data.remote.CalmSoulApi
import byvto.ru.calmsouldev.data.remote.CalmSoulApiImpl
import byvto.ru.calmsouldev.domain.repository.CalmSoulRepo
import byvto.ru.calmsouldev.domain.repository.CalmSoulRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.gson.gson
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun provideDb(app: Application): TracksDatabase {
        return Room.databaseBuilder(
            app, TracksDatabase::class.java, "tracks_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            // default validation to throw exceptions for non-2xx responses
            expectSuccess = true

            // set default request parameters
            defaultRequest {
                // add base url for all request
                url("https://api.byvto.ru:8443/")
            }

            // use gson content negotiation for serialize or deserialize
            install(ContentNegotiation) {
                gson()
            }
        }
    }

    @Provides
    @Singleton
    fun provideApi(
        httpClient: HttpClient
    ): CalmSoulApi {
        return CalmSoulApiImpl(httpClient)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @Provides
    @Singleton
    fun provideRepo(
        api: CalmSoulApi,
        db: TracksDatabase
    ): CalmSoulRepo {
        return CalmSoulRepoImpl(
            api = api,
            db = db
        )
    }
}