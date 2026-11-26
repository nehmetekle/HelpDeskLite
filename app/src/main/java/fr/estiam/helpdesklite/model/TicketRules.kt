package fr.estiam.helpdesklite.model

object TicketRules {
    private val priorityOrder = listOf(Priority.HIGH, Priority.MEDIUM, Priority.LOW)

    fun canTransition(from: Status, to: Status): Boolean =
        (from == Status.OPEN && to == Status.IN_PROGRESS) ||
            (from == Status.IN_PROGRESS && to == Status.RESOLVED)

    /** Returns a new list ordered HIGH, MEDIUM, LOW; `sortedBy` is stable, so ties keep their order. */
    fun sortByPriority(tickets: List<SupportTicket>): List<SupportTicket> =
        tickets.sortedBy { priorityOrder.indexOf(it.priority) }
}
