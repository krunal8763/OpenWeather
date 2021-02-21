package com.krunal.openweather.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.krunal.openweather.data.database.WeatherData
import com.krunal.openweather.databinding.RowBookemarkedCityBinding

class BookMarkCityAdapters : RecyclerView.Adapter<BookMarkCityAdapters.ViewModel>() {
    lateinit var onDelete: (WeatherData) -> Unit
    lateinit var onItemClick: (WeatherData) -> Unit
    private val bookMarkCityList = mutableListOf<WeatherData>()

    fun updateList(list: List<WeatherData>) {
        bookMarkCityList.clear()
        bookMarkCityList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewModel(val binding: RowBookemarkedCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: WeatherData) {
            binding.txtCity.text = data.cityName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val binding = RowBookemarkedCityBinding.inflate(LayoutInflater.from(parent.context))
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = bookMarkCityList.size

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val bookmark = bookMarkCityList[position]
        holder.setData(bookmark)
        holder.binding.deleteImage.setOnClickListener {
            onDelete(bookmark)
        }
        holder.binding.root.setOnClickListener { onItemClick(bookmark) }
    }
}