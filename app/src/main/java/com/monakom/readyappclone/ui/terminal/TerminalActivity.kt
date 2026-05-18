package com.monakom.readyappclone.ui.terminal

import com.monakom.readyappclone.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.monakom.readyappclone.base.BaseActivity
import com.monakom.readyappclone.data.SessionManager
import com.monakom.readyappclone.data.model.response.CompanyData
import com.monakom.readyappclone.data.model.response.TerminalData
import com.monakom.readyappclone.data.remote.RetrofitClient
import com.monakom.readyappclone.databinding.ActivityTerminalBinding
import com.monakom.readyappclone.ui.home.HomeActivity
import kotlinx.coroutines.launch

class TerminalActivity : BaseActivity() {

    private lateinit var binding: ActivityTerminalBinding

    private var userId: String = ""
    private var selectedCompanyCode: String = ""
    private var companyList = listOf<CompanyData>()
    private var terminalList = listOf<TerminalData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTerminalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnConnect.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        loadCompanies()
    }

    private fun loadCompanies() {
        val token = SessionManager.getToken(this) ?: return

        lifecycleScope.launch {
            try {
                // Step 1: Get userId
                val userInfoResponse = RetrofitClient.apiService.getUserInfo(token = token)

                if (userInfoResponse.isSuccessful && userInfoResponse.body() != null) {
                    userId = userInfoResponse.body()!!.data.id
                    SessionManager.saveUserId(this@TerminalActivity, userId)
                    // Step 2: Get companies
                    val companyResponse = RetrofitClient.apiService.getCompanies(
                        token = token,
                        userId = userId
                    )

                    if (companyResponse.isSuccessful && companyResponse.body() != null) {
                        companyList = companyResponse.body()!!.data
                        val names = companyList.map { it.companyName }
                        binding.actvCompany.setAdapter(
                            ArrayAdapter(this@TerminalActivity, android.R.layout.simple_dropdown_item_1line, names)

                        )
                    } else {
                        Toast.makeText(this@TerminalActivity, "Failed to load companies!", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                Toast.makeText(this@TerminalActivity, "Network error!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.actvCompany.setOnItemClickListener { _, _, position, _ ->
            val selectedCompany = companyList[position]  // ← fix: was companyId

            selectedCompanyCode = selectedCompany.companyCode  // ← now works!
            SessionManager.saveCompanyId(this, selectedCompany.id)
            SessionManager.saveCompanyCode(this, selectedCompanyCode)
            SessionManager.saveCompanyName(this, selectedCompany.companyName)
            loadTerminals(userId, selectedCompanyCode)
            checkFields()
        }
    }

    private fun loadTerminals(userId: String, companyCode: String) {  // ← fix: added params
        val token = SessionManager.getToken(this) ?: return
        binding.actvTerminal.setText("")

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getTerminals(
                    token = token,
                    userId = userId,
                    companyCode = companyCode  // ← now uses the parameter
                )

                if (response.isSuccessful && response.body() != null) {
                    terminalList = response.body()!!.data
                    val names = terminalList.map { it.terminalName }
                    binding.actvTerminal.setAdapter(
                        ArrayAdapter(this@TerminalActivity, android.R.layout.simple_dropdown_item_1line, names)

                    )
                } else {
                    Toast.makeText(this@TerminalActivity, "Failed to load terminals!", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@TerminalActivity, "Network error!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.actvTerminal.setOnItemClickListener { _, _, position, _ ->
            val selectedTerminal = terminalList[position]
            SessionManager.saveTerminalId(this, selectedTerminal.posTerminalId)
            checkFields()
        }
    }

    private fun checkFields() {
        binding.btnConnect.isEnabled =
            binding.actvCompany.text.isNotEmpty() && binding.actvTerminal.text.isNotEmpty()
    }
}