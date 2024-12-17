package com.ayukrisna.dicodingstory.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ayukrisna.dicodingstory.R

data class TopLevelRoute<T : Any>(
    val title: String,
    val route: T,
    val activeIcon: Int,
    val inactiveIcon: Int,
)

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding(),
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary

    ) {
        val topLevelRoutes = listOf(
            TopLevelRoute(
                title = "Story",
                route = RootScreen.StoryNav,
                activeIcon = R.drawable.filled_home,
                inactiveIcon = R.drawable.outline_home
            ),
            TopLevelRoute(
                title = "Maps",
                route = RootScreen.MapNav,
                activeIcon = R.drawable.filled_map,
                inactiveIcon = R.drawable.outline_map
            ),
            TopLevelRoute(
                title = "Settings",
                route = RootScreen.SettingNav,
                activeIcon = R.drawable.filled_settings,
                inactiveIcon = R.drawable.outline_settings

            )
        )


        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        topLevelRoutes.map{ topLevelRoute ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { current ->
                    current.hasRoute(topLevelRoute.route::class)} == true,
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (currentDestination?.hierarchy?.any { current ->
                                    current.hasRoute(topLevelRoute.route::class)} == true) {
                                topLevelRoute.activeIcon
                            } else {
                                topLevelRoute.inactiveIcon
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}