package com.example.news.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.adapters.PagingLoadStateAdapter
import com.example.news.databinding.FragmentBreakingNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalPagingApi::class)
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private val viewModel by viewModels<BreakingNewsFragmentViewModel>()
    lateinit var adapter: NewsAdapter
    lateinit var binding: FragmentBreakingNewsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        adapter = NewsAdapter()

        adapter.onItemClickListener = {
            val bundle = Bundle().apply {
                putParcelable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_infoFragment,
                bundle
            )
        }
        binding.apply {
            rvBreakingNews.setHasFixedSize(true)
            rvBreakingNews.itemAnimator = null
            rvBreakingNews.adapter = adapter.withLoadStateHeaderAndFooter(
                footer = PagingLoadStateAdapter { adapter.retry() },
                header = PagingLoadStateAdapter { adapter.retry() }
            )
            buttonRetry.setOnClickListener { adapter.retry() }
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvBreakingNews.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached && adapter.itemCount < 1
                ) {
                    rvBreakingNews.isVisible = false
                    textViewEmpty.isVisible = true
                } else textViewEmpty.isVisible = false
            }
        }

        viewModel.news.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }



        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search_action)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchNews(query)
                    binding.rvBreakingNews.scrollToPosition(0)
                    searchView.clearFocus()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean = true
        })
    }


}
