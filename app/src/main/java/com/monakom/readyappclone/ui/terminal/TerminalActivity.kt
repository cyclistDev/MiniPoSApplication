package com.monakom.readyappclone.ui.terminal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.monakom.readyappclone.base.BaseActivity
import com.monakom.readyappclone.databinding.ActivityTerminalBinding
import com.monakom.readyappclone.ui.home.HomeActivity

class TerminalActivity : BaseActivity() {

    private val binding by lazy {
        ActivityTerminalBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<TerminalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()

        // Load companies on start
        viewModel.loadCompanies()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnConnect.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        binding.actvCompany.setOnItemClickListener { _, _, position, _ ->
            val companies = viewModel.companies.value ?: return@setOnItemClickListener
            viewModel.selectCompany(companies[position])
            binding.actvTerminal.setText("")
            binding.btnConnect.isEnabled = false
        }

        binding.actvTerminal.setOnItemClickListener { _, _, position, _ ->
            val terminals = viewModel.terminals.value ?: return@setOnItemClickListener
            viewModel.selectTerminal(terminals[position])
        }
    }

    private fun observeViewModel() {
        // Companies loaded → set adapter
        viewModel.companies.observe(this) { companies ->
            val names = companies.map { it.companyName }
            binding.actvCompany.setAdapter(
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, names)
            )
        }

        // Terminals loaded → set adapter
        viewModel.terminals.observe(this) { terminals ->
            val names = terminals.map { it.terminalName }
            binding.actvTerminal.setAdapter(
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, names)
            )
        }

        // Connect ready
        viewModel.connectSuccess.observe(this) { ready ->
            binding.btnConnect.isEnabled = ready
        }

        // Loading
        viewModel.isLoading.observe(this) { isLoading ->
            // TODO: show/hide progress bar
        }

        // Error
        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
}