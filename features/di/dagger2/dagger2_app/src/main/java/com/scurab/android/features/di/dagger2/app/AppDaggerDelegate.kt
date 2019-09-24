package com.scurab.android.features.di.dagger2.app

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.app.di.*
import com.scurab.android.features.di.dagger2.base.SessionManager
import com.scurab.android.features.di.dagger2.base.util.SessionToken

/**
 * Help class for DI handling and an example how to do a session management in terms of DI
 */
class AppDaggerDelegate(private val app: Application) : SessionManager {

    private var _appComponent: AppComponent? = null
    private var _sessionComponent: SessionComponent? = null

    fun requireAppComponent(): AppComponent {
        val appComponent = _appComponent ?: DaggerAppComponent
            .builder()
            .appModule(AppModule(app, this))
            .build()
        _appComponent = appComponent
        return appComponent
    }

    override fun updateSession(token: SessionToken) {
        _sessionComponent = requireAppComponent().sessionComponent(SessionModule(token))
    }

    fun createInternalActivityComponent(activity: AppCompatActivity): InternalActivityComponent {
        return (_sessionComponent ?: throw IllegalStateException("Session not created yet!"))
            .internalActivityComponent(InternalActivityModule(activity))
    }
}