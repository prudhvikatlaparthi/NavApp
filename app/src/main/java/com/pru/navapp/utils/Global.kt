package com.pru.navapp.utils

import android.location.Location
import android.os.Build
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import com.pru.navapp.MainActivity
import com.pru.navapp.R
import com.pru.navapp.appContext
import com.pru.navapp.base.ResultBack
import com.pru.navapp.ui.composables.AlertItem

object Global {
    fun Fragment.getMainActivity(): MainActivity {
        return this.requireActivity() as MainActivity
    }

    fun showLoader(
        isForInlineProgress: Boolean = false,
        message: String = getString(R.string.loading)
    ) {
        if (isForInlineProgress) {
            ComposableUtils.inlineProgressState = true
        } else {
            ComposableUtils.loaderMessage = message
            ComposableUtils.loaderState = true
        }
    }

    fun dismissLoader() {
        ComposableUtils.loaderMessage = getString(R.string.loading)
        ComposableUtils.loaderState = false
        ComposableUtils.inlineProgressState = false
    }

    fun showAlertDialog(alertItem: AlertItem) {
        ComposableUtils.alertItem = alertItem
        ComposableUtils.alertDialogState = true
    }

    fun showToast(message: String?) {
        message?.let {
            Toast.makeText(appContext, it, Toast.LENGTH_SHORT).show()
        }
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any? = emptyArray()): String {
        return appContext.resources.getString(resId, *formatArgs)
    }

    inline fun Fragment.createOptionsMenu(
        @MenuRes menuRes: Int,
        crossinline onMenuItemSelected: (menuItem: MenuItem) -> Boolean
    ) {
        val menuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
                    menuInflater.inflate(menuRes, menu)

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    onMenuItemSelected(menuItem)
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    fun Fragment.getLocation(
        locationListener: (Location) -> Unit
    ) {
        getMainActivity().getLocation(
            locationListener = locationListener
        )
    }

    fun Fragment.setResult(result: ResultBack) {
        setFragmentResult("result", bundleOf("result" to result))
    }

    @Suppress("DEPRECATION")
    fun Fragment.setResultListener(listener: (ResultBack) -> Unit) {
        setFragmentResultListener("result") { _, bundle ->
            if (bundle.containsKey("result")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable("result", ResultBack::class.java)?.let {
                        listener.invoke(it)
                    }
                } else {
                    bundle.getParcelable<ResultBack?>("result")?.let {
                        listener.invoke(it)
                    }
                }
            }
        }
    }
}