package com.example.common

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import java.lang.StringBuilder
import java.lang.reflect.Field
import java.util.AbstractCollection

val Context.appPrefs: AppSharedPrefs
    get() = AppSharedPrefs(
        getSharedPreferences(
            "App",
            Context.MODE_PRIVATE
        )
    )

val Fragment.appPrefs: AppSharedPrefs get() = requireContext().appPrefs