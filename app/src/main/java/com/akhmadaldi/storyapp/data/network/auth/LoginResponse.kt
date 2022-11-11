package com.akhmadaldi.storyapp.data.network.auth


data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult
)
