package com.scurab.android.features.di.dagger2.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.dynamicfeature.di.DynamicFeatureNavigation
import com.scurab.android.features.di.dagger2.feature1.Feature1Navigation
import com.scurab.android.features.di.dagger2.feature2.Feature2Activity
import com.scurab.android.features.di.dagger2.feature2.Feature2Fragment
import javax.inject.Inject

class AppNavigation @Inject constructor(private val activityRef: Reference<AppCompatActivity>) :
    Feature1Navigation,
    DynamicFeatureNavigation {

    override fun navigateToFeature2Activity() = start(Feature2Activity::class.java)

    override fun navigateToFeature2Fragment() = Feature2Fragment().replace()

    override fun navigateToDynamicFeatureActivity() =
        //we don't have reference to DynamicFeatureModule, so we can start the activity only this way
        start(Class.forName("com.scurab.android.features.di.dagger2.dynamicfeature.DynamicFeatureActivity"))

    private fun start(clazz: Class<*>) {
        activityRef.require().apply {
            startActivity(Intent(this, clazz))
        }
    }

    private fun Fragment.replace() {
        activityRef.require().supportFragmentManager.apply {
            beginTransaction()
                .replace(R.id.fragment_container, this@replace)
                .commit()
        }
    }
}