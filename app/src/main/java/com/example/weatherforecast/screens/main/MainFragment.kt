package com.example.weatherforecast.screens.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.MenuHost
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private lateinit var menuHost: MenuHost
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menu: Menu
    private val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.main_nav_graph)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        drawerLayout = binding.mainDrawer
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigationView = binding.mainNavView
        navigationView.setNavigationItemSelectedListener(this)

//        val list = arrayOf("Lviv", "Warsaw","Krakow")
//        val menu = navigationView.menu
//
//        for (index in list.indices) {
//            menu.add(Menu.CATEGORY_ALTERNATIVE, index, Menu.CATEGORY_ALTERNATIVE, list[index]).setIcon(R.drawable.ic_current_location)
//        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.mainScreenState.collectLatest { state ->
                    state?.let {
                        binding.toolbar.tvToolbarTitle.text = it.city
                        binding.mainWeatherWidget.tvMaxTemp.text =
                            "${it.mainWeatherInfo.maxTemperature}\u00B0C"
                        binding.mainWeatherWidget.tvMinTemp.text =
                            "${it.mainWeatherInfo.minTemperature}\u00B0C"
                        binding.mainWeatherWidget.tvCurrentTemp.text =
                            "${it.mainWeatherInfo.currentTemperature}\u00B0C"
                        binding.mainWeatherWidget.tvWeatherTypeDesc.text =
                            it.mainWeatherInfo.weatherType.weatherType
                        binding.mainWeatherWidget.ivWeatherTypeIcon.setImageResource(it.mainWeatherInfo.weatherType.weatherIcon)
                    }

                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        binding.toolbar.mainToolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        Log.d("VIEW_MODEL", viewModel.hashCode().toString())


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_location -> {
                TODO("Not implemented method for current location")
            }

            R.id.current_location -> {
                viewModel.getWeatherByCurrentUserLocation()
                drawerLayout.close()
            }

            else -> {
                drawerLayout.close()
                Toast.makeText(
                    this@MainFragment.requireContext(),
                    "${item}",
                    Toast.LENGTH_SHORT
                ).show()

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