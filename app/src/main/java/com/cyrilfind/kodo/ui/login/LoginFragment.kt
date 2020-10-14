package com.cyrilfind.kodo.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.edit
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.cyrilfind.kodo.Constants.TOKEN_PREF_KEY
import com.cyrilfind.kodo.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val loginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel.loginFormState.observe(this, Observer { loginState ->
            login.isEnabled = loginState.isDataValid
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this, Observer { response ->
            loading.visibility = View.GONE
            if (response != null) {
                PreferenceManager.getDefaultSharedPreferences(context).edit {
                    putString(TOKEN_PREF_KEY, response.token)
                }
                findNavController().navigate(R.id.tasksListFragment)
            }
        })

        username.doAfterTextChanged { loginDataChanged() }

        password.apply {
            doAfterTextChanged { loginDataChanged() }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) login()
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                login()
            }

            signup.setOnClickListener {
                //  findNavController().navigate(R.id.signupFragment)
            }
        }
    }

    private fun login() {
        loginViewModel.login(username.text.toString(), password.text.toString())
    }

    private fun loginDataChanged() {
        loginViewModel.loginDataChanged(username.text.toString(), password.text.toString())
    }

}