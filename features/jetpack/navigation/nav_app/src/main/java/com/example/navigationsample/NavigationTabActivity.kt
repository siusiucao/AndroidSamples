package com.example.navigationsample

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.common.CheckingLoginStateActivity
import com.example.common.appPrefs

class NavigationTabActivity : AppCompatActivity(), CheckingLoginStateActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_activity_tab)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.tabFragmentLeftStep1, R.id.tabFragmentRightStep1, R.id.helpContainerFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun requireLoggedIn(): Boolean {
        val isLoggedIn = appPrefs.isLoggedIn
        if (!isLoggedIn) {
            findNavController(R.id.nav_host_fragment).navigate(R.id.nav_login_journey)
        }
        return isLoggedIn
    }
}
