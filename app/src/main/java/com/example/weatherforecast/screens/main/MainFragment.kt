package com.example.weatherforecast.screens.main

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.text.Layout
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.MenuHost
import androidx.core.view.marginTop
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val ANDROID_STATUS_BAR_SIZE = 24
const val ANDROID_ACTION_BAR = 56
const val SYSTEM_NAVIGATION_BAR_SIZE = 48

@AndroidEntryPoint
class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var menuHost: MenuHost
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menu: Menu
    private val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.main_nav_graph)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hourlyAdapter = HourlyAdapter()
        drawerLayout = binding.mainDrawer

        val displayMetrics = requireContext().resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val density = displayMetrics.density
        val viewHeight =
            screenHeight - ANDROID_STATUS_BAR_SIZE * density.toInt() -
                    ANDROID_ACTION_BAR * density.toInt() - SYSTEM_NAVIGATION_BAR_SIZE * density.toInt() - 220
        binding.mainWeatherWidget.currentWeatherInfoLayout.setPadding(0, viewHeight, 0, 0)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigationView = binding.mainNavView
        navigationView.setNavigationItemSelectedListener(this)
        initHourlyRecycler()

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
                        binding.toolbar.tvCurrentTime.text = it.mainWeatherInfo.currentTime
                        binding.mainWeatherWidget.apply {
                            tvMaxTemp.text =
                                "${it.mainWeatherInfo.maxTemperature}\u00B0C"
                            tvMinTemp.text =
                                "${it.mainWeatherInfo.minTemperature}\u00B0C"
                            tvCurrentTemp.text =
                                "${it.mainWeatherInfo.currentTemperature}\u00B0C"
                            tvWeatherTypeDesc.text =
                                it.mainWeatherInfo.weatherType.weatherType
                            ivWeatherTypeIcon.setImageResource(it.mainWeatherInfo.weatherType.weatherIcon)
                        }

                        if (it.hourlyForecast != null)
                            hourlyAdapter.submitList(it.hourlyForecast)
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

    private fun initHourlyRecycler() {
        binding.widgetForecast.rvHourlyForecast.adapter = hourlyAdapter
        binding.widgetForecast.rvHourlyForecast.layoutManager =
            LinearLayoutManager(this@MainFragment.requireContext(), RecyclerView.HORIZONTAL, false)
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