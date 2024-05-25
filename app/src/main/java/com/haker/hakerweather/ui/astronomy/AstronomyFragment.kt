package com.haker.hakerweather.ui.astronomy

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.haker.hakerweather.databinding.FragmentAstronomyBinding
import com.haker.hakerweather.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

private const val TAG = "AstronomyFragment"

@AndroidEntryPoint
class AstronomyFragment : Fragment() {

    private var _binding: FragmentAstronomyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: AstronomyViewModel by viewModels()

    private var todayDateStr = ""

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAstronomyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val todayDate = Calendar.getInstance().time
        todayDateStr = SimpleDateFormat("yyyy-MM-dd").format(todayDate)
        Log.i(TAG, "todayDateStr = $todayDateStr")

        binding.tvTodayDate.text = "Today Date : $todayDateStr"

        viewModel.data.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { astronomyResponse ->

                        binding.progressBar.visibility = View.INVISIBLE
                        resetData()

                        if (astronomyResponse != null) {
                            binding.llResult.visibility = View.VISIBLE

                            binding.tvName.text = astronomyResponse.location.name
                            Log.i(TAG, "name = ${astronomyResponse.location.name}")
                            binding.tvRegion.text = astronomyResponse.location.region
                            binding.tvCountry.text = astronomyResponse.location.country
                            val distance = calculateDistance(astronomyResponse.location.lat!!, astronomyResponse.location.lon!!, 20.78, 90.17)
                            binding.tvDistance.text = "$distance meter"
                            val localTimeWithLong = astronomyResponse.location.localtime_epoch
                            val localTimeStr = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date(localTimeWithLong!!))
                            binding.tvLocalTime.text = localTimeStr
                            binding.tvSunrise.text = astronomyResponse.astronomy.astro.sunrise
                            binding.tvSunset.text = astronomyResponse.astronomy.astro.sunset
                            binding.tvMoonrise.text = astronomyResponse.astronomy.astro.moonrise
                            binding.tvMoonset.text = astronomyResponse.astronomy.astro.moonset

                            val diffRise = calculateTimeDiff(astronomyResponse.astronomy.astro.sunrise!!, astronomyResponse.astronomy.astro.moonrise!!, "rise")
                            binding.tvDiffRise.text = diffRise

                            val diffSet = calculateTimeDiff(astronomyResponse.astronomy.astro.sunset!!, astronomyResponse.astronomy.astro.moonset!!, "set")
                            binding.tvDiffSet.text = diffSet

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
            viewModel.astronomy(binding.etSearch.text.toString(), todayDateStr)
        }

        return root
    }

    private fun resetData() {
        binding.tvName.text = ""
        binding.tvRegion.text = ""
        binding.tvCountry.text = ""
        binding.tvDistance.text = ""
        binding.tvLocalTime.text = ""
    }

    //can't calculate distance from one LatLon, normally it needs two LatLon
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        var distance = 0.0
        var R = 6371 // Radius of the earth in km
        var dLat = deg2rad(lat2-lat1)  // deg2rad below
        var dLon = deg2rad(lon2-lon1)
        var a =
            Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                    Math.sin(dLon/2) * Math.sin(dLon/2)

        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
        var d = R * c // Distance in km
        distance = d * 1000 // Distance in meter
        return distance
    }

    private fun deg2rad(deg: Double): Double {
        return deg * (Math.PI/180)
    }

    @SuppressLint("SimpleDateFormat")
    private fun calculateTimeDiff(time1: String, time2: String, type: String): String {
        var timeDiff = ""
        try {
            val date1 = SimpleDateFormat("hh:mm a").parse(time1)
            val date2 = SimpleDateFormat("hh:mm a").parse(time2)
            var different = 0L
            different = if (type == "rise")
                date2.time - date1.time
            else
                date1.time - date2.time
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            val elapsedDays: Long = different / daysInMilli
            different = different % daysInMilli

            val elapsedHours: Long = different / hoursInMilli
            different = different % hoursInMilli

            val elapsedMinutes: Long = different / minutesInMilli
            different = different % minutesInMilli

            val elapsedSeconds: Long = different / secondsInMilli
            timeDiff = "$elapsedDays days $elapsedHours hours $elapsedMinutes minutes $elapsedSeconds seconds"
        } catch (e: Exception) {
            Log.e(TAG, "exception = ${e.message}")
            timeDiff = "Can't calculate coz some data is not correct!"
        }
        return timeDiff
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}