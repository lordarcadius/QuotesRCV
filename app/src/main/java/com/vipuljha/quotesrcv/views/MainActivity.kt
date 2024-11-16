package com.vipuljha.quotesrcv.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
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
            Toast.makeText(this, it.quote, Toast.LENGTH_SHORT).show()
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
                                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                left = systemBars.left,
                top = systemBars.top,
                right = systemBars.right,
                bottom = systemBars.bottom
            )
            insets
        }
    }
}