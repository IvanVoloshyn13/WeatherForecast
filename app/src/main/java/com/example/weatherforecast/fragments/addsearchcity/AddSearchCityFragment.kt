package com.example.weatherforecast.fragments.addsearchcity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.models.SearchedCity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentCitySearchBinding
import com.example.weatherforecast.fragments.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddSearchCityFragment : Fragment(), SearchedCitiesAdapter.RecyclerViewOnItemClick {
    private val binding by viewBinding<FragmentCitySearchBinding>()
    private val searchViewModel by hiltNavGraphViewModels<SearchViewModel>(R.id.main_nav_graph)
    private var searchJob: Job? = null
    private val searchDelay: Long = 500
    private lateinit var searchedAdapter: SearchedCitiesAdapter
    private lateinit var onBackPressed: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressed = object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressed)
    }

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
        searchedAdapter =
            SearchedCitiesAdapter(this as SearchedCitiesAdapter.RecyclerViewOnItemClick)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = binding.rvCity
        recycler.adapter = searchedAdapter
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
        lifecycleScope.launch {
            searchViewModel.cities.collectLatest {
                searchedAdapter.submitList1(it)
            }
        }


    }

    private fun onBackPressed() {
        searchViewModel.search("")
        findNavController().popBackStack()
    }

    override fun onItemClick(city: SearchedCity) {
        searchViewModel.saveCity(city)
        setFragmentResult("cityId", bundleOf("bundle_key" to city.id))
        onBackPressed()
    }


}