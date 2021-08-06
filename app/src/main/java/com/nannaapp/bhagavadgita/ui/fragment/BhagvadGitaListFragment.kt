package com.nannaapp.bhagavadgita.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nannaapp.bhagavadgita.R
import com.nannaapp.bhagavadgita.adapter.ChapterAdapter
import com.nannaapp.bhagavadgita.databinding.FragmentChapterListBinding
import com.nannaapp.bhagavadgita.model.ChapterModel
import com.nannaapp.bhagavadgita.model.network_data.Chapter
import com.nannaapp.bhagavadgita.ui.viewmodel.MainViewModel
import com.nannaapp.bhagavadgita.util.ItemOnClickListener
import com.nannaapp.bhagavadgita.util.ResultOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BhagvadGitaListFragment : Fragment(R.layout.fragment_chapter_list), ItemOnClickListener {

    private val TAG = "AppDebug"
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentChapterListBinding? = null
    private val binding
        get() = _binding!!
    private var chapterAdapter = ChapterAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChapterListBinding.bind(view)
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            adapter = chapterAdapter
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is ResultOf.Success<List<ChapterModel>> -> {
                    displayProgressBar(false)
                    displayChapterList(dataState.value)
                }
                is ResultOf.Error -> {
                    displayProgressBar(false)
                    displayError(dataState.e.message)
                }
                is ResultOf.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
    }

    private fun displayError(message: String?) {
        val msg = if (message != null) message else "Unknown Error"
        val snackbar = Snackbar.make(binding.constraintLayout, msg, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.shimmerLayout.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun displayChapterList(chapters: List<ChapterModel>) {
        chapterAdapter.chapters = chapters
    }

    override fun onVerseItemClick(chapterNumber: Int, verseNumber: Int) = Unit

    override fun onFavClick(id: Int, favStatus: Boolean)  = Unit

    override fun onItemClicked(chapterNumber: Int, versesCount: Int) {
        val action = BhagvadGitaListFragmentDirections
            .actionBhagvadGitaListFragmentToChapterDetailsFragment(chapterNumber,versesCount)
        findNavController().navigate(action)
    }

}
