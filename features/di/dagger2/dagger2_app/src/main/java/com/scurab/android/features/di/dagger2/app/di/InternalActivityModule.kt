package com.scurab.android.features.di.dagger2.app.di

import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.app.AppNavigation
import com.scurab.android.features.di.dagger2.base.di.CommonComponentProvider
import com.scurab.android.features.di.dagger2.base.di.DIComponent
import com.scurab.android.features.di.dagger2.base.di.InternalActivityScope
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.base.util.WeakMutableReference
import com.scurab.android.features.di.dagger2.dynamicfeature.di.DynamicFeatureComponentDependencies
import com.scurab.android.features.di.dagger2.dynamicfeature.di.DynamicFeatureNavigation
import com.scurab.android.features.di.dagger2.feature1.Feature1Navigation
import com.scurab.android.features.di.dagger2.feature1.di.Feature1ComponentProvider
import com.scurab.android.features.di.dagger2.feature2.Feature2Fragment
import com.scurab.android.features.di.dagger2.feature2.di.Feature2ComponentProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

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

/**
 * DI sub-node. As this is provided via [SessionComponent].
 * This nodes can provide anything what [SessionComponent] and [AppComponent] can.
 * Also as it uses [InternalActivityModule] anything what is using this as a parent DI node can provide also reference
 * to an [AppCompatActivity]
 *
 * This is the interface where you should be adding all the ComponentProviders, Dependencies or Injectables
 */
@InternalActivityScope
@Subcomponent(modules = [InternalActivityModule::class, BindingModule::class])
interface InternalActivityComponent : DIComponent,
    //providers basically tell to dagger, this will create a DI sub-node (subcomponent)
    CommonComponentProvider,
    Feature1ComponentProvider,
    Feature2ComponentProvider,
    //F2F is started in F1Activity and F2Activity, hence it can't depend on anything specific from each particular feature module
    //and for case injecting from F1Activity, it has to be here, because F1Component doesn't know about it
    Feature2Fragment.Injectable,
    //DynamicFeatureModule depends on the app, so we can't get a reference of the DynamicFeatureComponent
    //so we have to define a [DynamicFeatureComponentDependencies] in a bridge module which can be used in the [:app, :dagger2_dynamicfeature]
    //all scope defs are presereved
    DynamicFeatureComponentDependencies

/**
 * Just a binding between ifaces and actual instances
 */
@Module
abstract class BindingModule {
    @Binds
    abstract fun feature1Navigation(app: AppNavigation): Feature1Navigation

    @Binds
    abstract fun dynamicFeatureNavigation(app: AppNavigation): DynamicFeatureNavigation
}