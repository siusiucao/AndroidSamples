package com.scurab.android.features.di.dagger2.base.util

import android.app.Application
import com.scurab.android.features.di.dagger2.base.di.AppScope
import javax.inject.Inject

/**
 * Some class living inside the application component and lifecycle is tied to application (or whoever owns the AppComponent instance)
 */
@AppScope
class AppCore @Inject constructor(private val app: Application) {

    init {
        instanceCounter++
    }

    val instances get() = instanceCounter

    companion object {
        private var instanceCounter = 0
    }
}