package com.example.deardiary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.deardiary.presentation.screens.auth.AuthenticationScreen

@Composable
fun SetupNavGraph(startDestination: Screen, navController: NavHostController) {
    NavHost(startDestination = startDestination, navController = navController) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute() {
    composable<Screen.Authentication> {
        AuthenticationScreen(loadingState = false, onButtonClicked = {

        })
    }
}

fun NavGraphBuilder.homeRoute() {
    composable<Screen.Home> {}
}

fun NavGraphBuilder.writeRoute() {
    composable<Screen.Write> { backStackEntry ->
        val args = backStackEntry.toRoute<Screen.Write>()
        args.id
    }
}