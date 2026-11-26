package fr.estiam.helpdesklite.model

object TicketValidation {
    fun isTitleValid(title: String): Boolean {
        // TODO Exercise 1: Evaluate a title supplied by the form.
        return true
    }

    fun isDescriptionValid(description: String): Boolean {
        // TODO Exercise 1: Evaluate the description against the assessment's length rule.
        return true
    }

    fun create(
        id: String,
        title: String,
        description: String,
        priority: Priority,
        createdAt: Long,
    ): SupportTicket? {
        // TODO Exercise 1: Validate, normalize and set the initial status.
        return SupportTicket(id, title, description, priority, Status.IN_PROGRESS, createdAt)
    }
}
