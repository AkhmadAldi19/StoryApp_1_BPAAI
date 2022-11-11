package com.akhmadaldi.storyapp.data.network.auth


data class LoginResult(
    val userId: String,
    val name: String,
    val token: String
)
