package com.scurab.android.features.di.dagger2.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.base.util.WeakMutableReference
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Suppress("PropertyName")
    //updating reference is necessary if we have ActivityScope components tied to ViewModel's lifecycle (survives rotation)
    @Inject lateinit var _self: Reference<AppCompatActivity>

    protected lateinit var testContainer: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        (_self as? WeakMutableReference)?.set(this)
        super.onCreate(savedInstanceState)

        title = this::class.java.simpleName
        setContentView(R.layout.activity_base)
        testContainer = findViewById(R.id.test_container)
    }

    abstract fun inject()

    override fun onDestroy() {
        (_self as? WeakMutableReference)?.set(null)
        super.onDestroy()
    }
}