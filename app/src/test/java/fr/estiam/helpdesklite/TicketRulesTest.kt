package fr.estiam.helpdesklite

import fr.estiam.helpdesklite.model.*
import org.junit.Assert.*
import org.junit.Test

class TicketRulesTest {
    @Test fun allowsOnlyForwardSteps() {
        assertTrue(TicketRules.canTransition(Status.OPEN, Status.IN_PROGRESS))
        assertTrue(TicketRules.canTransition(Status.IN_PROGRESS, Status.RESOLVED))
        assertFalse(TicketRules.canTransition(Status.OPEN, Status.RESOLVED))
        assertFalse(TicketRules.canTransition(Status.IN_PROGRESS, Status.OPEN))
        assertFalse(TicketRules.canTransition(Status.RESOLVED, Status.RESOLVED))
    }

    @Test fun sortsHighFirstAndPreservesTies() {
        val low = ticket("a", Priority.LOW)
        val high = ticket("b", Priority.HIGH)
        val medium = ticket("c", Priority.MEDIUM)
        val otherHigh = ticket("d", Priority.HIGH)
        assertEquals(listOf(high, otherHigh, medium, low),
            TicketRules.sortByPriority(listOf(low, high, medium, otherHigh)))
        assertEquals(emptyList<SupportTicket>(), TicketRules.sortByPriority(emptyList()))
    }

    private fun ticket(id: String, priority: Priority) =
        SupportTicket(id, "Ticket", "Ticket description", priority, Status.OPEN, 0)
}
