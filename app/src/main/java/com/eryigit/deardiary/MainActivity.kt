package com.eryigit.deardiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.eryigit.deardiary.data.database.ImageToDeleteDao
import com.eryigit.deardiary.data.database.ImageToUploadDao
import com.eryigit.deardiary.navigation.Screen
import com.eryigit.deardiary.navigation.SetupNavGraph
import com.eryigit.deardiary.ui.theme.DearDiaryTheme
import com.eryigit.deardiary.util.Constants.APP_ID
import com.eryigit.deardiary.util.retryDeletingImageFromFirebase
import com.eryigit.deardiary.util.retryUploadingImageToFirebase
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

    @Inject
    lateinit var imageToDeleteDao: ImageToDeleteDao

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
        cleanUpCheck(scope = lifecycleScope, imageToUploadDao, imageToDeleteDao)
    }
}

private fun cleanUpCheck(scope: CoroutineScope, imageToUploadDao: ImageToUploadDao, imageToDeleteDao: ImageToDeleteDao) {
    scope.launch(Dispatchers.IO) {
        val uploadList = imageToUploadDao.getAllImages()
        uploadList.forEach { imageToUpload ->
            retryUploadingImageToFirebase(
                imageToUpload,
                onSuccess = {
                    scope.launch(Dispatchers.IO) {
                        imageToUploadDao.cleanupImage(imageToUpload.id)
                    }
                }
            )
        }
        val deleteList = imageToDeleteDao.getAllImages()
        deleteList.forEach { image ->
            retryDeletingImageFromFirebase(
                imageToDelete = image,
                onSuccess = {
                    scope.launch(Dispatchers.IO) {
                        imageToDeleteDao.cleanupImage(image.id)
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