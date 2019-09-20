package com.scurab.android.features.di.dagger2.feature1

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.base.ActivityScope
import com.scurab.android.features.di.dagger2.base.util.AppCore
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.base.util.SessionToken
import javax.inject.Inject

class Feature1UseCase @Inject constructor(
    private val appCore: AppCore,
    private val sessionToken: SessionToken,
    private val navigation: Feature1Navigation
) {
    fun doSomething(): String = "Feature1UseCase"
}


@ActivityScope
class Feature1ScopedUseCase @Inject constructor(private val feature1UseCase: Feature1UseCase) {
    fun doSomething(): String = feature1UseCase.doSomething() + System.currentTimeMillis()
}


@ActivityScope
class ActivityDepenedentUseCase @Inject constructor(val activityRef: Reference<AppCompatActivity>) {

    fun hasActivity(): Boolean = activityRef.get() != null
}