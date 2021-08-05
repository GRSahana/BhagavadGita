package com.nannaapp.bhagavadgita.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.nannaapp.bhagavadgita.R
import com.nannaapp.bhagavadgita.databinding.FragmentVerseDetailsBinding
import com.nannaapp.bhagavadgita.model.network_data.Slok
import com.nannaapp.bhagavadgita.ui.viewmodel.VerseDetailsViewModel
import com.nannaapp.bhagavadgita.util.ResultOf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class VerseDetailsFragment : Fragment(R.layout.fragment_verse_details) {

    private val TAG = "AppDebug"

    @Inject
    lateinit var glide: RequestManager
    private val viewModel : VerseDetailsViewModel by viewModels()
    private var _binding: FragmentVerseDetailsBinding? = null
    private val binding
        get() = _binding!!
    private val args by navArgs<VerseDetailsFragmentArgs>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVerseDetailsBinding.bind(view)

        viewModel.getVerseDetails(args.chapterNumber, args.verseNumber)
        subscribeObservers()
        binding.scrollView.getViewTreeObserver()
            .addOnScrollChangedListener(OnScrollChangedListener {
                if (binding.scrollView.getChildAt(0).getBottom()
                    <= binding.scrollView.getHeight() + binding.scrollView.getScrollY()
                ) {
                    //scroll view is at bottom
                    viewModel.updateReadStatus(args.chapterNumber, args.verseNumber)
                }
            })
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is ResultOf.Success<Slok> -> {
                    displayProgressBar(false)
                    displayVerseDetails(dataState.value)
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

    private fun displayError(message: String?){
        val msg = if(message!=null) message else "Unknown Error"
        val snackbar = Snackbar.make(binding.relativeLayout, msg, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun displayProgressBar(isDisplayed : Boolean){
        binding.shimmerLayout.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    private fun displayVerseDetails(slok: Slok){
        binding.apply {
            verse.text = "${slok.slok} \n\n${slok.transliteration}"
            verseMeaning.text = "${slok.raman.et} \n \t\t by ${slok.raman.author}"
        }
    }


}
