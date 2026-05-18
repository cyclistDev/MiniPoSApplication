package com.monakom.readyappclone.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.monakom.readyappclone.R
import com.monakom.readyappclone.base.BaseActivity
import com.monakom.readyappclone.data.SessionManager
import com.monakom.readyappclone.data.model.response.TicketData
import com.monakom.readyappclone.data.remote.RetrofitClient
import com.monakom.readyappclone.databinding.ActivityHomeBinding
import com.monakom.readyappclone.ui.setting.SettingActivity
import com.monakom.readyappclone.data.mqtt.MqttManager
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var ticketAdapter: TicketAdapter

    private var currentType: String? = null
    private var tabTypes = listOf<String>()
//    private var isLoadingTickets = false
    private var selectedTabIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvStoreName.text =
            SessionManager.getCompanyName(this) ?: "Store"

        setupRecyclerView()
        setupListeners()
        loadOrderTypes()
        connectMqtt()
    }

    private fun connectMqtt() {
        val terminalId = SessionManager.getTerminalId(this) ?: return

        Thread {
            MqttManager.connect(terminalId)
        }.start()

        // When new ticket arrives → refresh list
        MqttManager.onMessageReceived = { message ->
            runOnUiThread {
                android.util.Log.d("MQTT", "New ticket: $message")
                // Refresh tickets
                loadTickets(currentType)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MqttManager.disconnect()  // ← disconnect when leaving screen
    }

    private fun setupRecyclerView() {
        ticketAdapter = TicketAdapter()

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

    // ---------------------------
    // TAB BUILDER
    // ---------------------------
    private fun buildTabs(types: List<String>) {
        binding.tabContainer.removeAllViews()

        val allTypes = listOf("All") + types

        allTypes.forEachIndexed { index, type ->

            val tab = TextView(this).apply {
                text = type
                textSize = 13f
                setPadding(40, 16, 40, 16)

                val params = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.marginEnd = 8
                layoutParams = params

                setOnClickListener {
                    selectedTabIndex = index

                    currentType = if (type == "All") null else type
                    loadTickets(currentType)

                    updateTabsUI(index)
                }
            }

            binding.tabContainer.addView(tab)
        }

        // default select "All"
        updateTabsUI(0)
    }

    private fun updateTabsUI(selectedIndex: Int) {
        for (i in 0 until binding.tabContainer.childCount) {

            val child = binding.tabContainer.getChildAt(i) as TextView

            val isSelected = i == selectedIndex

            child.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (isSelected)
                        android.R.color.white
                    else
                        android.R.color.darker_gray
                )
            )

            child.background = ContextCompat.getDrawable(
                this,
                if (isSelected)
                    R.drawable.bg_tab_selected
                else
                    R.drawable.bg_tab_unselected
            )
        }
    }

    // ---------------------------
    // LOAD ORDER TYPES
    // ---------------------------
    private fun loadOrderTypes() {
        val token = SessionManager.getToken(this) ?: return
        val terminalId = SessionManager.getTerminalId(this) ?: return

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getOrderTypes(
                    token = token,
                    terminalId = terminalId
                )

                if (response.isSuccessful && response.body() != null) {

                    tabTypes = response.body()!!
                        .data
                        .map { it.trim() }
                        .distinct()

                    buildTabs(tabTypes)

                    // initial load
                    loadTickets(null)

                } else {
                    Toast.makeText(
                        this@HomeActivity,
                        "Failed to load tabs!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@HomeActivity,
                    "Network error!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // ---------------------------
    // LOAD TICKETS
    // ---------------------------
    private fun loadTickets(orderType: String?) {

//        if (isLoadingTickets) return
//        isLoadingTickets = true

        val token = SessionManager.getToken(this) ?: return
        val terminalId = SessionManager.getTerminalId(this) ?: return

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getTicketList(
                    token = token,
                    terminalId = terminalId,
//                    orderType = orderType
                )

                if (response.isSuccessful && response.body() != null) {

                    val allTickets = response.body()!!.data

                    val filtered = if (orderType == null) {
                        allTickets
                    } else {
                        allTickets.filter { it.orderType == orderType }
                    }
                    ticketAdapter.submitList(filtered)

                    binding.tvEmpty.visibility =
                        if (filtered.isEmpty()) View.VISIBLE else View.GONE

                    binding.rvTickets.visibility =
                        if (filtered.isEmpty()) View.GONE else View.VISIBLE

                } else {
                    Toast.makeText(
                        this@HomeActivity,
                        "Failed to load tickets!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@HomeActivity,
                    "Network error!",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            finally { isLoadingTickets = false }
        }
    }
}

// ===========================
// ADAPTER
// ===========================
class TicketAdapter : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    private var tickets = listOf<TicketData>()

    fun submitList(list: List<TicketData>) {
        tickets = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount() = tickets.size

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNumber = itemView.findViewById<TextView>(R.id.tvTicketNumber)
        private val tvStatus = itemView.findViewById<MaterialButton>(R.id.tvStatus)
        private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
        private val tvSource = itemView.findViewById<TextView>(R.id.tvSource)
        private val tvQty = itemView.findViewById<TextView>(R.id.tvQuantity)
        private val tvType = itemView.findViewById<TextView>(R.id.tvOrderType)

        fun bind(ticket: TicketData) {

            tvNumber.text = "Order #${ticket.ticketNumber}"
            tvTime.text = ticket.dateCreated.take(16).replace("T", " ")
            tvSource.text = ticket.destinationName
            tvQty.text = ticket.totalQty.toString()
            tvType.text = ticket.orderType

            val statusText = ticket.orderStatus

            tvStatus.setOnClickListener {

                if (!tvStatus.isEnabled) return@setOnClickListener

                val newStatus = when (ticket.orderStatus.uppercase()) {

                    "PENDING", "PREPARING" -> "READY"

                    "READY" -> "RE_CALL"

                    "RE_CALL" -> "DONE"

                    else -> "DONE"
                }

                Toast.makeText(
                    itemView.context,
                    "Change status → $newStatus (send to backend)",
                    Toast.LENGTH_SHORT
                ).show()
            }

            when (ticket.orderStatus.uppercase()) {

                "PENDING", "PREPARING" -> {
                    tvStatus.text = "READY"
                    tvStatus.isEnabled = true
                    tvStatus.setBackgroundColor(
                        itemView.context.getColor(android.R.color.holo_green_dark)
                    )
                }

                "READY" -> {
                    tvStatus.text = "SERVE"
                    tvStatus.isEnabled = true
                    tvStatus.setBackgroundColor(
                        itemView.context.getColor(android.R.color.holo_green_dark)
                    )
                }

                "RE_CALL" -> {
                    tvStatus.text = "RE-CALL"
                    tvStatus.isEnabled = true
                    tvStatus.setBackgroundColor(
                        itemView.context.getColor(android.R.color.holo_orange_light)
                    )
                }

                "DONE", "COMPLETED" -> {
                    tvStatus.text = "DONE"
                    tvStatus.isEnabled = false
                    tvStatus.setBackgroundColor(
                        itemView.context.getColor(android.R.color.darker_gray)
                    )
                }

                else -> {
                    tvStatus.text = ticket.orderStatus
                    tvStatus.isEnabled = false
                    tvStatus.setBackgroundColor(
                        itemView.context.getColor(android.R.color.darker_gray)
                    )
                }
            }
        }
    }
}