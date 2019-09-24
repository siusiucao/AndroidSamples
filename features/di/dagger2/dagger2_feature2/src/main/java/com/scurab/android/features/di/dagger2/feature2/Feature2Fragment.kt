package com.scurab.android.features.di.dagger2.feature2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.scurab.android.features.di.dagger2.base.di.AndroidInjector
import com.scurab.android.features.di.dagger2.base.di.DIComponent
import javax.inject.Inject

class Feature2Fragment : Fragment() {

    @Inject lateinit var feature2UseCase: Feature2UseCase

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidInjector
            .component(this, Feature2Fragment.Injectable::class.java)
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feature2, container, false)
    }

    override fun onResume() {
        super.onResume()
        Log.d("Feature2Fragment", feature2UseCase.doSomething())
    }

    /**
     * Used in multiple places so we have to have the [inject] here and any Component used for injecting this instance by
     * must extend it
     */
    interface Injectable : DIComponent {
        fun inject(fragment: Feature2Fragment)
    }
}