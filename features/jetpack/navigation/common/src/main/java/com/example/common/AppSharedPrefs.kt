package com.example.common

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AppSharedPrefs(prefs: SharedPreferences) {
    private val sharedPrefs = SharedPreferencesWrapper(prefs)
    var isLoggedIn: Boolean by sharedPrefs.boolean
}


private class SharedPreferencesWrapper(
    private val sharedPrefs: SharedPreferences
) {
    val boolean: ReadWriteProperty<AppSharedPrefs, Boolean> =
        object : ReadWriteProperty<AppSharedPrefs, Boolean> {
            override fun getValue(thisRef: AppSharedPrefs, property: KProperty<*>): Boolean {
                return sharedPrefs.getBoolean(property.name, false)
            }

            override fun setValue(thisRef: AppSharedPrefs, property: KProperty<*>, value: Boolean) {
                return sharedPrefs.edit().putBoolean(property.name, value).apply()
            }
        }
}