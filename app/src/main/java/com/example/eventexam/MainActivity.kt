package com.example.eventexam

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.eventexam.databinding.ActivityMainBinding
import com.example.eventexam.ui.favoriteEvent.FavoriteEventFragment
import com.example.eventexam.ui.finishingEvent.FinishingEventFragment
import com.example.eventexam.ui.setting.SettingFragment
import com.example.eventexam.ui.setting.SettingPreferences
import com.example.eventexam.ui.setting.SettingViewModel
import com.example.eventexam.ui.setting.SettingViewModelFactory
import com.example.eventexam.ui.setting.datasore
import com.example.eventexam.ui.upcomingEvent.UpcomingEventFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel : SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val pref = SettingPreferences.getInstance(this.application.datasore)
        mainViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref)).get(SettingViewModel::class.java)

        mainViewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_upcoming,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

}