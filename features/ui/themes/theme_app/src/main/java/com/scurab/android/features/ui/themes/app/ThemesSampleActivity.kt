package com.scurab.android.features.ui.themes.app

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ThemesSampleActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "ThemesSampleActivity", Toast.LENGTH_LONG).show()
    }
}