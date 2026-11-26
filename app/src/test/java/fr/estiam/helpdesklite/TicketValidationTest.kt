package fr.estiam.helpdesklite

import fr.estiam.helpdesklite.model.*
import org.junit.Assert.*
import org.junit.Test

class TicketValidationTest {
    @Test fun rejectsBlankTitle() {
        assertFalse(TicketValidation.isTitleValid(""))
        assertFalse(TicketValidation.isTitleValid(" \t\n"))
        assertTrue(TicketValidation.isTitleValid(" A "))
    }

    @Test fun countsOnlyTrimmedDescription() {
        assertFalse(TicketValidation.isDescriptionValid(" 123456789 "))
        assertFalse(TicketValidation.isDescriptionValid("          "))
        assertTrue(TicketValidation.isDescriptionValid(" 1234567890 "))
    }

    @Test fun factoryRejectsInvalidInputAndCreatesOpenTicket() {
        assertNull(TicketValidation.create("x", " ", "1234567890", Priority.LOW, 5))
        assertNull(TicketValidation.create("x", "Display", "short", Priority.LOW, 5))
        assertEquals(
            SupportTicket("x", "Display", "1234567890", Priority.HIGH, Status.OPEN, 5),
            TicketValidation.create("x", " Display ", " 1234567890 ", Priority.HIGH, 5),
        )
    }
}
