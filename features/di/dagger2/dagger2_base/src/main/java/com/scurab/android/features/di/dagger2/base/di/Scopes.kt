package com.scurab.android.features.di.dagger2.base.di

import javax.inject.Scope

/**
 * AppScope
 * - network layer
 * - core
 *
 * (Anything what is tied moreless to the app process)
 */
@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class AppScope

/**
 * SessionScope
 * - SessionToken
 *
 * (Anything what is tied to some wider time of the app usage,
 * for example for time between log-in log-out)
 */
@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class SessionScope
/**
 * Internal scope, this is only for the case when
 * we have some usecase what needs to have an activity instance.
 * For example 3rd party library etc.
 */
@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class InternalActivityScope

/**
 * Activity scope is tied rather to some activity (process)
 * This might be tied to:
 * - Android Activity instance
 * - Android View Model's instance (=> survives rotation)
 */
@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class ActivityScope
