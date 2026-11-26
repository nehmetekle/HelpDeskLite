package fr.estiam.helpdesklite

import android.app.Application
import androidx.room.Room
import fr.estiam.helpdesklite.data.TicketRepository
import fr.estiam.helpdesklite.data.local.RoomTicketRepository
import fr.estiam.helpdesklite.data.local.TicketDatabase

class HelpDeskApplication : Application() {
    private val persistentRepository by lazy {
        val db = Room.databaseBuilder(this, TicketDatabase::class.java, "helpdesk.db").build()
        RoomTicketRepository(db.tickets())
    }

    // Instrumented UI tests install an in-memory repository before launching an activity.
    @Volatile internal var testRepository: TicketRepository? = null
    val tickets: TicketRepository get() = testRepository ?: persistentRepository
}
