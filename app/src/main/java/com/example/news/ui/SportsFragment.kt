package com.example.news.ui


import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.adapters.PagingLoadStateAdapter
import com.example.news.databinding.FragmentSportsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalPagingApi::class)
class SportsFragment : Fragment(R.layout.fragment_sports) {
    private val viewModel by viewModels<BreakingNewsFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSportsBinding.bind(view)
        val adapter = NewsAdapter()

        adapter.onItemClickListener = {
            val bundle = Bundle().apply {
                putParcelable("article", it)
            }
            findNavController().navigate(
                R.id.action_sportsFragment_to_infoFragment,
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

        viewModel.sportsNews.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }


    }
}
