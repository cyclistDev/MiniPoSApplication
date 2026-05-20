package com.monakom.readyappclone.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.monakom.readyappclone.base.BaseActivity
import com.monakom.readyappclone.data.remote.dto.request.LoginRequest
import com.monakom.readyappclone.databinding.ActivityMainBinding
import com.monakom.readyappclone.ui.terminal.TerminalActivity
import com.monakom.readyappclone.data.local.pref.SessionManager
import com.monakom.readyappclone.data.remote.RetrofitClient

import kotlinx.coroutines.launch
import kotlin.getValue

class LoginActivity : BaseActivity() {

//    private lateinit var binding: ActivityMainBinding
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkSession()
        setupLogin()
    }

    private fun checkSession() {
        val token     = SessionManager.getToken(this)
        val terminalId = SessionManager.getTerminalId(this)

        when {
            // Fully logged in → go to Home directly
            token != null && terminalId != null -> {
                startActivity(Intent(this, TerminalActivity::class.java))
                finish()
            }
            // Logged in but no terminal selected yet
            token != null && terminalId == null -> {
                startActivity(Intent(this, TerminalActivity::class.java))
                finish()
            }
            // Not logged in → show login form
            else -> {}
        }
    }

    private fun setupLogin() {

        binding.btnLogin.isEnabled = false

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val u = binding.etUsername.text.toString().trim()
                val p = binding.etPassword.text.toString().trim()

                binding.btnLogin.isEnabled = u.isNotEmpty() && p.isNotEmpty()
            }
        }

        binding.etUsername.addTextChangedListener(watcher)
        binding.etPassword.addTextChangedListener(watcher)

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.authApi(this@LoginActivity).login(
                    LoginRequest(username, password)
                )

                if (response.isSuccessful) {
                    val body = response.body()!!
                    SessionManager.saveToken(
                        this@LoginActivity,
                        body.accessToken,
                        body.refreshToken
                    )

                    startActivity(Intent(this@LoginActivity, TerminalActivity::class.java))
                    finish()

                } else {
                    showError("Invalid credentials")
                }

            } catch (e: Exception) {
                showError("Network error")
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        binding.btnLogin.isEnabled = true
    }
}