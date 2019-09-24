package com.scurab.android.features.di.dagger2.feature1.di

import com.scurab.android.features.di.dagger2.base.di.ActivityScope
import com.scurab.android.features.di.dagger2.base.di.DIComponent
import com.scurab.android.features.di.dagger2.base.di.DIComponentProvider
import com.scurab.android.features.di.dagger2.feature1.Feature1Activity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface Feature1Component : DIComponent {
    fun inject(activity: Feature1Activity)
}

interface Feature1ComponentProvider : DIComponentProvider {
    fun feature1Component(): Feature1Component
}