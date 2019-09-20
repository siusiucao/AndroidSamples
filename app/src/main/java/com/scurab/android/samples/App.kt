package com.scurab.android.samples

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.app.AppDIDelegate
import com.scurab.android.features.di.dagger2.base.DIComponent
import com.scurab.android.features.di.dagger2.base.InternalActivityComponentProvider
import com.scurab.android.features.di.dagger2.base.util.SessionToken

class App : Application(), InternalActivityComponentProvider {

    private val daggerDelegate = AppDIDelegate()

    override fun onCreate() {
        super.onCreate()
        daggerDelegate.resetSession(this, SessionToken(-1))
    }

    override fun internalActivityComponent(activity: AppCompatActivity): DIComponent {
        return daggerDelegate.createInternalActivityComponent(activity)
    }
}