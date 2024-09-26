package com.example.deardiary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.auth.navigation.authenticationRoute
import com.example.home.navigation.homeRoute
import com.example.util.Screen
import com.example.write.navigation.writeRoute

@Composable
fun SetupNavGraph(
    startDestination: Screen,
    navController: NavHostController,
    onDataLoaded: () -> Unit
) {
    NavHost(startDestination = startDestination, navController = navController) {
        authenticationRoute(
            navigateToHome = {
                navController.navigate(Screen.Home)
            },
            onDataLoaded = onDataLoaded
        )
        homeRoute(
            navigateToWrite = { navController.navigate(Screen.Write()) },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication)
            },
            onDataLoaded = onDataLoaded,
            navigateToWriteWithArgs = {
                navController.navigate(Screen.Write(it))
            }
        )
        writeRoute(
            onBackPressed = { navController.popBackStack() }
        )
    }
}