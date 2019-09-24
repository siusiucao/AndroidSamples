package com.scurab.android.features.di.dagger2.base.util

import com.scurab.android.features.di.dagger2.base.di.SessionScope

/**
 * Object tied to a session scope
 */
@SessionScope
data class SessionToken(val id: Int)