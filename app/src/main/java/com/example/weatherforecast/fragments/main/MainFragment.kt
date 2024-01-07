package com.example.weatherforecast.fragments.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
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
import com.example.domain.models.mainscreen.weather.MainWeatherInfo
import com.example.domain.models.searchscreen.SearchedCity
import com.example.weatherforecast.R
import com.example.weatherforecast.connectivity.GpsStatus
import com.example.weatherforecast.connectivity.NetworkStatus
import com.example.weatherforecast.connectivity.UpdateConnectivityStatus
import com.example.weatherforecast.databinding.FragmentMainBinding
import com.example.weatherforecast.databinding.HeaderLayoutBinding
import com.example.weatherforecast.fragments.main.models.GetSavedLocationsList
import com.example.weatherforecast.fragments.main.models.GetWeather
import com.example.weatherforecast.fragments.main.models.GetWeatherByCurrentLocation
import com.example.weatherforecast.fragments.main.models.MainScreenState
import com.example.weatherforecast.fragments.main.models.ShowLess
import com.example.weatherforecast.fragments.main.models.ShowMore
import com.example.weatherforecast.fragments.main.models.ShowMoreLess
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val ANDROID_STATUS_BAR_SIZE = 24
const val ANDROID_ACTION_BAR = 56
const val SYSTEM_NAVIGATION_BAR_SIZE = 48

@AndroidEntryPoint
class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener,
    OnCityClickListener {

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private lateinit var headerBinding: HeaderLayoutBinding
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var savedLocationAdapter: SavedLocationAdapter
    private lateinit var drawerLayout: DrawerLayout

    private val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.main_nav_graph)


    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        savedLocationAdapter = SavedLocationAdapter(this)
        drawerLayout = binding.mainDrawer
        val header = binding.mainNavView.getHeaderView(0)
        headerBinding = HeaderLayoutBinding.bind(header)

        setFragmentResultListener("city") { _, bundle ->
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("bundle_key", SearchedCity::class.java)
            } else {
                bundle.getParcelable("bundle_key")
            }
            if (result != null) {
                viewModel.setEvent(GetWeather(result))
                viewModel.setEvent(GetSavedLocationsList)
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
        initSavedLocationsAdapter()
    }

    override fun onStart() {
        super.onStart()
        observeConnectivityStatus()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mainScreenState.collectLatest { state ->
                updateMainWeatherWidget(state.mainWeatherInfo)
                updateWidgetForecast(state)
                updateShowMoreLessLocationState(state = state.showMoreLess)
                binding.apply {
                    with(toolbar) {
                        tvCurrentTime.text = state.time
                        tvToolbarTitle.text = state.location
                    }
                    if (state.currentWeatherLocationImage.isNotEmpty()) {
                        ivCityImage.load(state.currentWeatherLocationImage)
                    } else {
                        ivCityImage.load(R.drawable.cloud_blue_sky)
                    }

                }
                savedLocationAdapter.submitList(state.cities)
            }
        }
    }

    private fun updateShowMoreLessLocationState(state: ShowMoreLess) {
        when (state) {
            is ShowMoreLess.ShowMore -> {
                headerBinding.bttShowLess.visibility = View.GONE
                headerBinding.bttShowMore.visibility = View.VISIBLE
            }

            is ShowMoreLess.ShowLess -> {
                headerBinding.bttShowLess.visibility = View.VISIBLE
                headerBinding.bttShowMore.visibility = View.GONE
            }

            is ShowMoreLess.Hide -> {
                headerBinding.bttShowLess.visibility = View.GONE
                headerBinding.bttShowMore.visibility = View.GONE
            }
        }
    }

    private fun updateWidgetForecast(state: MainScreenState) {
        dailyAdapter.submitList(state.dailyForecast as List<DailyForecast>)
        hourlyAdapter.submitList(state.hourlyForecast as List<HourlyForecast>)
    }

    private fun updateMainWeatherWidget(state: MainWeatherInfo) {
        binding.mainWeatherWidget.apply {
            tvCurrentTemp.text =
                state.currentTemperature.toString()
            tvMaxTemp.text =
                state.maxTemperature.toString()
            tvMinTemp.text =
                state.minTemperature.toString()
            tvWeatherTypeDesc.text =
                state.weatherType.weatherType
            ivWeatherTypeIcon.setImageResource(state.weatherType.weatherIcon)
        }

    }

    override fun onResume() {
        super.onResume()

        binding.toolbar.bttAddNewCity.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_citySearchFragment)
        }

        binding.toolbar.bttAddNewCity.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_mainFragment_to_citySearchFragment)
            }
        })

        binding.toolbar.mainToolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        headerBinding.currentLocation.cityLayout.setOnClickListener {
            viewModel.setEvent(GetWeatherByCurrentLocation)
            drawerLayout.close()
        }

        headerBinding.bttShowMore.setOnClickListener {
            viewModel.setEvent(ShowMore)
        }

        headerBinding.bttShowLess.setOnClickListener {
            viewModel.setEvent(ShowLess)
        }


    }

    private fun observeConnectivityStatus() {
        if (activity != null && activity is UpdateConnectivityStatus) {
            val fragmentActivity = activity as UpdateConnectivityStatus
            val networkJob =
                viewLifecycleOwner.lifecycleScope.launch(start = CoroutineStart.LAZY) {
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

    private fun initSavedLocationsAdapter() {
        val rv = headerBinding.rvCities
        rv.adapter = savedLocationAdapter
    }

    override fun onClick(city: SearchedCity) {
        drawerLayout.close()
        viewModel.setEvent(GetWeather(city))
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