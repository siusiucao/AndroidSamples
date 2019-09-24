package com.scurab.android.features.di.dagger2.dynamicfeature

import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.base.util.AppCore
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.base.util.SessionToken
import com.scurab.android.features.di.dagger2.dynamicfeature.di.DynamicFeatureNavigation
import javax.inject.Inject

class DynamicUseCase @Inject constructor(
    private val activity: Reference<AppCompatActivity>,
    private val appCore: AppCore,
    private val sessionToken: SessionToken,
    private val navigation: DynamicFeatureNavigation
) {
    fun doSomething(): String {
        if(appCore.instances != 1) {
            throw IllegalStateException("Unexpected amount of cores:${appCore.instances}")
        }
        return sessionToken.id.toString()
    }

    fun navigateToActivity() {
        navigation.navigateToFeature2Activity()
    }

    fun navigateToFragment() {
        navigation.navigateToFeature2Fragment()
    }
}