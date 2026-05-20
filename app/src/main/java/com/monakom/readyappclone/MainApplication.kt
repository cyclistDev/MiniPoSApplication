package com.monakom.readyappclone

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.monakom.readyappclone.base.BaseActivity
import com.monakom.readyappclone.data.local.pref.SessionManager
import com.monakom.readyappclone.databinding.ActivityMainBinding
import com.monakom.readyappclone.ui.home.HomeActivity
import com.monakom.readyappclone.ui.login.LoginViewModel
import com.monakom.readyappclone.ui.terminal.TerminalActivity
import kotlin.getValue

class MainApplication : BaseActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkSession()
        observeViewModel()
    }

    private fun checkSession() {
        val token      = SessionManager.getToken(this)
        val terminalId = SessionManager.getTerminalId(this)

        when {
            token != null && terminalId != null -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            token != null && terminalId == null -> {
                startActivity(Intent(this, TerminalActivity::class.java))
                finish()
            }
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
            viewModel.login(username, password)
        }
    }

    private fun observeViewModel() {
        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                startActivity(Intent(this, TerminalActivity::class.java))
                finish()
            }
        }

        // Loading
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility =
                if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
        }

        // Error
        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            binding.btnLogin.isEnabled = true
        }
    }
}