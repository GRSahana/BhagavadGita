package com.nannaapp.bhagavadgita.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
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
import com.nannaapp.bhagavadgita.ui.viewmodel.ChapterDetailsViewModel
import com.nannaapp.bhagavadgita.util.ItemOnClickListener
import com.nannaapp.bhagavadgita.View.PlayPauseView
import com.nannaapp.bhagavadgita.util.ResultOf
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
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
    var pausePlayStatus = false

    private val args by navArgs<ChapterDetailsFragmentArgs>()

    lateinit var textToSpeech: TextToSpeech

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
        textToSpeech = TextToSpeech(
            activity
        ) { i ->
            // if No error is found then only it will run
            if (i != TextToSpeech.ERROR) {
                // To Choose language of speech
                textToSpeech.language = Locale.ENGLISH
                Log.i("TextToSpeech", textToSpeech.availableLanguages.toString())
                Log.i("TextToSpeech", textToSpeech.voices.toString())
                textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String) {
                        Log.i("TextToSpeech", "On Start")
                    }

                    override fun onDone(utteranceId: String) {
                        Log.i("TextToSpeech", "On Done")
                        stopTextToSpeech()
                    }

                    override fun onError(utteranceId: String) {
                        Log.i("TextToSpeech", "On Error")
                        stopTextToSpeech()
                    }
                })
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        binding.ttsButton.setOnClickListener {
            if (!pausePlayStatus) {
                pausePlayStatus = true
                binding.ttsButton.setState(PlayPauseView.STATE_PLAY)
                var text = "${binding.chapterName.text}. ${binding.chapterSummaryEn.text}"
                textToSpeech.speak(
                    text,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED
                )
                binding.visualView.apply {
                    updateSpeaking(true)
                    updateViewColor(Color.BLACK)
                    updateAmplitude(0.5f)
                    updateNumberOfWaves(3.0f)
                    updateSpeed(-0.1f)
                }
            } else {
                textToSpeech.stop()
                stopTextToSpeech()
            }
        }
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
        viewModel.favState.observe(viewLifecycleOwner, { verseId ->
            when (verseId) {
                is ResultOf.Success<Int> -> {
                    Log.d(TAG, "slokAdapter refresh")
                    viewModel.getVerseDetails(args.chapterNumber, args.verseCount)
                    if (snackbarStatus) {
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

    private fun stopTextToSpeech() {
        binding.visualView.apply {
            updateSpeaking(false)
            pausePlayStatus = false
        }
        binding.ttsButton.setState(PlayPauseView.STATE_PAUSE)
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
            chapterName.text = "${chapter.translation} - ${chapter.meaning.en}"
            chapterSummaryEn.text = chapter.summary.en
            verseCount.text = "Verse Count : ${chapter.verses_count}"
            chapterNumber = chapter.chapter_number
            val percent = (chapter.read_progress.toDouble() / chapter.verses_count);
            chapterProgressLinear.setProgress((percent * 100).roundToInt())
            progressText.setText("${(percent * 100).roundToInt()}/100")
            Log.d(
                TAG,
                "displayChapterDetails: ${chapter.read_progress.toDouble()} ${chapter.verses_count} ${(percent * 100).roundToInt()}"
            )
        }
    }

    override fun onVerseItemClick(chapterNumber: Int, verseNumber: Int) {
        val action = ChapterDetailsFragmentDirections
            .actionChapterDetailsFragmentToVerseDetailsFragment(chapterNumber, verseNumber)
        findNavController().navigate(action)
    }

    override fun onFavClick(id: Int, favStatus: Boolean) {
        viewModel.updateFavoriteStatus(id)
        snackbarStatus = true
    }

    override fun onItemClicked(chapterNumber: Int, versesCount: Int) = Unit

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
    }
}
