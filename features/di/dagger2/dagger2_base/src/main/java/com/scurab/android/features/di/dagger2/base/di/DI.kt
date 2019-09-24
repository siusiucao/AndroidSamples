package com.scurab.android.features.di.dagger2.base.di

import androidx.appcompat.app.AppCompatActivity

/**
 * Marker interface
 * For easier working with [AndroidInjector]
 */
interface DIComponent

/**
 * Marker interface
 * For easier working with [AndroidInjector]
 */
interface DIComponentProvider

/**
 * Marker interface
 * For easier working with [AndroidInjector]
 */
interface DIComponentDependencies

/**
 * Holder for the component
 * Activity has ti implement this, so any Fragment running in the particular activity can get
 * the Component [AndroidInjector.component]
 */
interface DIComponentHolder<T : DIComponent> {
    val component: T
}

interface InternalActivityComponentProvider : DIComponentProvider {
    fun internalActivityComponent(activity: AppCompatActivity): DIComponent
}