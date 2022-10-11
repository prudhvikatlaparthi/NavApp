package com.pru.navapp

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.pru.navapp.composables.Alert
import com.pru.navapp.composables.AlertItem
import com.pru.navapp.composables.Loader
import com.pru.navapp.databinding.ActivityMainBinding
import com.pru.navapp.fragments.MainViewModel
import com.pru.navapp.listeners.OnBackPressListener
import com.pru.navapp.navigation.AppNavigator.NavigationIntent
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private val viewModel by viewModels<MainViewModel>()

    companion object {
        var loaderMessage: String = ""
        var loaderState by mutableStateOf(false)
        var inlineProgressState by mutableStateOf(false)
        var alertItem = AlertItem(
            title = null,
            message = "",
            posBtnText = "",
            negBtnText = null,
            posBtnListener = {},
            negBtnListener = {})
        var alertDialogState by mutableStateOf(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        appBarConfiguration =
            AppBarConfiguration(
                setOf(R.id.mainFragment),
                binding.drawerLayout
            )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)

        binding.composeView.setContent {
            Loader(message = loaderMessage, showLoader = loaderState, onDismissRequest = {
                loaderState = false
            })
            Alert(
                showAlert = alertDialogState,
                title = alertItem.title,
                message = alertItem.message,
                posBtnText = alertItem.posBtnText,
                negBtnText = alertItem.negBtnText,
                posBtnListener = {
                    alertDialogState = false
                    alertItem.posBtnListener.invoke()
                },
                negBtnListener = {
                    alertItem.negBtnListener?.let {
                        alertDialogState = false
                        it.invoke()
                    }
                })
        }

        binding.pbView.setContent {
            if (inlineProgressState){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }

        setEvents()
        backPressCallback()
        observeNavigation()
    }

    private fun observeNavigation() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                appNavigator.navChannel.collect { intent ->
                    when (intent) {
                        is NavigationIntent.Navigate -> {
                            navController.navigate(
                                intent.resId,
                                navOptions = intent.navOptions,
                                args = null
                            )
                        }
                        is NavigationIntent.NavigateDirections -> {
                            navController.navigate(
                                intent.directions,
                                navOptions = intent.navOptions
                            )
                        }
                        is NavigationIntent.NavigateUp -> {
                            navController.navigateUp()
                        }
                        is NavigationIntent.PopBackStack -> {
                            navController.popBackStack()
                        }
                        is NavigationIntent.PopBackStackWithID -> {
                            navController.popBackStack(
                                destinationId = intent.destinationId,
                                inclusive = intent.inclusive
                            )
                        }
                    }
                }
            }
        }
    }

    private fun backPressCallback() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitOnBackPressed()
                }
            })
    }

    private fun exitOnBackPressed() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is OnBackPressListener) {
            currentFragment.backPress()
        } else if (currentFragment.toString().lowercase().contains(
                navController.graph.startDestDisplayName.replace(packageName.plus(":id/"), "")
                    .lowercase()
            )
        ) {
            finish()
        } else {
            navController.popBackStack()
        }
    }

    private fun setEvents() {
        binding.tvSettings.setOnClickListener {
            /*closeDrawer()
            navController.navigate(R.id.action_mainFragment_to_settingsFragment)*/
        }
        binding.tvAbout.setOnClickListener {
            closeDrawer()
            navController.navigate(R.id.action_mainFragment_to_aboutFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun getCurrentFragment(): Fragment? {
        return navHostFragment.childFragmentManager.fragments.getOrNull(0)
    }

    private fun isDrawerOpen(): Boolean =
        binding.drawerLayout.isDrawerOpen(GravityCompat.START)

    private fun closeDrawer() = binding.drawerLayout.closeDrawer(GravityCompat.START)


    override fun onSupportNavigateUp(): Boolean {
        val currentFragment = getCurrentFragment()
        return if (currentFragment is OnBackPressListener) {
            currentFragment.backPress()
            false
        } else {
            NavigationUI.navigateUp(
                navController,
                appBarConfiguration
            ) || super.onSupportNavigateUp()
        }
    }

    fun lockNavigationDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun unLockNavigationDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun navigate(destination: Int) {
        navController.navigate(destination)
    }
}