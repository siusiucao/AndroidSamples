package com.scurab.android.features.di.dagger2.feature2

import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.base.util.SessionToken
import javax.inject.Inject

class Feature2UseCase @Inject constructor(
    private val activity: Reference<AppCompatActivity>,
    private val sessionToken: SessionToken
) {
    fun doSomething(): String {
        return "Feature2UseCase\ndoSomething\n" +
                "hasActivity:${activity.get() != null}\n" +
                "sessionToken:$sessionToken"
    }
}