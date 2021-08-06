package com.nannaapp.bhagavadgita.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nannaapp.bhagavadgita.databinding.ChapterViewBinding
import com.nannaapp.bhagavadgita.model.ChapterModel
import com.nannaapp.bhagavadgita.util.ItemOnClickListener
import kotlinx.android.synthetic.main.fragment_chapter_details.*
import javax.inject.Inject
import kotlin.math.roundToInt

class ChapterAdapter @Inject
constructor(val listener: ItemOnClickListener) :
    RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>() {

    public final val TAG = ChapterAdapter::class.java.canonicalName

    protected val diffCallBack = object : DiffUtil.ItemCallback<ChapterModel>() {
        override fun areItemsTheSame(oldItem: ChapterModel, newItem: ChapterModel): Boolean =
            oldItem.chapter_number == newItem.chapter_number

        override fun areContentsTheSame(oldItem: ChapterModel, newItem: ChapterModel): Boolean =
            oldItem.hashCode() == newItem.hashCode()

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    var chapters: List<ChapterModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val binding =
            ChapterViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val cur_chapter = chapters.get(position)
        holder.bind(cur_chapter)

    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    inner class ChapterViewHolder(private val binding: ChapterViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = chapters[position]
                    listener.onItemClicked(item.chapter_number, item.verses_count)
                }
            }
        }

        fun bind(cur_chapter: ChapterModel) {
            println(cur_chapter)
            binding.apply {
                chapterId.text = cur_chapter.chapter_number.toString()
                chapterName.text = cur_chapter.meaning.en
                chapterMeaning.text = "${cur_chapter.translation} - ${cur_chapter.name}"
                val percent = (cur_chapter.read_progress.toDouble() / cur_chapter.verses_count);
                readProgress.setProgress((percent * 100).roundToInt())
            }
        }
    }
}
