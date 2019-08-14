package com.scurab.android.features.ui.themes.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.features.ui.theme.app.R

private const val ARG_THEME_INDEX = "ARG_THEME_INDEX"

class ThemesSampleActivity : AppCompatActivity() {

    private var themeIndex = 0
    private val themes = intArrayOf(R.style.UITheme_Day, R.style.UITheme_Night)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeIndex = intent.getIntExtra(ARG_THEME_INDEX, 0)
        reload(themeIndex)
    }

    private fun reload(themeIndex: Int) {
        setTheme(themes[themeIndex])
        this.themeIndex = themeIndex
        setContentView(R.layout.themes_sample)
        findViewById<View>(R.id.change_theme).setOnClickListener {
            startWithThemeIndex(this, (themeIndex + 1) % themes.size)
            finish()
        }
    }

    companion object {
        fun startWithThemeIndex(context: Context, themeIndex: Int = 0) {
            Intent(context, ThemesSampleActivity::class.java).run {
                putExtra(ARG_THEME_INDEX, themeIndex)
                context.startActivity(this)
            }
        }
    }
}