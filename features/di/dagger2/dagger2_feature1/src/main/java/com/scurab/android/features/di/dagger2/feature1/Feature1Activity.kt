package com.scurab.android.features.di.dagger2.feature1

import android.os.Bundle
import android.util.Log
import android.view.View
import com.scurab.android.features.di.dagger2.base.BaseActivity
import com.scurab.android.features.di.dagger2.base.DIComponentHolder
import com.scurab.android.features.di.dagger2.feature1.di.Feature1Component
import com.scurab.android.features.di.dagger2.feature1.di.Feature1ComponentProvider
import javax.inject.Inject

private const val TAG = "Feature1Activity"

class Feature1Activity : BaseActivity(), DIComponentHolder<Feature1Component> {

    @Inject lateinit var scopedUseCase: Feature1ScopedUseCase
    @Inject lateinit var useCase: Feature1UseCase
    @Inject lateinit var activityUseCase: ActivityDepenedentUseCase
    @Inject lateinit var navigation: Feature1Navigation

    override val component by dagger(Feature1ComponentProvider::class.java, false) {
        feature1Component()
    }

    override fun inject() {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        View.inflate(this, R.layout.activity_feature1, findViewById(R.id.test_container))

        findViewById<View>(R.id.navigate_to_feature2_activity).setOnClickListener {
            navigation.navigateToFeature2Activity()
        }

        findViewById<View>(R.id.navigate_to_feature2_fragment).setOnClickListener {
            navigation.navigateToFeature2Fragment()
        }

        findViewById<View>(R.id.navigate_to_dynamic_feature_activity).setOnClickListener {
            navigation.navigateToDynamicFeatureActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, useCase.doSomething())
        Log.d(TAG, scopedUseCase.doSomething())

        val oldUseCase = useCase
        val oldScopedUseCase = scopedUseCase
        inject()
        Log.d(TAG, "useCasesEquals:${useCase === oldUseCase} (expected:false) ${component.hashCode()}")
        Log.d(TAG, "scopedUseCasesEquals:${scopedUseCase === oldScopedUseCase} (expected:true)")
        Log.d(TAG, "activityUseCase has activity: ${activityUseCase.hasActivity()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "activityUseCase has activity: ${activityUseCase.hasActivity()}")
    }
}