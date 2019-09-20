package com.scurab.android.features.di.dagger2.app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.feature1.Feature1Navigation
import com.scurab.android.features.di.dagger2.feature2.Feature2Activity
import com.scurab.android.features.di.dagger2.feature2.Feature2Fragment
import javax.inject.Inject

class AppNavigation @Inject constructor(private val activityRef: Reference<AppCompatActivity>) : Feature1Navigation {

    override fun navigateToFeature2Activity() = Feature2Activity::class.java.start()

    override fun navigateToFeature2Fragment() = Feature2Fragment().replace()

    private fun Class<out Activity>.start() {
        activityRef.require().apply {
            startActivity(Intent(this, Feature2Activity::class.java))
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