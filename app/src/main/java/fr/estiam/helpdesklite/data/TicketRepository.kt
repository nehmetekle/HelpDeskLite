package fr.estiam.helpdesklite.data

import fr.estiam.helpdesklite.model.Status
import fr.estiam.helpdesklite.model.SupportTicket
import kotlinx.coroutines.flow.Flow

interface TicketRepository {
    suspend fun add(ticket: SupportTicket)
    suspend fun findById(id: String): SupportTicket?
    suspend fun updateStatus(id: String, status: Status): Boolean
    fun observeAll(): Flow<List<SupportTicket>>
    fun observeByStatus(status: Status): Flow<List<SupportTicket>>
}
