package com.akhmadaldi.storyapp.view.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhmadaldi.storyapp.view.create.CreateStoryActivity
import com.akhmadaldi.storyapp.R
import com.akhmadaldi.storyapp.util.ViewModelFactory
import com.akhmadaldi.storyapp.view.welcome.WelcomeActivity
import com.akhmadaldi.storyapp.adapter.StoryAdapter
import com.akhmadaldi.storyapp.data.model.StoryModel
import com.akhmadaldi.storyapp.data.model.UserPreference
import com.akhmadaldi.storyapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var mainViewModel : MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvStory: RecyclerView
    private val list = ArrayList<StoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        rvStory = binding.rvListStory
        rvStory.setHasFixedSize(true)
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.listStory.observe(this) {
            list.clear()
            for (story in it) {
                list.add(
                    StoryModel(
                        story.id,
                        story.name,
                        story.description,
                        story.photoUrl,
                        story.createdAt
                    )
                )
            }
            rvStory.layoutManager = LinearLayoutManager(this)

            val listStoryAdapter = StoryAdapter(list)
            rvStory.adapter = listStoryAdapter
        }

        mainViewModel.loading.observe(this) {
            showLoading(it)
        }

        mainViewModel.errorMessage.observe(this) {
            when (it) {
                "Stories fetched successfully" -> {
                    Toast.makeText(this@MainActivity, getString(R.string.fetchedSuccess), Toast.LENGTH_SHORT).show()
                }
                "onFailure" -> {
                    Toast.makeText(this@MainActivity, getString(R.string.failureMessage), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@MainActivity, getString(R.string.notFound), Toast.LENGTH_SHORT).show()
                }
            }
        }
        mainViewModel.getToken().observe(this) { session ->
            if (session.Login) {
                mainViewModel.setData(session.token)

            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupAction() {
        val swipeRefresh = binding.swipeRefresh
        swipeRefresh.setOnRefreshListener {
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            swipeRefresh.isRefreshing = false
        }

        binding.ivAddStory.setOnClickListener {
            startActivity(Intent(this, CreateStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMenu(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMenu(itemId: Int) {
        when (itemId) {
            R.id.action_logout -> {
                val builder = AlertDialog.Builder(this)
                val alert = builder.create()
                builder
                    .setTitle(getString(R.string.alertTittleLogout))
                    .setMessage(getString(R.string.alertMassageLogout))
                    .setPositiveButton(getString(R.string.positiveLogout)) { _, _ ->
                        mainViewModel.logout()
                    }
                    .setNegativeButton(getString(R.string.negativeLogout)) { _, _ ->
                        alert.cancel()
                    }
                    .show()
            }


            R.id.action_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
    }
}
