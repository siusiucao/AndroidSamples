package com.scurab.android.features.di.dagger2.feature2

import android.os.Bundle
import com.scurab.android.features.di.dagger2.base.BaseActivity
import com.scurab.android.features.di.dagger2.base.DIComponentHolder
import com.scurab.android.features.di.dagger2.base.util.AppCore
import com.scurab.android.features.di.dagger2.base.util.SessionToken
import com.scurab.android.features.di.dagger2.feature2.di.Feature2Component
import com.scurab.android.features.di.dagger2.feature2.di.Feature2ComponentProvider
import com.scurab.android.features.di.dagger2.feature2.di.Feature2Module
import javax.inject.Inject

class Feature2Activity : BaseActivity(), DIComponentHolder<Feature2Component> {

    @Inject lateinit var appCore: AppCore
    @Inject lateinit var sessionToken: SessionToken

    override val component by dagger(Feature2ComponentProvider::class.java, false) {
        feature2Component(Feature2Module())
    }

    override fun inject() {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager
            .takeIf { it.findFragmentById(R.id.fragment_container) == null }
            ?.apply {
                beginTransaction()
                    .replace(R.id.fragment_container, Feature2Fragment())
                    .commit()
            }
    }
}