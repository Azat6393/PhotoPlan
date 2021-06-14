package com.azatberdimyradov.photoplan.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.azatberdimyradov.photoplan.R
import com.azatberdimyradov.photoplan.databinding.ActivityMainBinding
import com.azatberdimyradov.photoplan.utils.APP_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APP_ACTIVITY = this
        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
            navController = navHostFragment.findNavController()
            bottomNavView.setupWithNavController(navController)
            bottomNavView.background = null
            bottomNavView.menu.getItem(2).isEnabled = false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}