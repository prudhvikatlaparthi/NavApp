package com.pru.navapp.utils

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.pru.navapp.MainActivity
import com.pru.navapp.R
import com.pru.navapp.appContext
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
            MainActivity.inlineProgressState = true
        } else {
            MainActivity.loaderMessage = message
            MainActivity.loaderState = true
        }
    }

    fun dismissLoader() {
        MainActivity.loaderMessage = getString(R.string.loading)
        MainActivity.loaderState = false
        MainActivity.inlineProgressState = false
    }

    fun showAlertDialog(alertItem: AlertItem) {
        MainActivity.alertItem = alertItem
        MainActivity.alertDialogState = true
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
}