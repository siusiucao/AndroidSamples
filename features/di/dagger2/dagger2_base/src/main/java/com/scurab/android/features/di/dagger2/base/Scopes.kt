package com.scurab.android.features.di.dagger2.base

import kotlin.annotation.Retention
import javax.inject.Scope

@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class AppScope

@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class SessionScope

@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class InternalActivityScope

@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class ActivityScope
