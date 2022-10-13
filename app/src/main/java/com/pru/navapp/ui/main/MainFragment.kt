package com.pru.navapp.ui.main

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.pru.navapp.R
import com.pru.navapp.base.BaseFragment
import com.pru.navapp.databinding.FragmentMainBinding
import com.pru.navapp.listeners.RefreshListener
import com.pru.navapp.utils.Global.createOptionsMenu
import com.pru.navapp.listeners.NavigationDrawListener

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate),
    NavigationDrawListener {
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private fun getCurrentFragment(): Fragment {
        val frag = navHostFragment.childFragmentManager.fragments[0]
        Log.i("Prudhvi Log", "getCurrentFragment: $frag")
        return frag
    }

    override fun setup() {
        navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_b_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        createOptionsMenu(menuRes = R.menu.menu_dashboard, onMenuItemSelected = {
            when (it.itemId) {
                R.id.action_settings -> {
                    findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                    true
                }
                else -> false
            }
        })
    }

    override suspend fun observers() {

    }

    override fun listeners() {
        binding.tvB1.setOnClickListener {
            navController.popBackStack()
            navController.navigate(R.id.b1Fragment)
        }
        binding.tvB2.setOnClickListener {
            navController.popBackStack()
            navController.navigate(R.id.b2Fragment)
        }
        binding.tvB3.setOnClickListener {
            navController.popBackStack()
            navController.navigate(R.id.b3Fragment)
        }

        setFragmentResultListener("KEY_RESULT") { _, bundle ->
            if (getCurrentFragment() is RefreshListener) {
                (getCurrentFragment() as RefreshListener).onRefresh(bundle)
            }
        }
    }

}