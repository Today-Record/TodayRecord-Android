package com.todayrecord.data.di

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore
import com.todayrecord.data.datastore.PreferenceStorageImpl
import com.todayrecord.domain.util.PreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DataStoreModule {

    private val Context.prefDataStore by preferencesDataStore(
        name = PreferenceStorageImpl.PREFS_NAME,
        produceMigrations = { context ->
            listOf(
                SharedPreferencesMigration(context, PreferenceStorageImpl.PREFS_NAME)
            )
        }
    )

    @Singleton
    @Provides
    fun provideDataStoreModule(
        @ApplicationContext context: Context
    ): PreferenceStorage = PreferenceStorageImpl(context.prefDataStore)
}