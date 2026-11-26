package fr.estiam.helpdesklite.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.estiam.helpdesklite.model.Priority
import fr.estiam.helpdesklite.model.Status
import fr.estiam.helpdesklite.model.SupportTicket

@Entity(tableName = "tickets")
data class TicketRow(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val priority: Priority,
    val status: Status,
    val createdAt: Long,
) {
    fun toTicket() = SupportTicket(id, title, description, priority, status, createdAt)
}

fun SupportTicket.toRow() = TicketRow(id, title, description, priority, status, createdAt)
