package com.haker.hakerweather.ui.sports

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.haker.hakerweather.adapter.SportsAdapter
import com.haker.hakerweather.databinding.FragmentSportsBinding
import com.haker.hakerweather.util.Resource
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SportsFragment"

@AndroidEntryPoint
class SportsFragment : Fragment() {

    private var _binding: FragmentSportsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: SportsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSportsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sportsAdapter = SportsAdapter()

        binding.apply {
            rvFootball.apply {
                adapter = sportsAdapter
                //setHasFixedSize(true)
            }

            rvCricket.apply {
                adapter = sportsAdapter
                //setHasFixedSize(true)
            }

            rvGolf.apply {
                adapter = sportsAdapter
                //setHasFixedSize(true)
            }
        }

        viewModel.data.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { sportsResponse ->

                        binding.progressBar.visibility = View.INVISIBLE

                        if (sportsResponse != null) {
                            val footballList = sportsResponse.football
                            Log.i(TAG, "footballList_size = ${footballList.size}")
                            if (footballList.isNotEmpty()) {
                                binding.rvFootball.visibility = View.VISIBLE
                                binding.tvNoDataFootball.visibility = View.GONE
                                sportsAdapter.submitList(footballList.toList())
                                sportsAdapter.notifyDataSetChanged()
                            }
                            else {
                                binding.tvNoDataFootball.visibility = View.VISIBLE
                                binding.rvFootball.visibility = View.GONE
                            }

                            val cricketList = sportsResponse.cricket
                            Log.i(TAG, "cricketList_size = ${cricketList.size}")
                            if (cricketList.isNotEmpty()) {
                                binding.rvCricket.visibility = View.VISIBLE
                                binding.tvNoDataCricket.visibility = View.GONE
                                sportsAdapter.submitList(cricketList.toList())
                                sportsAdapter.notifyDataSetChanged()
                            }
                            else {
                                binding.tvNoDataCricket.visibility = View.VISIBLE
                                binding.rvCricket.visibility = View.GONE
                            }

                            val golfList = sportsResponse.golf
                            Log.i(TAG, "golfList_size = ${golfList.size}")
                            if (golfList.isNotEmpty()) {
                                binding.rvGolf.visibility = View.VISIBLE
                                binding.tvNoDataGolf.visibility = View.GONE
                                sportsAdapter.submitList(golfList.toList())
                                sportsAdapter.notifyDataSetChanged()
                            }
                            else {
                                binding.tvNoDataGolf.visibility = View.VISIBLE
                                binding.rvGolf.visibility = View.GONE
                            }
                        }
                        else {
                            binding.tvNoDataFootball.visibility = View.VISIBLE
                            binding.tvNoDataCricket.visibility = View.VISIBLE
                            binding.tvNoDataGolf.visibility = View.VISIBLE
                            binding.rvFootball.visibility = View.GONE
                            binding.rvCricket.visibility = View.GONE
                            binding.rvGolf.visibility = View.GONE
                        }
                    }
                }
                is Resource.Error -> {
                    it.message?.let {message ->
                        //Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Error : $message")
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.tvNoDataFootball.visibility = View.VISIBLE
                        binding.tvNoDataCricket.visibility = View.VISIBLE
                        binding.tvNoDataGolf.visibility = View.VISIBLE
                        binding.rvFootball.visibility = View.GONE
                        binding.rvCricket.visibility = View.GONE
                        binding.rvGolf.visibility = View.GONE
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        binding.btnSearch.setOnClickListener {
            viewModel.sports(binding.etSearch.text.toString())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}