package com.akhmadaldi.storyapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akhmadaldi.storyapp.data.model.StoryModel
import com.akhmadaldi.storyapp.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<StoryModel>("Story") as StoryModel
        Glide.with(applicationContext)
            .load(story.photo)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = story.userName
        binding.tvDetailDescription.text = story.description
        binding.tvFirstLetterName.text = story.userName.substring(0, 1)
        binding.tvDetailCreated.text = story.createdAt.substringBefore("T")
    }

}


