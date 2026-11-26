package fr.estiam.helpdesklite.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TicketRow::class], version = 1, exportSchema = false)
abstract class TicketDatabase : RoomDatabase() {
    abstract fun tickets(): TicketDao
}
