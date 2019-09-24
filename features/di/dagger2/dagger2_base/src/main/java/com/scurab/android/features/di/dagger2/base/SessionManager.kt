package com.scurab.android.features.di.dagger2.base

import com.scurab.android.features.di.dagger2.base.util.SessionToken

interface SessionManager {
    fun updateSession(token: SessionToken)
}