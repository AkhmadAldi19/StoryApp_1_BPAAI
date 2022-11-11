package com.akhmadaldi.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.akhmadaldi.storyapp.view.main.MainActivity
import com.akhmadaldi.storyapp.R
import com.akhmadaldi.storyapp.util.ViewModelFactory
import com.akhmadaldi.storyapp.data.model.UserPreference
import com.akhmadaldi.storyapp.databinding.ActivityLoginBinding
import com.akhmadaldi.storyapp.util.isValidEmail

class LoginActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.errorMessage.observe(this) {
            when (it) {
                "success" -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.alertTitle))
                        setMessage(R.string.alertMessage)
                        setPositiveButton(R.string.alertPositive) { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }

                "onFailure" -> {
                    Toast.makeText(this@LoginActivity, getString(R.string.failureMessage), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@LoginActivity, getString(R.string.wrongEmailPassword), Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginViewModel.loading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupAction() {

        binding.loginButton.setOnClickListener {


            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = getString(R.string.emailEmptyError)
                }
                password.isEmpty() -> {
                    binding.edLoginPassword.setError(getString(R.string.passwordEmptyError), null)
                }
                !isValidEmail(email) -> {
                    binding.edLoginEmail.error = getString(R.string.emailFormatError)
                }
                password.length < 6 -> {
                    binding.edLoginPassword.setError(getString(R.string.passwordLengthError), null)
                }
                else -> {
                    loginViewModel.login(email, password)
                }
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.Title, View.ALPHA, 1f).setDuration(400)
        val message = ObjectAnimator.ofFloat(binding.descTitle, View.ALPHA, 1f).setDuration(400)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(400)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(400)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(title, message, emailEdit, passwordEdit, login)
            start()
        }
    }
}