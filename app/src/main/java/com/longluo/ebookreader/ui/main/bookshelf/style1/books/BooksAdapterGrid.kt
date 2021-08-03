package com.longluo.ebookreader.ui.main.bookshelf.style1.books

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.longluo.ebookreader.base.adapter.ItemViewHolder
import com.longluo.ebookreader.constant.BookType
import com.longluo.ebookreader.data.entities.Book
import com.longluo.ebookreader.databinding.ItemBookshelfGridBinding
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.utils.invisible
import splitties.views.onLongClick

class BooksAdapterGrid(context: Context, private val callBack: CallBack) :
    BaseBooksAdapter<ItemBookshelfGridBinding>(context) {

    override fun getViewBinding(parent: ViewGroup): ItemBookshelfGridBinding {
        return ItemBookshelfGridBinding.inflate(inflater, parent, false)
    }

    override fun convert(
        holder: ItemViewHolder,
        binding: ItemBookshelfGridBinding,
        item: Book,
        payloads: MutableList<Any>
    ) = binding.run {
        val bundle = payloads.getOrNull(0) as? Bundle
        if (bundle == null) {
            tvName.text = item.name
            ivCover.load(item.getDisplayCover(), item.name, item.author)
            upRefresh(binding, item)
        } else {
            bundle.keySet().forEach {
                when (it) {
                    "name" -> tvName.text = item.name
                    "cover" -> ivCover.load(item.getDisplayCover(), item.name, item.author)
                    "refresh" -> upRefresh(binding, item)
                }
            }
        }
    }

    private fun upRefresh(binding: ItemBookshelfGridBinding, item: Book) {
        if (item.origin != BookType.local && callBack.isUpdate(item.bookUrl)) {
            binding.bvUnread.invisible()
            binding.rlLoading.show()
        } else {
            binding.rlLoading.hide()
            if (AppConfig.showUnread) {
                binding.bvUnread.setBadgeCount(item.getUnreadChapterNum())
                binding.bvUnread.setHighlight(item.lastCheckCount > 0)
            } else {
                binding.bvUnread.invisible()
            }
        }
    }

    override fun registerListener(holder: ItemViewHolder, binding: ItemBookshelfGridBinding) {
        holder.itemView.apply {
            setOnClickListener {
                getItem(holder.layoutPosition)?.let {
                    callBack.open(it)
                }
            }

            onLongClick {
                getItem(holder.layoutPosition)?.let {
                    callBack.openBookInfo(it)
                }
            }
        }
    }
}