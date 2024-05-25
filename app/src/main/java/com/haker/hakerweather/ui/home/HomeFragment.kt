package com.haker.hakerweather.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.haker.hakerweather.databinding.FragmentHomeBinding
import com.haker.hakerweather.util.Resource
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.data.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { weatherResponse ->

                        binding.progressBar.visibility = View.INVISIBLE
                        resetData()

                        if (weatherResponse.size > 0) {
                            binding.llResult.visibility = View.VISIBLE

                            binding.tvID.text = weatherResponse[0].id.toString()
                            Log.i(TAG, "id = ${weatherResponse[0].id}")
                            binding.tvName.text = weatherResponse[0].name.toString()
                            binding.tvRegion.text = weatherResponse[0].region.toString()
                            binding.tvCountry.text = weatherResponse[0].country.toString()
                            binding.tvLat.text = weatherResponse[0].lat.toString()
                            binding.tvLon.text = weatherResponse[0].lon.toString()
                            binding.tvUrl.text = weatherResponse[0].url.toString()

                            binding.tvNoData.visibility = View.GONE
                        }
                        else {
                            binding.llResult.visibility = View.GONE
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                }
                is Resource.Error -> {
                    it.message?.let {message ->
                        //Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Error : $message")
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.llResult.visibility = View.GONE
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        binding.btnSearch.setOnClickListener {
            viewModel.search(binding.etSearch.text.toString())
        }

        return root
    }

    private fun resetData() {
        binding.tvID.text = ""
        binding.tvName.text = ""
        binding.tvRegion.text = ""
        binding.tvCountry.text = ""
        binding.tvLat.text = ""
        binding.tvLon.text = ""
        binding.tvUrl.text = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}