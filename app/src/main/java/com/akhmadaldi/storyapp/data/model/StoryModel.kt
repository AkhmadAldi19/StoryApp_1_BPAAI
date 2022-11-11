package com.akhmadaldi.storyapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class StoryModel(
    val storyId: String,
    val userName: String,
    val description: String,
    val photo: String,
    val createdAt: String,
): Parcelable
