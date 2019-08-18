package com.scurab.android.samples

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.security.keystore.KeystoreSampleActivity
import com.scurab.android.features.ui.themes.app.ThemesSampleActivity

class AppActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, KeystoreSampleActivity::class.java))
    }
}