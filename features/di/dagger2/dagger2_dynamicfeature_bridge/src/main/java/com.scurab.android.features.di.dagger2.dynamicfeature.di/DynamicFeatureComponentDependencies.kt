package com.scurab.android.features.di.dagger2.dynamicfeature.di

import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.base.SessionManager
import com.scurab.android.features.di.dagger2.base.di.DIComponentDependencies
import com.scurab.android.features.di.dagger2.base.util.AppCore
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.base.util.SessionToken

/**
 * This is iface for all necessary dependencies for the DynamicFeature module.
 * As the [DynamicFeatureComponent] must be dagger [@Component] instead of [@Subcomponent] (because dynamic feature depends on the app and
 * not viceversa as for standard android lib module).
 * So it has to be told that these dependencies are coming from somewhere else. That means
 * - Every single dependency has to be explicitly specified here
 * - This iface is used for extending of some component in the app (InternalActivityComponent in this sample)
 * - In case of multiple ifaces they have to have matching names if they have same type, otherwise dagger will take it
 *   as ambiguous definition and will fail with "Dagger/DuplicateBindings" error
 * - very strict common naming should be applied through project, something like `provideSomeType() : SomeType` to avoid issues
 * mentioned in prev point
 */
//just basic iface, all those refs will be provided via InternalComponent => InternalComponent has to implement this iface
interface DynamicFeatureComponentDependencies : DIComponentDependencies {

    fun provideAppCore(): AppCore
    fun provideSessionToken(): SessionToken
    fun provideSessionManager(): SessionManager
    fun provideActivity(): Reference<AppCompatActivity>
    fun provideDynamicFeatureNavigation() : DynamicFeatureNavigation
}