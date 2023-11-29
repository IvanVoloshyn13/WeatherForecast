package com.example.weatherforecast.screens.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.connectivity.GpsStatus
import com.example.weatherforecast.connectivity.NetworkStatus
import com.example.weatherforecast.connectivity.UpdateConnectivityStatus
import com.example.weatherforecast.databinding.FragmentMainBinding
import com.example.weatherforecast.screens.main.models.MainScreenEvents
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val ANDROID_STATUS_BAR_SIZE = 24
const val ANDROID_ACTION_BAR = 56
const val SYSTEM_NAVIGATION_BAR_SIZE = 48

@AndroidEntryPoint
class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter
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
        dailyAdapter = DailyAdapter()
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
        initDailyRecycler()

        observeConnectivityStatus()

//        val list = arrayOf("Lviv", "Warsaw","Krakow")
//        val menu = navigationView.menu
//
//        for (index in list.indices) {
//            menu.add(Menu.CATEGORY_ALTERNATIVE, index, Menu.CATEGORY_ALTERNATIVE, list[index]).setIcon(R.drawable.ic_current_location)
//        }


    }

    override fun onStart() {
        super.onStart()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mainScreenState.collectLatest { state ->

                state?.let {
                    binding.toolbar.tvToolbarTitle.text = it.location
                    binding.toolbar.tvCurrentTime.text = it.time
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

                    if (it.dailyForecast != null) {
                        dailyAdapter.submitList(it.dailyForecast)
                    }

                }
            }
        }
    }

    private fun observeConnectivityStatus() {
      if (activity != null && activity is UpdateConnectivityStatus ) {
         val fragmentActivity = activity as UpdateConnectivityStatus
            viewLifecycleOwner.lifecycleScope.launch {
                fragmentActivity.networkStatus.collectLatest {
                    observeGpsStatus(it.gpsStatus)
                    observeNetworkStatus(it.networkStatus)
                    if(it.gpsStatus == GpsStatus.Available){
                        viewModel.setEvent(MainScreenEvents.GetWeatherByCurrentLocation)
                    }
                }
            }
        }
    }

    private fun observeGpsStatus(status: GpsStatus) {
        when (status) {
            GpsStatus.Unavailable -> {
                binding.noGpsDialog.noGpsDialog.visibility = View.VISIBLE
                binding.mainGroup.visibility = View.GONE
            }
            GpsStatus.Available -> {
                binding.noGpsDialog.noGpsDialog.visibility = View.GONE
                binding.mainGroup.visibility = View.VISIBLE

            }
        }
    }


    private fun observeNetworkStatus(network: NetworkStatus?) {
        if(isResumed) {
            if (network != null) {
                when (network) {
                    NetworkStatus.Available -> {

                    }

                    NetworkStatus.Lost -> {
                        Toast.makeText(
                            this@MainFragment.requireContext(),
                            R.string.no_network,
                            Toast.LENGTH_SHORT
                        ).show()
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

    override fun onPause() {
        super.onPause()
        viewModel.stopTimeObserve()

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_location -> {
                TODO("Not implemented method for current location")
            }

            R.id.current_location -> {
                viewModel.setEvent(MainScreenEvents.GetWeatherByCurrentLocation)
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

    private fun initDailyRecycler() {
        binding.widgetForecast.rvDaily.adapter = dailyAdapter
        binding.widgetForecast.rvDaily.layoutManager = LinearLayoutManager(
            this@MainFragment.requireContext(),
            RecyclerView.VERTICAL, false
        )

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