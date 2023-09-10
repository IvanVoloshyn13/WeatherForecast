package com.example.weatherforecast

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initNavigation()


    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host)
        val navController = navHostFragment?.findNavController()
        navController?.navigate(R.id.mainFragment)
    }

}


//addMenuProvider(object : MenuProvider {
//    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        Log.d("TAG", "On create menu")
//        menuInflater.inflate(R.menu.main_drawer_menu, menu)
//    }
//
//    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//        when (menuItem.itemId) {
//            R.id.current_location1 -> {
//                Log.d("TAG", menuItem.itemId.toString())
//            }
//
//            R.id.current_location -> {
//                Log.d("TAG", menuItem.itemId.toString())
//
//            }
//        }
//        return true
//    }
//}, this, Lifecycle.State.RESUMED)