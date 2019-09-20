package com.scurab.android.features.di.dagger2.base.util

import android.app.Activity

interface ActivityModule<T : Activity> {

    //looks like this can't be abstract class with predefined signature
    //dagger would fail
    fun provideActivity(): Reference<T>
}