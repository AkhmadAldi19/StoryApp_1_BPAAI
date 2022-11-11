package com.akhmadaldi.storyapp.data.network.story


data class StoryListItem(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: String,
    val lon: String
)
