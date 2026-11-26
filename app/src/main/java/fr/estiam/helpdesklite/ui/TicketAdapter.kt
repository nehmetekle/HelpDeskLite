package fr.estiam.helpdesklite.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.estiam.helpdesklite.R
import fr.estiam.helpdesklite.databinding.ItemTicketBinding
import fr.estiam.helpdesklite.model.Status
import fr.estiam.helpdesklite.model.SupportTicket
import fr.estiam.helpdesklite.model.TicketRules

class TicketAdapter(private val onAdvance: (SupportTicket, Status) -> Unit) :
    ListAdapter<SupportTicket, TicketAdapter.Holder>(TicketDiff) {

    class Holder(val binding: ItemTicketBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val ticket = getItem(position)
        with(holder.binding) {
            ticketTitle.text = ticket.title
            ticketDescription.text = ticket.description
            ticketPriority.text = ticket.priority.name
            ticketStatus.text = ticket.status.name
            val next = Status.entries.firstOrNull { TicketRules.canTransition(ticket.status, it) }
            advanceStatus.isVisible = next != null
            advanceStatus.setText(if (next == Status.IN_PROGRESS) R.string.start_ticket else R.string.resolve_ticket)
            advanceStatus.setOnClickListener { if (next != null) onAdvance(ticket, next) }
        }
    }

    private object TicketDiff : DiffUtil.ItemCallback<SupportTicket>() {
        override fun areItemsTheSame(old: SupportTicket, new: SupportTicket) = old.id == new.id
        override fun areContentsTheSame(old: SupportTicket, new: SupportTicket) = old == new
    }
}
