package fr.estiam.helpdesklite.data.local

import fr.estiam.helpdesklite.data.TicketRepository
import fr.estiam.helpdesklite.model.Status
import fr.estiam.helpdesklite.model.SupportTicket
import fr.estiam.helpdesklite.model.TicketRules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class RoomTicketRepository(
    private val dao: TicketDao,
    private val canTransition: (Status, Status) -> Boolean = TicketRules::canTransition,
) : TicketRepository {
    override suspend fun add(ticket: SupportTicket) = dao.insert(ticket.toRow())

    override suspend fun findById(id: String): SupportTicket? {
        // TODO Exercise 3: Retrieve and map a matching row.
        return null
    }

    override suspend fun updateStatus(id: String, status: Status): Boolean {
        // TODO Exercise 3: Use the supplied policy and DAO methods to persist a valid change.
        return false
    }

    override fun observeAll() = dao.observeAll().map { rows -> rows.map(TicketRow::toTicket) }

    override fun observeByStatus(status: Status): Flow<List<SupportTicket>> {
        // TODO Exercise 3: Expose the matching rows as domain tickets.
        return flowOf(emptyList())
    }
}
