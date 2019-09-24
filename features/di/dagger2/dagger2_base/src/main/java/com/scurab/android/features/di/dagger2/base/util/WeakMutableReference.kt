package com.scurab.android.features.di.dagger2.base.util

import java.lang.ref.WeakReference

/**
 * Anyone who depends on the activity can get injected Reference<AppAcompatActivity>
 */
interface Reference<T> {
    fun get(): T?
    fun require(): T {
        return get() ?: throw NullPointerException("Reference is null!")
    }
}

/**
 * Class used for keeping the instance of the Activity.
 * BaseActivity updates the reference if DI node is tied to the ViewModel's lifecycle
 *
 * Dagger Issue:
 * It can't handle a multiple different providing functions with same type but different generic type so having
 * ```
 * //InternalActivityModule.kt
 * fun provideActivity() : Reference<AppCompatActivity>
 *
 * //SomeSpecificFeatureModule.kt
 * fun provideFeatureActivity() : Reference<FeatureActivity>
 * ```
 * will unfortunately fail on dagger side.
 *
 * Also introducing multiple activity reference might become tricky in terms of updating reference when the DI Node
 * is tied to ViewModel's lifecycle. Check [BaseActivity.onCreate] and [BaseActivity.onDestroy]
 */
class WeakMutableReference<T>(ref: T?) : Reference<T> {
    private var reference: WeakReference<T?> = WeakReference(ref)

    override fun get(): T? = reference.get()
    fun set(ref: T?) {
        if (ref !== reference.get()) {
            reference = WeakReference(ref)
        }
    }
}