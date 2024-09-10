package com.example.deardiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.deardiary.data.database.ImageToUploadDao
import com.example.deardiary.navigation.Screen
import com.example.deardiary.navigation.SetupNavGraph
import com.example.deardiary.ui.theme.DearDiaryTheme
import com.example.deardiary.util.Constants.APP_ID
import com.example.deardiary.util.retryUploadingImageToFirebase
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageToUploadDao: ImageToUploadDao

    var keepSplashOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpened
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FirebaseApp.initializeApp(this)
        setContent {
            DearDiaryTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = getStartDestination(),
                    navController = navController,
                    onDataLoaded = {
                        keepSplashOpened = false
                    }
                )
            }
        }
        cleanUpCheck(scope = lifecycleScope, imageToUploadDao)
    }
}

private fun cleanUpCheck(scope: CoroutineScope, imageToUploadDao: ImageToUploadDao) {
    scope.launch(Dispatchers.IO) {
        val result = imageToUploadDao.getAllImages()
        result.forEach { imageToUpload ->
            retryUploadingImageToFirebase(
                imageToUpload,
                onSuccess = {
                    scope.launch(Dispatchers.IO) {
                        imageToUploadDao.cleanupImage(imageToUpload.id)
                    }
                }
            )
        }
    }
}

private fun getStartDestination(): Screen {
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home else Screen.Authentication
}