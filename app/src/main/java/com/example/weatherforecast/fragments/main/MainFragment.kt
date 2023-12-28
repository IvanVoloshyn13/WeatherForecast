package com.example.weatherforecast.fragments.main

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.fragment.app.setFragmentResultListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.domain.models.mainscreen.weather.DailyForecast
import com.example.domain.models.mainscreen.weather.HourlyForecast
import com.example.domain.models.searchscreen.SearchedCity
import com.example.weatherforecast.R
import com.example.weatherforecast.connectivity.GpsStatus
import com.example.weatherforecast.connectivity.NetworkStatus
import com.example.weatherforecast.connectivity.UpdateConnectivityStatus
import com.example.weatherforecast.databinding.FragmentMainBinding
import com.example.weatherforecast.fragments.main.models.GetWeather
import com.example.weatherforecast.fragments.main.models.GetWeatherByCurrentLocation
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
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

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        drawerLayout = binding.mainDrawer

        setFragmentResultListener("city") { _, bundle ->
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("bundle_key", SearchedCity::class.java)
            } else {
                bundle.getParcelable("bundle_key")
            }
            if (result != null) {
                viewModel.setEvent(GetWeather(result))
            }
        }

        /**The view height code didnt work correctly on
         * some devices with bottom system bar etc.
         * TODO() refactor view height using insets
         */

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
//        val list = arrayOf("Lviv", "Warsaw","Krakow")
//        val menu = navigationView.menu
//        for (index in list.indices) {
//            menu.add(Menu.CATEGORY_ALTERNATIVE, index, Menu.CATEGORY_ALTERNATIVE, list[index]).setIcon(R.drawable.ic_current_location)
//        }
    }

    override fun onStart() {
        super.onStart()
        observeConnectivityStatus()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mainScreenState.collectLatest { state ->
                with(binding) {
                    with(state) {
                        with(mainWeatherWidget) {
                            tvCurrentTemp.text =
                                mainWeatherInfo.currentTemperature.toString()
                            tvMaxTemp.text =
                                mainWeatherInfo.maxTemperature.toString()
                            tvMinTemp.text =
                                mainWeatherInfo.minTemperature.toString()
                            tvWeatherTypeDesc.text =
                                mainWeatherInfo.weatherType.weatherType
                            ivWeatherTypeIcon.setImageResource(mainWeatherInfo.weatherType.weatherIcon)
                        }
                        with(toolbar) {
                            tvCurrentTime.text = time
                            tvToolbarTitle.text = location
                        }
                        if (currentWeatherLocationImage.isNotEmpty()) {
                            ivCityImage.load(currentWeatherLocationImage)
                        } else {
                            ivCityImage.load(R.drawable.cloud_blue_sky)
                        }
                    }
                }
                dailyAdapter.submitList(state.dailyForecast as List<DailyForecast>)
                hourlyAdapter.submitList(state.hourlyForecast as List<HourlyForecast>)
            }
        }


        binding.toolbar.bttAddNewCity.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_citySearchFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.toolbar.mainToolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun observeConnectivityStatus() {
        if (activity != null && activity is UpdateConnectivityStatus) {
            val fragmentActivity = activity as UpdateConnectivityStatus
            val networkJob = viewLifecycleOwner.lifecycleScope.launch(start = CoroutineStart.LAZY) {
                fragmentActivity.networkStatus.collectLatest {
                    observeNetworkStatus(it)
                }
            }
            val gpsJob = viewLifecycleOwner.lifecycleScope.launch {
                fragmentActivity.gpsStatus.collectLatest {
                    observeGpsStatus(it)
                }
            }
            if (requireActivity().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                networkJob.start()
                gpsJob.start()
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
                viewModel.setEvent(GetWeatherByCurrentLocation)
            }
        }
    }

    private fun observeNetworkStatus(network: NetworkStatus) {
        if (isResumed) {
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

                NetworkStatus.Unavailable -> {
                    Toast.makeText(
                        this@MainFragment.requireContext(),
                        R.string.no_network,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_location -> {
                TODO("Not implemented method for current location")
            }

            R.id.current_location -> {
                viewModel.setEvent(GetWeatherByCurrentLocation)
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
            LinearLayoutManager(
                this@MainFragment.requireContext(),
                RecyclerView.HORIZONTAL, false
            )
    }

    private fun initDailyRecycler() {
        binding.widgetForecast.rvDaily.adapter = dailyAdapter
        binding.widgetForecast.rvDaily.layoutManager =
            LinearLayoutManager(
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