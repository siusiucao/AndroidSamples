package com.example.navigationsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.common.CheckingLoginStateActivity
import com.example.common.appPrefs

class NavigationActivity : AppCompatActivity(), CheckingLoginStateActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.nav_activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.fragment_home, R.id.nav_login_journey))
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)
    }

    override fun requireLoggedIn(): Boolean {
        val isLoggedIn = appPrefs.isLoggedIn
        if (!isLoggedIn) {
            findNavController(R.id.nav_host_fragment).navigate(R.id.nav_login_journey)
        }
        return isLoggedIn
    }
}
