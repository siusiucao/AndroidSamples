package com.scurab.android.features.di.dagger2.app.di

import com.scurab.android.features.di.dagger2.base.di.DIComponent
import com.scurab.android.features.di.dagger2.base.di.SessionScope
import com.scurab.android.features.di.dagger2.base.util.SessionToken
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class SessionModule(val sessionToken: SessionToken) {
    @Provides
    fun provideSessionToken() = sessionToken
}

/**
 * DI sub node (made by [AppComponent] as it has `fun sessionComponent(module: SessionModule): SessionComponent`)
 *
 * Can provide another level sub-node [InternalActivityComponent]
 */
@SessionScope
@Subcomponent(modules = [SessionModule::class])
interface SessionComponent : DIComponent {
    fun internalActivityComponent(module: InternalActivityModule): InternalActivityComponent
}