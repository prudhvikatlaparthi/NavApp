package com.pru.navapp.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface AppNavigator {
    val navChannel_: Channel<NavigationIntent>

    val navChannel: Flow<NavigationIntent>

    fun navigate(resId: Int, navOptions: NavOptions? = null)

    fun navigate(directions: NavDirections, navOptions: NavOptions? = null)

    fun popBackStack()

    fun popBackStack(destinationId: Int, inclusive: Boolean)

    fun navigateUp()

    sealed class NavigationIntent {
        data class Navigate(@IdRes val resId: Int, val navOptions: NavOptions? = null) :
            NavigationIntent()

        data class NavigateDirections(
            val directions: NavDirections,
            val navOptions: NavOptions? = null
        ) : NavigationIntent()

        object PopBackStack : NavigationIntent()

        data class PopBackStackWithID(@IdRes val destinationId: Int, val inclusive: Boolean) :
            NavigationIntent()

        object NavigateUp : NavigationIntent()
    }
}