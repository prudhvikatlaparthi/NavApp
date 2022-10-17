package com.pru.navapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
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
import com.pru.navapp.databinding.ActivityMainBinding
import com.pru.navapp.listeners.OnBackPressListener
import com.pru.navapp.location.LocationState
import com.pru.navapp.navigation.AppNavigator.NavigationIntent
import com.pru.navapp.ui.composables.Alert
import com.pru.navapp.ui.composables.AlertItem
import com.pru.navapp.ui.composables.Loader
import com.pru.navapp.ui.main.MainViewModel
import com.pru.navapp.utils.ComposableUtils.alertDialogState
import com.pru.navapp.utils.ComposableUtils.alertItem
import com.pru.navapp.utils.ComposableUtils.inlineProgressState
import com.pru.navapp.utils.ComposableUtils.loaderMessage
import com.pru.navapp.utils.ComposableUtils.loaderState
import com.pru.navapp.utils.Global.showAlertDialog
import com.pru.navapp.utils.Global.showToast
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private var locationListener: ((Location) -> Unit)? = null
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainFragment), binding.drawerLayout
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)

        binding.composeView.setContent {
            Loader(message = loaderMessage, showLoader = loaderState, onDismissRequest = {
                loaderState = false
            })
            Alert(showAlert = alertDialogState,
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
            if (inlineProgressState) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }

        setEvents()
        backPressCallback()
        observeNavigation()
        observeLocationClient()
    }

    private val requestGpsRequest =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            Log.i("Prudhvi Log", "$result: ")
            if (result.resultCode == RESULT_OK) {
                locationClient.requestLocation()
            } else {
                loaderState = false
            }
        }

    private val requestLocationPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        Log.i("Prudhvi Log", "$permissions: ")
        val accepted = permissions.filter { it.value }
        if (accepted.isNotEmpty()) {
            locationClient.start()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, ACCESS_FINE_LOCATION
                )
            ) {
                showAlertDialog(
                    alertItem = AlertItem(title = "Location is mandatory for this app",
                        message = "please grant permission.",
                        posBtnText = "YES",
                        negBtnText = "NO",
                        posBtnListener = {
                            locationClient.start()
                        },
                        negBtnListener = {})
                )
            } else {
                showAlertDialog(
                    alertItem = AlertItem(title = "Location is mandatory for this app",
                        message = "please grant permission.",
                        posBtnText = "YES",
                        negBtnText = "NO",
                        posBtnListener = {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri: Uri = Uri.fromParts(
                                "package", packageName, null
                            )
                            intent.data = uri
                            settingsActivityResultLauncher.launch(intent)
                        },
                        negBtnListener = {})
                )
            }
        }
    }

    private val settingsActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        lifecycleScope.launchWhenStarted {
            delay(1000)
            locationClient.start()
        }
    }

    private fun observeLocationClient() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                locationClient.locationChannel.collect { locationState ->
                    when (locationState) {
                        is LocationState.PermissionRequired -> {
                            requestLocationPermissions.launch(
                                arrayOf(
                                    ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION
                                )
                            )
                        }
                        is LocationState.Failed -> {
                            showToast(message = locationState.message)
                        }
                        is LocationState.GPSRequired -> {
                            requestGpsRequest.launch(
                                IntentSenderRequest.Builder(locationState.pendingIntent).build()
                            )
                        }
                        is LocationState.Success -> {
                            Log.i(
                                "Prudhvi Log", "observeLocationClient: $locationState"
                            )
                            locationListener?.invoke(locationState.location)
                            locationClient.stop()
                        }
                    }
                }
            }
        }
        locationClient.start()
    }

    private fun observeNavigation() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                appNavigator.navChannel.collect { intent ->
                    when (intent) {
                        is NavigationIntent.Navigate -> {
                            navController.navigate(
                                intent.resId, navOptions = intent.navOptions, args = null
                            )
                        }
                        is NavigationIntent.NavigateDirections -> {
                            navController.navigate(
                                intent.directions, navOptions = intent.navOptions
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
                                destinationId = intent.destinationId, inclusive = intent.inclusive
                            )
                        }
                    }
                }
            }
        }
    }

    private fun backPressCallback() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
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

    private fun isDrawerOpen(): Boolean = binding.drawerLayout.isDrawerOpen(GravityCompat.START)

    private fun closeDrawer() = binding.drawerLayout.closeDrawer(GravityCompat.START)


    override fun onSupportNavigateUp(): Boolean {
        val currentFragment = getCurrentFragment()
        return if (currentFragment is OnBackPressListener) {
            currentFragment.backPress()
            false
        } else {
            NavigationUI.navigateUp(
                navController, appBarConfiguration
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

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    fun getLocation(locationListener: (Location) -> Unit) {
        this.locationListener = locationListener
        locationClient.start()
    }
}