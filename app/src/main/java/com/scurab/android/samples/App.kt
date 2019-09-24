package com.scurab.android.samples

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.app.AppDaggerDelegate
import com.scurab.android.features.di.dagger2.base.di.DIComponent
import com.scurab.android.features.di.dagger2.base.di.InternalActivityComponentProvider
import com.scurab.android.features.di.dagger2.base.util.SessionToken

class App : Application(), InternalActivityComponentProvider {

    private val daggerDelegate = AppDaggerDelegate(this)

    override fun onCreate() {
        super.onCreate()

        daggerDelegate.requireAppComponent().provideSessionManager().apply {
            updateSession(SessionToken(-1))
        }
    }

    override fun internalActivityComponent(activity: AppCompatActivity): DIComponent {
        return daggerDelegate.createInternalActivityComponent(activity)
    }
}