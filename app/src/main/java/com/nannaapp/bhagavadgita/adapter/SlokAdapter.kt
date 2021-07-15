package com.nannaapp.bhagavadgita.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nannaapp.bhagavadgita.databinding.VerseViewBinding
import com.nannaapp.bhagavadgita.model.cache_data.VerseInfo
import com.nannaapp.bhagavadgita.util.ItemOnClickListener
import javax.inject.Inject

class SlokAdapter @Inject
constructor(val listener: ItemOnClickListener) :
    RecyclerView.Adapter<SlokAdapter.SlokViewHolder>() {

    protected val diffCallBack = object : DiffUtil.ItemCallback<VerseInfo>() {
        override fun areItemsTheSame(oldItem: VerseInfo, newItem: VerseInfo): Boolean =
            oldItem.verse_number == newItem.verse_number

        override fun areContentsTheSame(oldItem: VerseInfo, newItem: VerseInfo): Boolean =
            oldItem.hashCode() == newItem.hashCode()

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    var verseInfo: List<VerseInfo>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlokViewHolder {
        val binding =
            VerseViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlokViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlokViewHolder, position: Int) {
        val cur_verse = verseInfo.get(position)
        holder.bind(cur_verse)
    }

    inner class SlokViewHolder(private val binding: VerseViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = verseInfo[position]
                    listener.onItemClick(item.verse_number)
                }
            }
        }

        fun bind(cur_verse: VerseInfo) {
            println("Count " + cur_verse)
            binding.apply {
                verseId.text = "Verse ${cur_verse.verse_number.toString()}"
            }
        }
    }

    override fun getItemCount(): Int {
        return verseInfo.size
    }
}
