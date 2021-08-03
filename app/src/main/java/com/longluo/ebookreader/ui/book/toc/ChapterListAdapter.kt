package com.longluo.ebookreader.ui.book.toc

import android.content.Context
import android.view.ViewGroup
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.adapter.ItemViewHolder
import com.longluo.ebookreader.base.adapter.RecyclerAdapter
import com.longluo.ebookreader.data.entities.BookChapter
import com.longluo.ebookreader.databinding.ItemChapterListBinding
import com.longluo.ebookreader.lib.theme.accentColor
import com.longluo.ebookreader.utils.getCompatColor
import com.longluo.ebookreader.utils.visible


class ChapterListAdapter(context: Context, val callback: Callback) :
    RecyclerAdapter<BookChapter, ItemChapterListBinding>(context) {

    val cacheFileNames = hashSetOf<String>()

    override fun getViewBinding(parent: ViewGroup): ItemChapterListBinding {
        return ItemChapterListBinding.inflate(inflater, parent, false)
    }

    override fun convert(
        holder: ItemViewHolder,
        binding: ItemChapterListBinding,
        item: BookChapter,
        payloads: MutableList<Any>
    ) {
        binding.run {
            val isDur = callback.durChapterIndex() == item.index
            val cached = callback.isLocalBook || cacheFileNames.contains(item.getFileName())
            if (payloads.isEmpty()) {
                if (isDur) {
                    tvChapterName.setTextColor(context.accentColor)
                } else {
                    tvChapterName.setTextColor(context.getCompatColor(R.color.primaryText))
                }
                tvChapterName.text = item.title
                if (!item.tag.isNullOrEmpty()) {
                    tvTag.text = item.tag
                    tvTag.visible()
                }
                upHasCache(binding, isDur, cached)
            } else {
                upHasCache(binding, isDur, cached)
            }
        }
    }

    override fun registerListener(holder: ItemViewHolder, binding: ItemChapterListBinding) {
        holder.itemView.setOnClickListener {
            getItem(holder.layoutPosition)?.let {
                callback.openChapter(it)
            }
        }
    }

    private fun upHasCache(binding: ItemChapterListBinding, isDur: Boolean, cached: Boolean) =
        binding.apply {
            tvChapterName.paint.isFakeBoldText = cached
            ivChecked.setImageResource(R.drawable.ic_outline_cloud_24)
            ivChecked.visible(!cached)
            if (isDur) {
                ivChecked.setImageResource(R.drawable.ic_check)
                ivChecked.visible()
            }
        }

    interface Callback {
        val isLocalBook: Boolean
        fun openChapter(bookChapter: BookChapter)
        fun durChapterIndex(): Int
    }
}