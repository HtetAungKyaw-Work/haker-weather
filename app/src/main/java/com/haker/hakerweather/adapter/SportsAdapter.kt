package com.haker.hakerweather.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.haker.hakerweather.data.model.Sport
import com.haker.hakerweather.databinding.ListRowSportBinding

class SportsAdapter(): ListAdapter<Sport, SportsAdapter.ArticleViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ListRowSportBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ArticleViewHolder(private val binding: ListRowSportBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(sport: Sport){
            binding.apply {
                tvStadium.text = "  ${sport.stadium}"
                tvCountry.text = "  ${sport.country}"
                tvTourName.text = "  ${sport.tournament}"
                val start = sport.start
                if (start!!.isNotEmpty()) {
                    val startArr = start.split(" ")
                    tvDate.text = "  ${startArr[0]}"
                    tvTime.text = "  ${startArr[1]}"
                }
                tvMatch.text = "  ${sport.match}"
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Sport>(){
        override fun areItemsTheSame(oldItem: Sport, newItem: Sport): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Sport, newItem: Sport): Boolean {
            return oldItem == newItem
        }

    }
}