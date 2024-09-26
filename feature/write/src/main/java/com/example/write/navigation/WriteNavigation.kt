package com.example.write.navigation

import android.widget.Toast
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.util.Screen
import com.example.util.model.Mood
import com.example.write.WriteScreen
import com.example.write.WriteViewModel

fun NavGraphBuilder.writeRoute(onBackPressed: () -> Unit) {
    composable<Screen.Write> {
        /*backStackEntry ->
        val args = backStackEntry.toRoute<Screen.Write>()
        args.id*/
        val viewModel: WriteViewModel = hiltViewModel()
        val context = LocalContext.current
        val uiState = viewModel.uiState
        val galleryState = viewModel.galleryState
        LaunchedEffect(key1 = uiState) {

        }
        val pagerState = rememberPagerState(pageCount = { Mood.entries.size })
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }
        WriteScreen(
            uiState = uiState,
            moodName = { Mood.entries[pageNumber].name },
            pagerState = pagerState,
            galleryState = galleryState,
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = { viewModel.setDescription(description = it) },
            onDeleteConfirmed = {
                viewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    },
                    onError = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onBackPressed = onBackPressed,
            onSaveClicked = {
                viewModel.upsertDiary(
                    diary = it.apply { mood = Mood.entries[pageNumber].name },
                    onSuccess = {
                        onBackPressed()
                    },
                    onError = { errorMessage -> Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show() }
                )
            },
            onDateTimeUpdated = {
                viewModel.updateDateTime(it)
            },
            onImageSelect = {
                val type = context.contentResolver.getType(it)?.split("/")?.last() ?: "jpg"
                viewModel.addImage(image = it, imageType = type)
            },
            onImageDeleteClicked = {
                galleryState.removeImage(it)
            }
        )
    }
}