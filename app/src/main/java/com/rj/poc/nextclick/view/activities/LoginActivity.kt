package com.rj.poc.nextclick.view.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rj.poc.nextclick.R
import com.rj.poc.nextclick.databinding.ActivityMainBinding
import com.rj.poc.nextclick.model.Repository
import com.rj.poc.nextclick.model.data.LoginRequestModel
import com.rj.poc.nextclick.model.data.RegistrationRequestModel
import com.rj.poc.nextclick.viewmodel.LoginSignUpViewModel
import com.rj.poc.nextclick.viewmodel.LoginSignUpViewModelFactory
import com.rj.poc.nextclick.viewmodel.utils.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val loginViewModel: LoginSignUpViewModel by viewModels {
        LoginSignUpViewModelFactory(Repository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUIState(isLogin = true)
        setListeners()
        setClickListeners()
        setObservers()
    }

    private fun setObservers() {
        loginViewModel.loginResponse.observe(this) { response ->
            when (response) {
                is ApiResponse.Loading -> showToast(getString(R.string.loading))
                is ApiResponse.Success -> handleLoginSuccess()
                is ApiResponse.Error -> showToast(getString(R.string.login_failed))
            }
        }
        loginViewModel.registrationResponse.observe(this) { response ->
            when (response) {
                is ApiResponse.Loading -> showToast(getString(R.string.loading))
                is ApiResponse.Success -> {
                    showToast(getString(R.string.registration_success_please_login))
                    setUIState(true)
                }
                is ApiResponse.Error -> showToast(getString(R.string.registartion_failed))
            }
        }
    }

    private fun handleLoginSuccess() {
        showToast("Login Success")
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun setClickListeners() {
        binding.loginButton.setOnClickListener {
            it.hideKeyboard()
            handleLoginButtonClick()
        }

        binding.tvRegister.setOnClickListener {
            toggleUIState()
        }
    }

    private fun handleLoginButtonClick() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        when (binding.loginButton.text) {
            getString(R.string.login) -> handleLogin(email, password)
            getString(R.string.register) -> handleRegister()
        }
    }

    private fun handleLogin(email: String, password: String) {
        if (validateLoginInputs(email, password)) {
            val loginRequestModel = LoginRequestModel(EMAIL, PASSWORD)
            loginViewModel.userLogin(loginRequestModel)
        }
    }

    private fun handleRegister() {
        val username = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        if (validateRegisterInputs(username, email, password, confirmPassword)) {
            val requestModel = RegistrationRequestModel(EMAIL,PASSWORD)
            loginViewModel.userRegistration(requestModel)
        }
    }

    private fun validateLoginInputs(email: String, password: String): Boolean {
        var isValid = true

        if (email.isBlank()) {
            binding.emailTextInputLayout.error = getString(R.string.enter_email)
            isValid = false
        } else if (!email.isEmailValid()) {
            binding.emailTextInputLayout.error = getString(R.string.enter_valid_email)
            isValid = false
        } else {
            binding.emailTextInputLayout.error = null
        }

        if (password.isBlank()) {
            binding.passwordTextInputLayout.error = getString(R.string.enter_password)
            isValid = false
        } else if (password.length < 6) {
            binding.passwordTextInputLayout.error = getString(R.string.password_length_should_be_greater_than_6)
            isValid = false
        } else {
            binding.passwordTextInputLayout.error = null
        }

        return isValid
    }

    private fun validateRegisterInputs(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (username.isBlank()) {
            binding.usernameTextInputLayout.error = getString(R.string.enter_username)
            isValid = false
        } else if (username.length < 6) {
            binding.usernameTextInputLayout.error =
                getString(R.string.username_length_should_be_greater_than_6)
            isValid = false
        } else {
            binding.usernameTextInputLayout.error = null
        }

        if (email.isBlank()) {
            binding.emailTextInputLayout.error = getString(R.string.enter_email)
            isValid = false
        } else if (!email.isEmailValid()) {
            binding.emailTextInputLayout.error = getString(R.string.enter_valid_email)
            isValid = false
        } else {
            binding.emailTextInputLayout.error = null
        }

        if (password.isBlank()) {
            binding.passwordTextInputLayout.error = getString(R.string.enter_password)
            isValid = false
        } else if (password.length < 6) {
            binding.passwordTextInputLayout.error =
                getString(R.string.password_length_should_be_greater_than_6)
            isValid = false
        } else {
            binding.passwordTextInputLayout.error = null
        }

        if (confirmPassword.isBlank()) {
            binding.confirmPasswordTextInputLayout.error = getString(R.string.enter_confirm_password)
            isValid = false
        } else if (confirmPassword != password) {
            binding.confirmPasswordTextInputLayout.error = getString(R.string.password_not_matched)
            isValid = false
        } else {
            binding.confirmPasswordTextInputLayout.error = null
        }

        return isValid
    }

    private fun setListeners() {
        binding.usernameEditText.addTextChangedListener(createTextWatcher { binding.usernameTextInputLayout.error = null })
        binding.emailEditText.addTextChangedListener(createTextWatcher { binding.emailTextInputLayout.error = null })
        binding.passwordEditText.addTextChangedListener(createTextWatcher { binding.passwordTextInputLayout.error = null })
        binding.confirmPasswordEditText.addTextChangedListener(createTextWatcher { binding.confirmPasswordTextInputLayout.error = null })
    }

    private fun createTextWatcher(onTextChangedAction: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChangedAction()
            }
            override fun afterTextChanged(s: Editable?) = Unit
        }
    }

    private fun setUIState(isLogin: Boolean) {
        if (isLogin) {
            binding.usernameEditText.text?.clear()
            binding.emailEditText.text?.clear()
            binding.passwordEditText.text?.clear()
            binding.confirmPasswordEditText.text?.clear()
            binding.usernameTextInputLayout.hide()
            binding.confirmPasswordTextInputLayout.hide()
            binding.loginButton.text = getString(R.string.login)
            binding.tvWelcome.text = getString(R.string.welcome_login)
            binding.tvRegister.text = getString(R.string.dont_have_account)
        } else {
            binding.emailEditText.text?.clear()
            binding.passwordEditText.text?.clear()
            binding.usernameTextInputLayout.show()
            binding.confirmPasswordTextInputLayout.show()
            binding.loginButton.text = getString(R.string.register)
            binding.tvWelcome.text = getString(R.string.welcome_register)
            binding.tvRegister.text = getString(R.string.already_have_account)
        }
    }

    private fun toggleUIState() {
        when (binding.loginButton.text) {
            getString(R.string.login) -> setUIState(isLogin = false)
            getString(R.string.register) -> setUIState(isLogin = true)
        }
    }
}
