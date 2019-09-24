package com.scurab.android.features.di.dagger2.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.scurab.android.features.di.dagger2.base.util.Reference
import com.scurab.android.features.di.dagger2.base.util.WeakMutableReference
import javax.inject.Inject
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class BaseActivity : AppCompatActivity() {

    @Suppress("PropertyName")
    @Inject lateinit var _self: Reference<AppCompatActivity>

    protected lateinit var testContainer: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        if (::_self.isInitialized) {
            (_self as? WeakMutableReference)?.set(this)
        }
        super.onCreate(savedInstanceState)

        title = this::class.java.simpleName
        setContentView(R.layout.activity_base)
        testContainer = findViewById(R.id.test_container)
    }

    abstract fun inject()

    override fun onDestroy() {
        if (::_self.isInitialized) {
            (_self as? WeakMutableReference)?.set(null)
        }
        super.onDestroy()
    }


    protected fun <T : DIComponentProvider, R : DIComponent> dagger(
        clazz: Class<out T>,
        survivingScope: Boolean = false,
        block: T.() -> R
    ): ReadOnlyProperty<Any, R> {
        return object : ReadOnlyProperty<Any, R> {
            private val component: R by lazy {
                val result = if (survivingScope) {
                    val node = ViewModelProviders.of(this@BaseActivity).get(DINode::class.java)
                    if (node.component == null) {
                        node.component = block(AndroidInjector.componentProvider(this@BaseActivity, clazz))
                    }
                    node.component
                } else {
                    block(AndroidInjector.componentProvider(this@BaseActivity, clazz))
                }
                result as R
            }

            override fun getValue(thisRef: Any, property: KProperty<*>): R {
                return component
            }
        }
    }

    protected fun <T : DIComponentProvider, R : DIComponent> dynamicDagger(
        clazz: Class<out T>,
        survivingScope: Boolean = false,
        block: T.() -> R
    ): ReadOnlyProperty<Any, R> {
        return object : ReadOnlyProperty<Any, R> {
            private val component: R by lazy {
                val result = if (survivingScope) {
                    val node = ViewModelProviders.of(this@BaseActivity).get(DINode::class.java)
                    if (node.component == null) {
                        node.component = block(AndroidInjector.componentProvider(this@BaseActivity, clazz))
                    }
                    node.component
                } else {
                    block(AndroidInjector.componentProvider(this@BaseActivity, clazz))
                }
                result as R
            }

            override fun getValue(thisRef: Any, property: KProperty<*>): R {
                return component
            }
        }
    }
}

internal class DINode : ViewModel() {
    var component: DIComponent? = null
}