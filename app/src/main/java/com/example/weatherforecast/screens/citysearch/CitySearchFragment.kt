package com.example.weatherforecast.screens.citysearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentCitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CitySearchFragment : Fragment() {
    private val binding by lazy { FragmentCitySearchBinding.inflate(layoutInflater) }
    private val searchViewModel by hiltNavGraphViewModels<SearchViewModel>(R.id.main_nav_graph)
    private var searchJob: Job? = null
    private val searchDelay: Long = 500

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.searchView.apply {
            isActivated = true
            setIconifiedByDefault(false)
            queryHint = getString(R.string.please_enter_city_name)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.length >= 3) {
                        searchViewModel.search(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    newText?.let { query ->
                        if (query.length >= 3) {
                            delay(searchDelay)
                            searchViewModel.search(query)
                        }
                        if (query.isEmpty()) {
                            searchViewModel.search(query)
                        }
                    }
                }
                return true
            }
        })
    }
}