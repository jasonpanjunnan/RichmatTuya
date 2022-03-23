package com.richmat.mytuya.util.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import com.richmat.mytuya.UserPreferences
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import com.richmat.mytuya.data.posts.PostsRepository
import com.richmat.mytuya.data.repository.SearchRepository
import com.richmat.mytuya.data.repository.impl.SearchDeviceRepository
import com.richmat.mytuya.data.room.TuyaDeviceDatabase
import com.richmat.mytuya.data.store.userPreferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //start 这玩意也太容易忘了。提供不同的实现方式，如接口，建造者方式，使用 @ + 类名。暂时无用，用作记忆,最好为所有情况都写一个，防止找不着
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OtherInterceptorOkHttpClient
    // end

    @Provides
    @Singleton
    fun providesDataBase(@ApplicationContext Context: Context): TuyaDeviceDatabase {
        return Room.databaseBuilder(
            Context,
            TuyaDeviceDatabase::class.java,
            "device_database"
        ).build()
    }

    //hilt大有用途啊
    @Provides
    fun provideUserPrefs(@ApplicationContext context: Context): DataStore<UserPreferences> =
        context.userPreferencesDataStore

}

@Module
@InstallIn(SingletonComponent::class)
abstract class interfaceAppMoudle {

    @Singleton
    @Binds
    abstract fun bindSearchRepository(
        repo: SearchDeviceRepository,
    ): SearchRepository

    @Singleton
    @Binds
    abstract fun bindRepository(
        repo1: FakePostsRepository,
    ): PostsRepository

}