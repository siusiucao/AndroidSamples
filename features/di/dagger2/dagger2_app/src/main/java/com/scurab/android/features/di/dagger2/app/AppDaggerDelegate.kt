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

    private var appComponent: AppComponent? = null
    private var sessionComponent: SessionComponent? = null

    fun requireAppComponent(): AppComponent {
        val appComponent = appComponent ?: DaggerAppComponent
            .builder()
            .appModule(AppModule(app, this))
            .build()
        this.appComponent = appComponent
        return appComponent
    }

    override fun updateSession(token: SessionToken) {
        sessionComponent = requireAppComponent().sessionComponent(SessionModule(token))
    }

    fun createInternalActivityComponent(activity: AppCompatActivity): InternalActivityComponent {
        return (sessionComponent ?: throw IllegalStateException("Session not created yet!"))
            .internalActivityComponent(InternalActivityModule(activity))
    }
}