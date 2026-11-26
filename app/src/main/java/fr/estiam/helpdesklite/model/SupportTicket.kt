package fr.estiam.helpdesklite.model

enum class Priority { LOW, MEDIUM, HIGH }
enum class Status { OPEN, IN_PROGRESS, RESOLVED }

data class SupportTicket(
    val id: String,
    val title: String,
    val description: String,
    val priority: Priority,
    val status: Status,
    val createdAt: Long,
)
