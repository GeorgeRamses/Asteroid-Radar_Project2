package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.databinding.FragmentMainBinding

@RequiresApi(Build.VERSION_CODES.N)
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        val factory = ViewModelFactory(activity.application)
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel.erroMessage.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                Toast.makeText(this.context, error, Toast.LENGTH_LONG).show()
                viewModel.resetError()
            }


        })
        binding.viewModel = viewModel
        binding.asteroidRecycler.adapter = AstroidAdapter {
            viewModel.displayAsteroid(it)
        }
        viewModel.navigateToSelected.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidComplete()
            }

        })
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_today_menu -> viewModel.todayAsteroid()
            R.id.show_week_menu -> viewModel.weekAsteroid()
            R.id.show_saved_menu -> viewModel.savedAsteroid()
        }
        return true
    }
}
