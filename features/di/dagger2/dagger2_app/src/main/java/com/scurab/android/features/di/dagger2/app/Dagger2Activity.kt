package com.scurab.android.features.di.dagger2.app

import android.content.Intent
import android.os.Bundle
import com.scurab.android.features.di.dagger2.base.BaseActivity
import com.scurab.android.features.di.dagger2.feature1.Feature1Activity

class Dagger2Activity : BaseActivity() {

    override fun inject() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, Feature1Activity::class.java))
        finish()
    }
}