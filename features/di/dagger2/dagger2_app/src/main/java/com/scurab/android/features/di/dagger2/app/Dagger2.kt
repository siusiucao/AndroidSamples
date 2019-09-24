package com.scurab.android.features.di.dagger2.app

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.base.*
import com.scurab.android.features.di.dagger2.base.util.ActivityModule
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.base.util.SessionToken
import com.scurab.android.features.di.dagger2.base.util.WeakMutableReference
import com.scurab.android.features.di.dagger2.dynamicfeature.di.DynamicFeatureComponentDependencies
import com.scurab.android.features.di.dagger2.dynamicfeature.di.DynamicFeatureNavigation
import com.scurab.android.features.di.dagger2.feature1.Feature1Navigation
import com.scurab.android.features.di.dagger2.feature1.di.Feature1ComponentProvider
import com.scurab.android.features.di.dagger2.feature2.Feature2Fragment
import com.scurab.android.features.di.dagger2.feature2.di.Feature2ComponentProvider
import dagger.*

@Module
class AppModule(val app: Application) {

    @Provides
    fun provideApp(): Application = app
}

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent : DIComponent {
    fun sessionComponent(module: SessionModule): SessionComponent

}

@Module
class SessionModule(val sessionToken: SessionToken) {
    @Provides
    fun provideSessionToken() = sessionToken
}

@SessionScope
@Subcomponent(modules = [SessionModule::class])
interface SessionComponent : DIComponent {
    fun internalActivityComponent(module: InternalActivityModule): InternalActivityComponent
}

@Module
class InternalActivityModule(activity: AppCompatActivity) {
    private val activityReference = WeakMutableReference(activity)

    //generics like this can't be reused for more specific instances,
    //dagger2 won't handle it
    @Provides
    fun provideActivity(): Reference<AppCompatActivity> {
        return activityReference
    }
}

@InternalActivityScope
@Subcomponent(modules = [InternalActivityModule::class, BindingModule::class])
interface InternalActivityComponent : DIComponent,
    CommonComponentProvider,
    Feature1ComponentProvider,
    Feature2ComponentProvider,
    //F2F is started in F1Activity and F2Activity, hence it can't depend on anything specific from each particular feature module
    //and for case injecting from F1Activity, it has to be here, because F1Component doesn't know about it
    Feature2Fragment.Injectable,
    DynamicFeatureComponentDependencies

@Module
abstract class BindingModule {
    @Binds
    abstract fun feature1Navigation(app: AppNavigation): Feature1Navigation

    @Binds
    abstract fun dynamicFeatureNavigation(app: AppNavigation): DynamicFeatureNavigation
}