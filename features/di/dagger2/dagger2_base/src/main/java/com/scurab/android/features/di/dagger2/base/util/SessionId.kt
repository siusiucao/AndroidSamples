package com.scurab.android.features.di.dagger2.base.util

import com.scurab.android.features.di.dagger2.base.SessionScope

/**
 * Object tied to a session
 */
@SessionScope
data class SessionToken(val id: Int)