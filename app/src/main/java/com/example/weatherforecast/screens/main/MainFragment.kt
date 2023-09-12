package com.example.weatherforecast.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.core.view.MenuHost
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentMainBinding
import com.google.android.material.navigation.NavigationView

class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private lateinit var menuHost: MenuHost
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menu: Menu


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        drawerLayout = binding.mainDrawer

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        binding.toolbar.mainToolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        val navigationView = binding.mainNavView
        navigationView.setNavigationItemSelectedListener(this)
        val menu = navigationView.menu


        val list = arrayOf("String", "Double","String", "Double","String", "Double","String", "Double")
        for (i in list.indices) {
            menu.add(Menu.FLAG_APPEND_TO_GROUP, i, Menu.CATEGORY_ALTERNATIVE, list[i]).setIcon(R.drawable.ic_current_location)
        }



    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_location -> {
                TODO("Not implemented method for current location")
            }

            R.id.current_location -> {
                TODO("Not implemented method for current location")
            }
        }
        return true
    }


}


//        val toggle = ActionBarDrawerToggle(
//            this@MainFragment.requireActivity(),
//            drawerLayout,
//            R.string.open_nav,
//            R.string.close_nav
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()