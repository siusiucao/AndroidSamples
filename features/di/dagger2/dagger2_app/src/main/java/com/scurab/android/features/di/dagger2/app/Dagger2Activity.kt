package com.scurab.android.features.di.dagger2.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.di.dagger2.feature1.Feature1Activity

//just trampoline, this is like app module
class Dagger2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, Feature1Activity::class.java))
        finish()
    }
}