package fr.estiam.helpdesklite.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.estiam.helpdesklite.model.Status
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Insert
    suspend fun insert(row: TicketRow)

    @Query("SELECT * FROM tickets WHERE id = :id")
    suspend fun findById(id: String): TicketRow?

    @Query("UPDATE tickets SET status = :next WHERE id = :id AND status = :previous")
    suspend fun updateStatus(id: String, previous: Status, next: Status): Int

    @Query("SELECT * FROM tickets ORDER BY createdAt DESC, id ASC")
    fun observeAll(): Flow<List<TicketRow>>

    @Query("SELECT * FROM tickets WHERE status = :status ORDER BY createdAt DESC, id ASC")
    fun observeByStatus(status: Status): Flow<List<TicketRow>>
}
