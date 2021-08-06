package com.nannaapp.bhagavadgita.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nannaapp.bhagavadgita.R
import com.nannaapp.bhagavadgita.adapter.SlokAdapter
import com.nannaapp.bhagavadgita.databinding.FragmentChapterDetailsBinding
import com.nannaapp.bhagavadgita.databinding.FragmentFavVerseBinding
import com.nannaapp.bhagavadgita.model.ChapterModel
import com.nannaapp.bhagavadgita.model.cache_data.VerseInfo
import com.nannaapp.bhagavadgita.ui.viewmodel.ChapterDetailsViewModel
import com.nannaapp.bhagavadgita.util.ItemOnClickListener
import com.nannaapp.bhagavadgita.util.ResultOf
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class FavoriteVerseFragment: Fragment(R.layout.fragment_fav_verse) , ItemOnClickListener {

    private val TAG = FavoriteVerseFragment::class.java.canonicalName
    private val viewModel: ChapterDetailsViewModel by viewModels()
    private var _binding: FragmentFavVerseBinding? = null
    private val binding
        get() = _binding!!
    private var slokAdapter = SlokAdapter(this, "Favorite")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavVerseBinding.bind(view)
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(context, 2)
            hasFixedSize()
            isNestedScrollingEnabled = false
            adapter = slokAdapter
        }
        viewModel.getFavVerseDetails()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.verseInfoList.observe(viewLifecycleOwner, Observer { verseInfoList ->
            when (verseInfoList) {
                is ResultOf.Success<List<VerseInfo>> -> {
                    displayProgressBar(false)
                    Log.d(TAG, "slokAdapter refresh")
                    if(verseInfoList.value.isEmpty()){
                        binding.emptyLayout.visibility = View.VISIBLE
                    }else{
                        binding.emptyLayout.visibility = View.GONE
                        slokAdapter.verseInfo = verseInfoList.value
                    }
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
    }

    private fun displayError(message: String?) {
        val msg = message ?: "Unknown Error"
        val snackbar = Snackbar.make(binding.constraintLayout, msg, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.shimmerLayout.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    override fun onVerseItemClick(chapterNumber: Int, verseNumber: Int) {
        val action = FavoriteVerseFragmentDirections
            .actionFavoriteVerseFragmentToVerseDetailsFragment(chapterNumber, verseNumber)
        findNavController().navigate(action)
    }

    override fun onFavClick(id: Int, favStatus:Boolean) = Unit

    override fun onItemClicked(chapterNumber: Int, versesCount: Int) = Unit
}