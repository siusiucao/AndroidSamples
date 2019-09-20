package com.scurab.android.features.di.dagger2.feature1.di

import com.scurab.android.features.di.dagger2.base.ActivityScope
import com.scurab.android.features.di.dagger2.base.DIComponent
import com.scurab.android.features.di.dagger2.base.DIComponentProvider
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