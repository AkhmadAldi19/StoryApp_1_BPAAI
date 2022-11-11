package com.akhmadaldi.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.akhmadaldi.storyapp.view.detail.DetailStoryActivity
import com.akhmadaldi.storyapp.data.model.StoryModel
import com.akhmadaldi.storyapp.databinding.ItemlistStoryBinding
import com.bumptech.glide.Glide

class StoryAdapter(private val listStory: ArrayList<StoryModel>) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    inner class ListViewHolder(binding: ItemlistStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private var photo = binding.ivItemPhoto
        private var profileName = binding.firstLetterName
        private var userName = binding.tvItemName


        fun bind(story: StoryModel) {
            Glide.with(itemView.context)
                .load(story.photo)
                .into(photo)
            profileName.text = story.userName.substring(0, 1)
            userName.text = story.userName


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra("Story", story)
                itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ItemlistStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size
}