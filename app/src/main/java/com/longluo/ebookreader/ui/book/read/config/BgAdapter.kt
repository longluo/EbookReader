package com.longluo.ebookreader.ui.book.read.config

import android.content.Context
import android.view.ViewGroup
import com.longluo.ebookreader.base.adapter.ItemViewHolder
import com.longluo.ebookreader.base.adapter.RecyclerAdapter
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.databinding.ItemBgImageBinding
import com.longluo.ebookreader.help.ImageLoader
import com.longluo.ebookreader.help.ReadBookConfig
import com.longluo.ebookreader.utils.postEvent

import java.io.File

class BgAdapter(context: Context, val textColor: Int) :
    RecyclerAdapter<String, ItemBgImageBinding>(context) {

    override fun getViewBinding(parent: ViewGroup): ItemBgImageBinding {
        return ItemBgImageBinding.inflate(inflater, parent, false)
    }

    override fun convert(
        holder: ItemViewHolder,
        binding: ItemBgImageBinding,
        item: String,
        payloads: MutableList<Any>
    ) {
        binding.run {
            ImageLoader.load(
                context,
                context.assets.open("bg${File.separator}$item").readBytes()
            )
                .centerCrop()
                .into(ivBg)
            tvName.setTextColor(textColor)
            tvName.text = item.substringBeforeLast(".")
        }
    }

    override fun registerListener(holder: ItemViewHolder, binding: ItemBgImageBinding) {
        holder.itemView.apply {
            this.setOnClickListener {
                getItemByLayoutPosition(holder.layoutPosition)?.let {
                    ReadBookConfig.durConfig.setCurBg(1, it)
                    ReadBookConfig.upBg()
                    postEvent(EventBus.UP_CONFIG, false)
                }
            }
        }
    }
}