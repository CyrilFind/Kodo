package com.cyrilfind.kodo.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrilfind.kodo.R
import com.cyrilfind.kodo.data.UserRepository
import com.cyrilfind.kodo.network.TokenResponse
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<TokenResponse>()
    val loginResult: LiveData<TokenResponse> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = userRepository.login(username, password)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean =
        username.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(username).matches()

    private fun isPasswordValid(password: String): Boolean = password.length > 5
}


