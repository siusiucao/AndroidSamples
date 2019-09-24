package com.scurab.android.features.di.dagger2.feature2.di

import com.scurab.android.features.di.dagger2.base.di.ActivityScope
import com.scurab.android.features.di.dagger2.base.di.DIComponent
import com.scurab.android.features.di.dagger2.base.di.DIComponentProvider
import com.scurab.android.features.di.dagger2.feature2.Feature2Activity
import com.scurab.android.features.di.dagger2.feature2.Feature2Fragment
import dagger.Module
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [Feature2Module::class])
interface Feature2Component : DIComponent,
    Feature2Fragment.Injectable {
    fun inject(activity: Feature2Activity)
}

@Module
class Feature2Module(private val feature2Activity: Feature2Activity) {
    //something feature2 specific here
}

interface Feature2ComponentProvider : DIComponentProvider {
    fun feature2Component(module: Feature2Module): Feature2Component
}
