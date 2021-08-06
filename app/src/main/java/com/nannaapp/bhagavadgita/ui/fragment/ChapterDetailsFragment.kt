package com.nannaapp.bhagavadgita.ui.fragment

import android.os.Bundle
import android.util.Log
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
import com.nannaapp.bhagavadgita.model.cache_data.VerseInfo
import com.nannaapp.bhagavadgita.model.network_data.Chapter
import com.nannaapp.bhagavadgita.repository.MainRepository
import com.nannaapp.bhagavadgita.ui.viewmodel.ChapterDetailsViewModel
import com.nannaapp.bhagavadgita.util.ItemOnClickListener
import com.nannaapp.bhagavadgita.util.ResultOf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chapter_details.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class ChapterDetailsFragment : Fragment(R.layout.fragment_chapter_details), ItemOnClickListener {

    private val TAG = ChapterDetailsFragment::class.java.canonicalName
    private val viewModel: ChapterDetailsViewModel by viewModels()
    private var _binding: FragmentChapterDetailsBinding? = null
    private val binding
        get() = _binding!!
    private var slokAdapter = SlokAdapter(this, "")
    val spanCount: Int = 3
    var chapterNumber: Int = 0
    var snackbarStatus = false

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
        viewModel.getVerseDetails(args.chapterNumber, args.verseCount)
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
        viewModel.verseInfoList.observe(viewLifecycleOwner, Observer { verseInfoList ->
            when (verseInfoList) {
                is ResultOf.Success<List<VerseInfo>> -> {
                    displayProgressBar(false)
                    Log.d(TAG, "slokAdapter refresh")
                    slokAdapter.verseInfo = verseInfoList.value
                }
                is ResultOf.Error -> {
                    displayProgressBar(false)
                    displayError(verseInfoList.e.message)
                }
                is ResultOf.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
        viewModel.favState.observe(viewLifecycleOwner,{verseId ->
            when (verseId) {
                is ResultOf.Success<Int> -> {
                    Log.d(TAG, "slokAdapter refresh")
                    viewModel.getVerseDetails(args.chapterNumber, args.verseCount)
                    if(snackbarStatus){
                        snackbarStatus = false
                        val mySnackbar = Snackbar.make(
                            binding.constraintLayout,
                            "Verse ${verseId.value} is added to favorites", Snackbar.LENGTH_LONG
                        )
                        mySnackbar.setAction(R.string.undo_string) {
                            viewModel.updateFavoriteStatus(verseId.value)
                            viewModel.getVerseDetails(args.chapterNumber, args.verseCount)
                        }
                        mySnackbar.show()
                    }
                }
                is ResultOf.Error -> {
                    val mySnackbar = Snackbar.make(
                        binding.constraintLayout,
                        "Error setting favorite", Snackbar.LENGTH_LONG
                    )
                    mySnackbar.show()
                }
                else -> Unit
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
            chapterNumber = chapter.chapter_number
            val percent = (chapter.read_progress.toDouble() / chapter.verses_count);
            chapterProgressLinear.setProgress((percent * 100).roundToInt())
            progressText.setText("${(percent * 100).roundToInt()}/100")
            Log.d(TAG, "displayChapterDetails: ${chapter.read_progress.toDouble()} ${chapter.verses_count} ${(percent * 100).roundToInt()}")
        }
    }

    override fun onVerseItemClick(chapterNumber: Int, verseNumber: Int) {
        val action = ChapterDetailsFragmentDirections
            .actionChapterDetailsFragmentToVerseDetailsFragment(chapterNumber, verseNumber)
        findNavController().navigate(action)
    }

    override fun onFavClick(id: Int, favStatus:Boolean) {
        viewModel.updateFavoriteStatus(id)
        snackbarStatus = true
    }

    override fun onItemClicked(chapterNumber: Int, versesCount: Int) = Unit
}
