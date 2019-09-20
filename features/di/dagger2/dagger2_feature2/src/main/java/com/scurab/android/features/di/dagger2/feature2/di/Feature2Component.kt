package com.scurab.android.features.di.dagger2.feature2.di

import com.scurab.android.features.di.dagger2.base.ActivityScope
import com.scurab.android.features.di.dagger2.base.DIComponent
import com.scurab.android.features.di.dagger2.base.DIComponentProvider
import com.scurab.android.features.di.dagger2.base.util.ActivityModule
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.base.util.WeakMutableReference
import com.scurab.android.features.di.dagger2.feature2.Feature2Activity
import com.scurab.android.features.di.dagger2.feature2.Feature2Fragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@ActivityScope
@Subcomponent(modules = [Feature2Module::class])
interface Feature2Component : DIComponent,
    Feature2Fragment.Injectable {
    fun inject(activity: Feature2Activity)
}

@Module
class Feature2Module {
    //something here
}

interface Feature2ComponentProvider : DIComponentProvider {
    fun feature2Component(module: Feature2Module): Feature2Component
}
