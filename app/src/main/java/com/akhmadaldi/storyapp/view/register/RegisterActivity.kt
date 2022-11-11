package com.akhmadaldi.storyapp.view.register

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
import com.akhmadaldi.storyapp.R
import com.akhmadaldi.storyapp.util.ViewModelFactory
import com.akhmadaldi.storyapp.data.model.UserPreference
import com.akhmadaldi.storyapp.data.network.auth.RegisterResult
import com.akhmadaldi.storyapp.databinding.ActivityRegisterBinding
import com.akhmadaldi.storyapp.util.isValidEmail
import com.akhmadaldi.storyapp.view.login.LoginActivity


class RegisterActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupAction()
        playAnimation()
    }


    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]

        registerViewModel.errorMessage.observe(this) {
            when (it) {
                "User created" -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.alertTitle))
                        setMessage(getString(R.string.alertMessageRegister))
                        setPositiveButton(getString(R.string.alertPositiveRegister)) { _, _ ->
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
                "onFailure" -> {
                    Toast.makeText(this@RegisterActivity, getString(R.string.failureMessage), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.emailEditTextLayout.error = getString(R.string.emailError)
                }
            }
        }

        registerViewModel.loading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            binding.nameEditTextLayout.isErrorEnabled = false
            binding.emailEditTextLayout.isErrorEnabled = false

            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = getString(R.string.nameEmptyError)
                }
                email.isEmpty() -> {
                    binding.edRegisterEmail.error = getString(R.string.emailEmptyError)
                }
                !isValidEmail(email) -> {
                    binding.edRegisterEmail.error = getString(R.string.emailFormatError)
                }
                password.isEmpty() -> {
                    binding.edRegisterPassword.setError(getString(R.string.passwordEmptyError), null)
                }
                password.length < 6 -> {
                    binding.edRegisterPassword.setError(getString(R.string.passwordLengthError), null)
                }
                else -> {
                    registerViewModel.registerUser(RegisterResult(name, email, password))
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
        ObjectAnimator.ofFloat(binding.imageView2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.Title, View.ALPHA, 1f).setDuration(600)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(600)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(600)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(600)
        val register = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(600)

        AnimatorSet().apply {
            playSequentially(title, nameEdit, emailEdit, passwordEdit, register)
            start()
        }
    }
}