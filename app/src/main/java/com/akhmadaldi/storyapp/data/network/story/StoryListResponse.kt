package com.akhmadaldi.storyapp.data.network.story

data class StoryListResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<StoryListItem>
)