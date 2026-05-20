package com.monakom.readyappclone.ui.home


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.monakom.readyappclone.R
import com.monakom.readyappclone.base.BaseActivity
import com.monakom.readyappclone.data.local.pref.SessionManager
import com.monakom.readyappclone.databinding.ActivityHomeBinding
import com.monakom.readyappclone.ui.home.adapter.TicketAdapter
import com.monakom.readyappclone.ui.setting.SettingActivity
import com.monakom.readyappclone.data.mqtt.MqttManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeActivity : BaseActivity() {

    //private val binding
    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    // ← ViewModel
    private val viewModel by viewModels<HomeViewModel>()

    // ← Separate adapter
    private val ticketAdapter by lazy {
        TicketAdapter(
            onReadyClick  = { ticket -> /* TODO: call Ready API */ },
            onRecallClick = { ticket -> /* TODO: call Re-call API */ }
        )
    }

    private var refreshJob: Job? = null
    private var selectedTabIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvStoreName.text = SessionManager.getCompanyName(this) ?: "Store"

        setupRecyclerView()
        setupListeners()
        observeViewModel()
        connectMqtt()

        // Load data
        viewModel.loadOrderTypes()
    }

    private fun setupRecyclerView() {
        binding.rvTickets.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = ticketAdapter
        }
    }

    private fun setupListeners() {
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }

    private fun observeViewModel() {
        // Observe order types → build tabs
        viewModel.orderTypes.observe(this) { types ->
            buildTabs(types)
            viewModel.loadTickets(null)
        }

        // Observe tickets → update adapter
        viewModel.tickets.observe(this) { tickets ->
            ticketAdapter.submitList(tickets)
            binding.tvEmpty.visibility =
                if (tickets.isEmpty()) View.VISIBLE else View.GONE
            binding.rvTickets.visibility =
                if (tickets.isEmpty()) View.GONE else View.VISIBLE
        }

        // Observe loading
        viewModel.isLoading.observe(this) { isLoading ->
            // TODO: show/hide progress bar
        }

        // Observe errors
        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildTabs(types: List<String>) {
        binding.tabContainer.removeAllViews()
        val allTypes = listOf("All") + types

        allTypes.forEachIndexed { index, type ->
            val tab = TextView(this).apply {
                text = type
                textSize = 13f
                setPadding(40, 16, 40, 16)
                val params = android.view.ViewGroup.MarginLayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.marginEnd = 8
                layoutParams = params

                setOnClickListener {
                    selectedTabIndex = index
                    val orderType = if (type == "All") null else type
                    viewModel.loadTickets(orderType)
                    updateTabsUI(index)
                }
            }
            binding.tabContainer.addView(tab)
        }
        updateTabsUI(0)
    }

    private fun updateTabsUI(selectedIndex: Int) {
        for (i in 0 until binding.tabContainer.childCount) {
            val child = binding.tabContainer.getChildAt(i) as TextView
            val isSelected = i == selectedIndex
            child.setTextColor(
                if (isSelected) 0xFFFFFFFF.toInt()
                else 0xFF374151.toInt()
            )
            child.background = ContextCompat.getDrawable(
                this,
                if (isSelected) R.drawable.bg_tab_selected
                else R.drawable.bg_tab_unselected
            )
        }
    }

    private fun connectMqtt() {
        val terminalId = SessionManager.getTerminalId(this) ?: return
        Thread { MqttManager.connectMqtt(terminalId) }.start()
        MqttManager.onMessageReceived = {
            runOnUiThread {
                viewModel.loadTickets(viewModel.currentOrderType)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshJob = lifecycleScope.launch {  // ← fixed: no androidx.lifecycle prefix needed
            while (true) {
                delay(30000)
                viewModel.loadTickets(viewModel.currentOrderType)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        refreshJob?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        MqttManager.disconnect()
    }
}