package com.example.deardiary.navigation

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.deardiary.data.repository.MongoDB
import com.example.deardiary.model.Diary
import com.example.deardiary.model.Mood
import com.example.deardiary.presentation.components.DisplayAlertDialog
import com.example.deardiary.presentation.screens.auth.AuthenticationScreen
import com.example.deardiary.presentation.screens.auth.AuthenticationViewModel
import com.example.deardiary.presentation.screens.home.HomeScreen
import com.example.deardiary.presentation.screens.home.HomeViewModel
import com.example.deardiary.presentation.screens.write.WriteScreen
import com.example.deardiary.presentation.screens.write.WriteViewModel
import com.example.deardiary.util.Constants.APP_ID
import com.example.deardiary.util.RequestState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable<Screen.Authentication> {
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val viewModel: AuthenticationViewModel = viewModel()
        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }
        AuthenticationScreen(
            authenticated = viewModel.authenticated.value,
            loadingState = viewModel.loadingState.value,
            oneTabState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onTokenIdReceived = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated!")
                        viewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    }
                )
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            },
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable<Screen.Home> {
        val viewModel: HomeViewModel = viewModel()
        val diaries by viewModel.diaries
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = diaries) {
            if (diaries !is RequestState.Loading) {
                onDataLoaded()
            }
        }

        HomeScreen(
            diaries = diaries,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            drawerState = drawerState,
            onSignOutClicked = {
                signOutDialogOpened = true
            },
            navigateToWrite = navigateToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs
        )
        LaunchedEffect(key1 = Unit) {
            MongoDB.configureTheRealm()
        }
        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to Sign Out from your Google Account",
            dialogOpened = signOutDialogOpened,
            onCloseDialog = { signOutDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(APP_ID).currentUser
                    if (user != null) {
                        user.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            }
        )
    }
}

fun NavGraphBuilder.writeRoute(onBackPressed: () -> Unit) {
    composable<Screen.Write> {
        /*backStackEntry ->
        val args = backStackEntry.toRoute<Screen.Write>()
        args.id*/
        val viewModel: WriteViewModel = viewModel()
        val uiState = viewModel.uiState
        LaunchedEffect(key1 = uiState) {

        }
        val pagerState = rememberPagerState(pageCount = { Mood.entries.size })
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }
        WriteScreen(
            uiState = uiState,
            moodName = { Mood.entries[pageNumber].name },
            pagerState = pagerState,
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = { viewModel.setDescription(description = it) },
            onDeleteConfirmed = {},
            onBackPressed = onBackPressed,
            onSaveClicked = {
                viewModel.upsertDiary(
                    diary = it.apply { mood = Mood.entries[pageNumber].name },
                    onSuccess = {
                        onBackPressed()
                    },
                    onError = {}
                )
            }
        )
    }
}