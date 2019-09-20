package com.scurab.android.features.di.dagger2.base.util

import java.lang.ref.WeakReference

interface Reference<T> {
    fun get(): T?
    fun require(): T {
        return get() ?: throw NullPointerException("Reference is null!")
    }
}

open class WeakMutableReference<T>(ref: T?) : Reference<T> {
    private var reference: WeakReference<T?> = WeakReference(ref)

    override fun get(): T? = reference.get()
    fun set(ref: T?) {
        if (ref !== reference.get()) {
            reference = WeakReference(ref)
        }
    }
}