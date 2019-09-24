package com.scurab.android.features.di.dagger2.dynamicfeature.di

import com.scurab.android.features.di.dagger2.base.ActivityScope
import com.scurab.android.features.di.dagger2.base.DIComponent
import com.scurab.android.features.di.dagger2.dynamicfeature.DynamicFeatureActivity
import dagger.Component

//can't be subcomponent, because app doesn't have a reference to this project module
@ActivityScope
@Component(dependencies = [DynamicFeatureComponentDependencies::class])
interface DynamicFeatureComponent : DIComponent {
    fun inject(activity: DynamicFeatureActivity)
}