package com.example.news.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapters.DatabaseAdapter
import com.example.news.adapters.PagingLoadStateAdapter
import com.example.news.databinding.FragmentSavedNewsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalPagingApi::class)
class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var binding: FragmentSavedNewsBinding
    private val viewModel by viewModels<BreakingNewsFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedNewsBinding.bind(view)
        val databaseAdapter = DatabaseAdapter()

        binding.apply {
            rvSavedNews.setHasFixedSize(true)
            rvSavedNews.itemAnimator = null
            rvSavedNews.adapter = databaseAdapter

        }

        databaseAdapter.onItemClickListener = {
            val bundle = Bundle().apply {
                putParcelable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_infoFragment, bundle
            )
        }

        viewModel.getSavedArticles().observe(viewLifecycleOwner) {
            databaseAdapter.differ.submitList(it)
        }





        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }


                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val article = databaseAdapter.differ.currentList[position]
                    viewModel.deleteArticle(article!!)
                    Snackbar.make(view!!, "Article deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("UNDO") {
                            viewModel.saveArticle(article)
                        }
                    }.show()
                }
            }
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvSavedNews)

        }


    }

