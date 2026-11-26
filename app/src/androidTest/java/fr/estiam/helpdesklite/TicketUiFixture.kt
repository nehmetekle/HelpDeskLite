package fr.estiam.helpdesklite

import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import fr.estiam.helpdesklite.data.local.RoomTicketRepository
import fr.estiam.helpdesklite.data.local.TicketDatabase
import fr.estiam.helpdesklite.ui.MainActivity
import org.junit.Assert.assertTrue
import org.junit.rules.ExternalResource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/** Provides isolated data and waits for asynchronous Room/RecyclerView updates. */
class TicketUiFixture : ExternalResource() {
    private lateinit var app: HelpDeskApplication
    private lateinit var db: TicketDatabase
    private lateinit var catalogue: ActivityScenario<MainActivity>

    override fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        app = context.applicationContext as HelpDeskApplication
        db = Room.inMemoryDatabaseBuilder(context, TicketDatabase::class.java).build()
        app.testRepository = RoomTicketRepository(db.tickets())
        try {
            catalogue = ActivityScenario.launch(MainActivity::class.java)
        } catch (failure: Throwable) {
            app.testRepository = null
            db.close()
            throw failure
        }
    }

    fun awaitCatalogueRows(expectedCount: Int = 1) {
        val ready = CountDownLatch(1)
        lateinit var adapter: RecyclerView.Adapter<*>
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() = checkRows()
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = checkRows()
            fun checkRows() {
                if (adapter.itemCount >= expectedCount) ready.countDown()
            }
        }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        lateinit var activity: MainActivity
        catalogue.onActivity { activity = it }
        instrumentation.runOnMainSync {
            adapter = requireNotNull(activity.findViewById<RecyclerView>(R.id.ticketList).adapter)
            adapter.registerAdapterDataObserver(observer)
            observer.checkRows()
        }
        try {
            assertTrue("Expected $expectedCount ticket row(s) in the catalogue within 10 seconds",
                ready.await(10, TimeUnit.SECONDS))
        } finally {
            instrumentation.runOnMainSync { adapter.unregisterAdapterDataObserver(observer) }
        }
    }

    override fun after() {
        try {
            // If a test fails on the form, finish that activity before closing its database.
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            instrumentation.runOnMainSync {
                val registry = androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry.getInstance()
                val stages = listOf(
                    androidx.test.runner.lifecycle.Stage.RESUMED,
                    androidx.test.runner.lifecycle.Stage.PAUSED,
                    androidx.test.runner.lifecycle.Stage.STOPPED,
                )
                stages.flatMap { registry.getActivitiesInStage(it) }
                    .filterIsInstance<fr.estiam.helpdesklite.ui.CreateTicketActivity>()
                    .forEach { it.finish() }
            }
        } finally {
            try {
                catalogue.close()
            } finally {
                app.testRepository = null
                db.close()
            }
        }
    }
}
