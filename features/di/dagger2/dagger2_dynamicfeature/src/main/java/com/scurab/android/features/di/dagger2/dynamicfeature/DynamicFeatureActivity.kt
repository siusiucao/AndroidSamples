package com.scurab.android.features.di.dagger2.dynamicfeature

import android.os.Bundle
import android.util.Log
import android.view.View
import com.scurab.android.features.di.dagger2.base.AndroidInjector
import com.scurab.android.features.di.dagger2.base.BaseActivity
import com.scurab.android.features.di.dagger2.base.DIComponentHolder
import com.scurab.android.features.di.dagger2.base.util.AppCore
import com.scurab.android.features.di.dagger2.dynamicfeature.di.DaggerDynamicFeatureComponent
import com.scurab.android.features.di.dagger2.dynamicfeature.di.DynamicFeatureComponent
import javax.inject.Inject

class DynamicFeatureActivity : BaseActivity(), DIComponentHolder<DynamicFeatureComponent> {

    @Inject lateinit var appCore: AppCore
    @Inject lateinit var useCase: DynamicUseCase

    override val component: DynamicFeatureComponent by lazy {
        DaggerDynamicFeatureComponent
            .builder()
            .dynamicFeatureComponentDependencies(AndroidInjector.dependencies(this))
            .build()
    }

    override fun inject() {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        View.inflate(this, R.layout.activity_dynamic, testContainer)

        findViewById<View>(R.id.navigate_to_feature2_activity).setOnClickListener {
            useCase.navigateToActivity()
        }

        findViewById<View>(R.id.navigate_to_feature2_fragment).setOnClickListener {
            useCase.navigateToFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("DynamicFeatureActivity", useCase.doSomething())
    }
}