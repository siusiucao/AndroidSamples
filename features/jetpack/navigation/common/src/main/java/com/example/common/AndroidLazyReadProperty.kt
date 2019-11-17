package com.example.common

import android.view.View
import androidx.annotation.IdRes
import androidx.core.app.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : View> Fragment.lazyView(@IdRes id: Int): ReadOnlyProperty<Any, T> {
    return AndroidLazyReadProperty(
        { viewLifecycleOwner },
        klass = T::class.java,
        init = { requireView().findViewById<T>(id) })
}

inline fun <reified T : View?> Fragment.lazyViewOpt(@IdRes id: Int): ReadOnlyProperty<Any, T> {
    return AndroidLazyReadProperty(
        { viewLifecycleOwner },
        klass = T::class.java,
        init = { requireView().findViewById<T>(id) })
}

inline fun <reified T : Any> Fragment.lazyViewLifecycle(noinline init: () -> T): ReadOnlyProperty<Any, T> {
    return AndroidLazyReadProperty(
        { viewLifecycleOwner },
        klass = T::class.java, init = init
    )
}

inline fun <reified T : Any> ComponentActivity.lazyViewLifecycle(noinline init: () -> T): ReadOnlyProperty<Any, T> {
    return lazyComponent(init)
}

inline fun <reified T : Any> LifecycleOwner.lazyComponent(noinline init: () -> T): ReadOnlyProperty<Any, T> {
    return AndroidLazyReadProperty({ this }, klass = T::class.java, init = init)
}

class AndroidLazyReadProperty<T>(
    //this has to be lambda for Fragment.viewLifecycleOwner
    private val lifecycleOwner: () -> LifecycleOwner,
    private val resetLifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    private val klass: Class<T>,
    private val init: () -> T
) : ReadOnlyProperty<Any, T>, LifecycleObserver {
    private var item: Any? = Unit

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (item == Unit) {
            lifecycleOwner().lifecycle.addObserver(this)
            item = init()
        }
        return item!!.takeIf { klass.isAssignableFrom(it::class.java) } as T
            ?: throw IllegalStateException(
                "Invalid declared type '${thisRef::class.java}.${property.name}', " +
                        "provided view is '${item?.javaClass?.name}', " +
                        "expected type is '${klass.name}'"
            )
    }

    //called via reflection
    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == resetLifecycleEvent) {
            lifecycleOwner().lifecycle.removeObserver(this)
            item = Unit
        }
    }
}