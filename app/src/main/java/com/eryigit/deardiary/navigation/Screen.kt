package com.eryigit.deardiary.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Authentication : Screen()
    @Serializable
    data object Home : Screen()
    @Serializable
    data class Write(val diaryDetailId: String? = null ) : Screen()
}