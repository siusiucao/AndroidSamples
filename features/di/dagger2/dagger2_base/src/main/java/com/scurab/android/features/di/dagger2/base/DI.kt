package com.scurab.android.features.di.dagger2.base

import androidx.appcompat.app.AppCompatActivity

interface DIComponent
interface DIComponentProvider

interface DIComponentHolder<T : DIComponent> {
    val component: T
}

interface InternalActivityComponentProvider : DIComponentProvider {
    fun internalActivityComponent(activity: AppCompatActivity): DIComponent
}