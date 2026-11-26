package fr.estiam.helpdesklite

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.estiam.helpdesklite.data.local.*
import fr.estiam.helpdesklite.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomTicketRepositoryTest {
    private lateinit var db: TicketDatabase
    private lateinit var repository: RoomTicketRepository
    private val printer = SupportTicket("p", "Printer", "Printer unavailable", Priority.LOW, Status.OPEN, 1)

    @Before fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, TicketDatabase::class.java).build()
        repository = RoomTicketRepository(db.tickets()) { from, to ->
            // This fixture isolates repository mechanics from the Exercise 2 implementation.
            from == printer.status && to == Status.IN_PROGRESS
        }
    }

    @After fun tearDown() {
        db.close()
    }

    @Test fun retrievesPersistedTicketAndReturnsNullForMissingId() = runBlocking {
        repository.add(printer)
        assertEquals(printer, repository.findById(printer.id))
        assertNull(repository.findById("missing"))
    }

    @Test fun changesOnlyStatusAndRejectsInvalidTransitions() = runBlocking {
        repository.add(printer)
        assertFalse(repository.updateStatus(printer.id, Status.RESOLVED))
        assertEquals(printer, db.tickets().findById(printer.id)?.toTicket())
        assertTrue(repository.updateStatus(printer.id, Status.IN_PROGRESS))
        assertEquals(printer.copy(status = Status.IN_PROGRESS), db.tickets().findById(printer.id)?.toTicket())
        assertFalse(repository.updateStatus("missing", Status.IN_PROGRESS))
    }

    @Test fun filtersByRequestedStatus() = runBlocking {
        val display = printer.copy(id = "d", title = "Display", status = Status.RESOLVED)
        repository.add(printer)
        repository.add(display)
        assertEquals(listOf(printer), repository.observeByStatus(Status.OPEN).first())
        assertEquals(listOf(display), repository.observeByStatus(Status.RESOLVED).first())
        assertTrue(repository.observeByStatus(Status.IN_PROGRESS).first().isEmpty())
    }
}
