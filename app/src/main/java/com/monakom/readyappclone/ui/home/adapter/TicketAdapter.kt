package com.monakom.readyappclone.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.monakom.readyappclone.databinding.ItemTicketBinding
import com.monakom.readyappclone.data.remote.dto.response.TicketData
import com.monakom.readyappclone.utils.enum.OrderStatus

class TicketAdapter(
    private val onReadyClick: (TicketData) -> Unit,
    private val onRecallClick: (TicketData) -> Unit
) : ListAdapter<TicketData, TicketAdapter.TicketViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemTicketBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TicketViewHolder(
        private val binding: ItemTicketBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: TicketData) {
            binding.tvTicketNumber.text = "Order #${ticket.ticketNumber}"
            binding.tvTime.text = ticket.dateCreated.take(16).replace("T", " ")
            binding.tvSource.text = ticket.destinationName
            binding.tvQuantity.text = ticket.totalQty.toString()
            binding.tvOrderType.text = ticket.orderType

            val status = OrderStatus.fromValue(ticket.orderStatus)

            when (status) {
                OrderStatus.PREPARING -> {
                    binding.tvStatus.text = "Ready"
                    binding.tvStatus.isEnabled = true
                    binding.tvStatus.setBackgroundColor(
                        binding.root.context.getColor(android.R.color.holo_green_dark)
                    )
                    binding.tvStatus.setOnClickListener {
                        onReadyClick(ticket)
                    }
                }
                OrderStatus.RE_CALL -> {
                    binding.tvStatus.text = "Re-call"
                    binding.tvStatus.isEnabled = true
                    binding.tvStatus.setBackgroundColor(
                        binding.root.context.getColor(android.R.color.holo_orange_light)
                    )
                    binding.tvStatus.setOnClickListener {
                        onRecallClick(ticket)
                    }
                }
                else -> {
                    binding.tvStatus.text = status.value
                    binding.tvStatus.isEnabled = false
                    binding.tvStatus.setBackgroundColor(
                        binding.root.context.getColor(android.R.color.darker_gray)
                    )
                    binding.tvStatus.setOnClickListener(null)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TicketData>() {
        override fun areItemsTheSame(old: TicketData, new: TicketData) = old.id == new.id
        override fun areContentsTheSame(old: TicketData, new: TicketData) = old == new
    }
}