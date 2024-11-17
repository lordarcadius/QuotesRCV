package com.vipuljha.quotesrcv.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.vipuljha.quotesrcv.R
import com.vipuljha.quotesrcv.data.repositories.Repositories
import com.vipuljha.quotesrcv.databinding.ActivityMainBinding
import com.vipuljha.quotesrcv.utils.Resource
import com.vipuljha.quotesrcv.viewmodels.QuotesViewModel
import com.vipuljha.quotesrcv.viewmodels.QuotesViewModelProviderFactory
import com.vipuljha.quotesrcv.views.adapters.QuotesAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: QuotesViewModel by viewModels {
        QuotesViewModelProviderFactory(Repositories())
    }
    private val quotesAdapter by lazy { QuotesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        applyWindowInsets()

        binding.apply {
            toolbar.setNavigationOnClickListener {
                drawerLayout.open()
            }
            navigationView.setNavigationItemSelectedListener { menuItem ->
                menuItem.isChecked = true

                when (menuItem.itemId) {
                    R.id.nav_menu_home -> showToast("Home")
                    R.id.nav_menu_explore -> showToast("Explore")
                    R.id.nav_menu_subscription -> showToast("Subscriptions")
                    R.id.nav_menu_library -> showToast("Library")
                    R.id.nav_menu_settings -> showToast("Settings")
                    R.id.nav_menu_account -> showToast("Account")
                }
                return@setNavigationItemSelectedListener true
            }
        }

        setRecyclerView()
        setupAdapterClickListener()
        setupObservers()

        // Trigger the API call after setting up observers and UI
        viewModel.getQuotes()
    }

    private fun setRecyclerView() {
        binding.rcvQuotes.apply {
            adapter = quotesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupAdapterClickListener() {
        quotesAdapter.setOnItemClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Quote Details")
            builder.setMessage(it.quote)

            builder.setPositiveButton(
                "Ok"
            ) { dialog, _ -> dialog?.dismiss() }

            builder.show()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.quotes.collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            toggleProgressBar(true)
                        }

                        is Resource.Success -> {
                            toggleProgressBar(false)
                            response.data?.let {
                                quotesAdapter.submitList(it.quotes)
                            }
                        }

                        is Resource.Error -> {
                            toggleProgressBar(false)
                            response.message?.let {
                                showToast(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun toggleProgressBar(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.rcvQuotes.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun applyWindowInsets() {
        // Edge-to-edge layout handling
        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Set padding to the root view
            view.updatePadding(
                left = systemBars.left,
                right = systemBars.right,
                bottom = systemBars.bottom
            )

            // Set top padding specifically to the AppBar
            binding.appBar.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}