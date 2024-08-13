package com.example.deardiary.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.deardiary.model.Diary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    selectedDiary: Diary?,
    pagerState: PagerState,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = selectedDiary,
                onDeleteConfirmed = onDeleteConfirmed,
                onBackPressed = onBackPressed
            )
        },
        content = {
            WriteContent(
                pagerState = pagerState,
                title = "",
                onTitleChanged = {},
                description = "",
                onDescriptionChanged = {},
                paddingValues = it
            )
        }
    )
}