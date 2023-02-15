package com.example.news.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.example.news.R
import com.example.news.databinding.FragmentInfoBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalPagingApi::class)
class InfoFragment : Fragment(R.layout.fragment_info) {
    private val viewModel by viewModels<BreakingNewsFragmentViewModel>()
    private val args: InfoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentInfoBinding.bind(view)
        val article = args.article
        binding.apply {
            webView.apply {
                webViewClient = WebViewClient()
                article.url?.let { loadUrl(it) }
            }
            fab.setOnClickListener {
                viewModel.saveArticle(article)
                Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}