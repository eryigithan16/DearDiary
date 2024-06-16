package com.example.deardiary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.deardiary.presentation.screens.auth.AuthenticationScreen
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.rememberOneTapSignInState

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
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        AuthenticationScreen(
            loadingState = oneTapState.opened,
            oneTabState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = { oneTapState.open() }
        )
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