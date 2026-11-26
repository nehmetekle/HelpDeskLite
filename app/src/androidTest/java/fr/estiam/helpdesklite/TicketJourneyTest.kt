package fr.estiam.helpdesklite

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TicketJourneyTest {
    @get:Rule val fixture = TicketUiFixture()

    @Ignore("Exercise 5: replace this placeholder with your Espresso test")
    @Test fun createsLowPriorityTicket() {
        // TODO Exercise 5: Remove @Ignore and use Espresso to create a LOW priority ticket.
        // Wait for the catalogue update and check the title and priority within the same row.
    }
}
