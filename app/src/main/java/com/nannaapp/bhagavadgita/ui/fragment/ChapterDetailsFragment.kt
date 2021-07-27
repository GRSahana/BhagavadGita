package com.nannaapp.bhagavadgita.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nannaapp.bhagavadgita.R
import com.nannaapp.bhagavadgita.adapter.SlokAdapter
import com.nannaapp.bhagavadgita.databinding.FragmentChapterDetailsBinding
import com.nannaapp.bhagavadgita.model.ChapterModel
import com.nannaapp.bhagavadgita.model.network_data.Chapter
import com.nannaapp.bhagavadgita.ui.viewmodel.ChapterDetailsViewModel
import com.nannaapp.bhagavadgita.util.ItemOnClickListener
import com.nannaapp.bhagavadgita.util.ResultOf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chapter_details.*

@AndroidEntryPoint
class ChapterDetailsFragment : Fragment(R.layout.fragment_chapter_details), ItemOnClickListener {

    private val TAG = "AppDebug"
    private val viewModel: ChapterDetailsViewModel by viewModels()
    private var _binding: FragmentChapterDetailsBinding? = null
    private val binding
        get() = _binding!!
    private var slokAdapter = SlokAdapter(this)
    val spanCount: Int = 3
    var chapter_number: Int = 0

    private val args by navArgs<ChapterDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChapterDetailsBinding.bind(view)
        binding.verseGrid.apply {
            layoutManager = GridLayoutManager(context, spanCount)
            hasFixedSize()
            isNestedScrollingEnabled = false
            adapter = slokAdapter
        }
        viewModel.getChapterDetails(args.chapterNumber)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is ResultOf.Success<ChapterModel> -> {
                    displayProgressBar(false)
                    displayChapterDetails(dataState.value)
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
        val snackbar = Snackbar.make(binding.relativeLayout, msg, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.shimmerLayout.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun displayChapterDetails(chapter: ChapterModel) {
        binding.apply {
            chapterName.text = chapter.name
            chapterMeaning.text = "${chapter.translation} - ${chapter.meaning.en}"
            chapterSummaryEn.text = chapter.summary.en
            verseCount.text = "Verse Count : ${chapter.verses_count}"
            chapter_number = chapter.chapter_number
            chapter_progress_linear.progress = chapter.read_progress
        }
        slokAdapter.verseInfo = chapter.verses_progress_list!!
    }

    override fun onItemClick(id: Int) {
        val action = ChapterDetailsFragmentDirections
            .actionChapterDetailsFragmentToVerseDetailsFragment(chapter_number, id)
        findNavController().navigate(action)
    }
}
