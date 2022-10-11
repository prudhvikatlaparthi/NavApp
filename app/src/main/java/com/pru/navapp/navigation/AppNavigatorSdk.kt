package com.pru.navapp.navigation

import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.pru.navapp.navigation.AppNavigator.NavigationIntent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class AppNavigatorSdk : AppNavigator {
    override val navChannel_: Channel<NavigationIntent> = Channel(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )
    override val navChannel
        get() = navChannel_.receiveAsFlow()

    override fun navigate(resId: Int, navOptions: NavOptions?) {
        val navigationIntent = NavigationIntent.Navigate(resId = resId, navOptions = navOptions)
        navChannel_.trySend(navigationIntent)
    }

    override fun navigate(directions: NavDirections, navOptions: NavOptions?) {
        val navigationIntent =
            NavigationIntent.NavigateDirections(directions = directions, navOptions = navOptions)
        navChannel_.trySend(navigationIntent)
    }

    override fun popBackStack() {
        val pop = NavigationIntent.PopBackStack
        navChannel_.trySend(pop)
    }

    override fun popBackStack(destinationId: Int, inclusive: Boolean) {
        val pop = NavigationIntent.PopBackStackWithID(
            destinationId = destinationId,
            inclusive = inclusive
        )
        navChannel_.trySend(pop)
    }

    override fun navigateUp() {
        navChannel_.trySend(NavigationIntent.NavigateUp)
    }

}