package com.scurab.android.features.di.dagger2.app

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.base.util.SessionToken

class AppDIDelegate {

    private var _appComponent: AppComponent? = null
    private var _sessionComponent: SessionComponent? = null

    private fun requireAppComponent(app: Application): AppComponent {
        val appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(app))
            .build()
        _appComponent = appComponent
        return appComponent
    }

    fun resetSession(app: Application, token: SessionToken) {
        _sessionComponent = requireAppComponent(app).sessionComponent(SessionModule(token))
    }

    fun createInternalActivityComponent(activity: AppCompatActivity): InternalActivityComponent {
        return (_sessionComponent ?: throw IllegalStateException("Session not created yet!"))
            .internalActivityComponent(InternalActivityModule(activity))
    }
}