package com.scurab.android.features.di.dagger2.app.di

import android.app.Application
import com.scurab.android.features.di.dagger2.base.SessionManager
import com.scurab.android.features.di.dagger2.base.di.AppScope
import com.scurab.android.features.di.dagger2.base.di.DIComponent
import dagger.Component
import dagger.Module
import dagger.Provides

@Module
class AppModule(
    private val app: Application,
    private val sessionManager: SessionManager
) {

    @Provides
    fun provideApp(): Application = app

    @Provides
    fun provideSessionManager(): SessionManager = sessionManager
}

/**
 * Root DI Node, hence it's a [Component]
 * It can provide [SessionManager] and most importantly it can create a DI child-node [SessionComponent]
 */
@AppScope
@Component(modules = [AppModule::class])
interface AppComponent : DIComponent {
    fun sessionComponent(module: SessionModule): SessionComponent
    fun provideSessionManager(): SessionManager
}