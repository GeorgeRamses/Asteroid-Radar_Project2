package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidRecyclerBinding

class AstroidAdapter(private val onClicked: (Asteroid) -> Unit) :
    ListAdapter<Asteroid, AstroidAdapter.AstroidHolder>(DiffCallback) {
    class AstroidHolder(private val binding: AsteroidRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.astroid = asteroid
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AstroidHolder {
        val astroid = AstroidHolder(AsteroidRecyclerBinding.inflate(LayoutInflater.from(parent.context)))
        astroid.itemView.setOnClickListener {
            val position = astroid.adapterPosition
            onClicked(getItem(position))
        }
        return astroid
    }

    override fun onBindViewHolder(holder: AstroidHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}