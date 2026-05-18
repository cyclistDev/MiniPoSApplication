package com.monakom.readyappclone.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.monakom.readyappclone.base.BaseActivity
import com.monakom.readyappclone.data.SessionManager
import com.monakom.readyappclone.data.model.request.LoginRequest
import com.monakom.readyappclone.data.remote.RetrofitClient
import com.monakom.readyappclone.databinding.ActivityMainBinding
import com.monakom.readyappclone.ui.home.HomeActivity
import com.monakom.readyappclone.ui.terminal.TerminalActivity
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ← Check session first before showing login
        checkSession()
    }

    private fun checkSession() {
        val token      = SessionManager.getToken(this)
        val terminalId = SessionManager.getTerminalId(this)

        when {
            // Fully logged in → go to Home directly
            token != null && terminalId != null -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            // Logged in but no terminal selected yet
            token != null && terminalId == null -> {
                startActivity(Intent(this, TerminalActivity::class.java))
                finish()
            }
            // Not logged in → show login form
            else -> setupLogin()
        }
    }

    private fun setupLogin() {
        binding.btnLogin.isEnabled = false

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val username = binding.etUsername.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()
                binding.btnLogin.isEnabled = username.isNotEmpty() && password.isNotEmpty()
            }
        }

        binding.etUsername.addTextChangedListener(watcher)
        binding.etPassword.addTextChangedListener(watcher)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            callLoginApi(username, password)
        }
    }

    private fun callLoginApi(clientId: String, clientSecret: String) {
        binding.btnLogin.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.login(
                    LoginRequest(clientId = clientId, clientSecret = clientSecret)
                )

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    SessionManager.saveToken(this@MainActivity, body.accessToken, body.refreshToken)
                    Toast.makeText(this@MainActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, TerminalActivity::class.java))
                    finish()  // ← close login so user can't go back
                } else {
                    Toast.makeText(this@MainActivity, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                    binding.btnLogin.isEnabled = true
                }

            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Network error!", Toast.LENGTH_SHORT).show()
                binding.btnLogin.isEnabled = true
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}