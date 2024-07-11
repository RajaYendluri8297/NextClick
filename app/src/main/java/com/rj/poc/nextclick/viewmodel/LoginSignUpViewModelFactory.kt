package com.rj.poc.nextclick.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rj.poc.nextclick.model.Repository

class LoginSignUpViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginSignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginSignUpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
