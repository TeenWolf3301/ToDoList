package com.teenwolf3301.to_do_list.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class SortOrder { BY_NAME, BY_DATE, BY_PRIORITY }

@Singleton
class PreferencesRepository @Inject constructor(@ApplicationContext context: Context) {

    private val Context.dataStore by preferencesDataStore("app_preferences")

    val preferencesFlow = context.dataStore.data.map {

    }

    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val HIDE_COMPLETED = stringPreferencesKey("hide_completed")
    }
}