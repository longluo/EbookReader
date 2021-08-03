package com.longluo.ebookreader.ui.book.search

import android.content.Context
import android.view.ViewGroup
import com.longluo.ebookreader.base.adapter.ItemViewHolder
import com.longluo.ebookreader.base.adapter.RecyclerAdapter
import com.longluo.ebookreader.data.entities.Book
import com.longluo.ebookreader.databinding.ItemFilletTextBinding


class BookAdapter(context: Context, val callBack: CallBack) :
    RecyclerAdapter<Book, ItemFilletTextBinding>(context) {

    override fun getViewBinding(parent: ViewGroup): ItemFilletTextBinding {
        return ItemFilletTextBinding.inflate(inflater, parent, false)
    }

    override fun convert(
        holder: ItemViewHolder,
        binding: ItemFilletTextBinding,
        item: Book,
        payloads: MutableList<Any>
    ) {
        binding.run {
            textView.text = item.name
        }
    }

    override fun registerListener(holder: ItemViewHolder, binding: ItemFilletTextBinding) {
        holder.itemView.apply {
            setOnClickListener {
                getItem(holder.layoutPosition)?.let {
                    callBack.showBookInfo(it)
                }
            }
        }
    }

    interface CallBack {
        fun showBookInfo(book: Book)
    }
}