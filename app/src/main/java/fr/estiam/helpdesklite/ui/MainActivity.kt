package fr.estiam.helpdesklite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.doOnAttach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import fr.estiam.helpdesklite.HelpDeskApplication
import fr.estiam.helpdesklite.R
import fr.estiam.helpdesklite.databinding.ActivityMainBinding
import fr.estiam.helpdesklite.model.Status
import fr.estiam.helpdesklite.model.TicketRules
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemPadding()
        val repository = (application as HelpDeskApplication).tickets
        val adapter = TicketAdapter { ticket, next ->
            lifecycleScope.launch { repository.updateStatus(ticket.id, next) }
        }
        binding.ticketList.layoutManager = LinearLayoutManager(this)
        binding.ticketList.adapter = adapter
        binding.statusFilter.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            listOf(getString(R.string.all_statuses)) + Status.entries.map { it.name },
        )
        binding.addTicket.setOnClickListener {
            startActivity(Intent(this, CreateTicketActivity::class.java))
        }

        // Wait until the filter is attached so its restored selection is applied normally.
        binding.statusFilter.doOnAttach {
            var observation: kotlinx.coroutines.Job? = null
            binding.statusFilter.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long,
                ) {
                    observation?.cancel()
                    val status = Status.entries.getOrNull(position - 1)
                    observation = lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            val source = if (status == null) repository.observeAll()
                            else repository.observeByStatus(status)
                            source.collect { tickets ->
                                adapter.submitList(TicketRules.sortByPriority(tickets))
                                binding.emptyMessage.isVisible = tickets.isEmpty()
                            }
                        }
                    }
                }
            }
        }
    }
}
