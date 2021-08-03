package com.longluo.ebookreader.ui.rss.favorites

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.longluo.ebookreader.base.adapter.ItemViewHolder
import com.longluo.ebookreader.base.adapter.RecyclerAdapter
import com.longluo.ebookreader.data.entities.RssStar
import com.longluo.ebookreader.databinding.ItemRssArticleBinding
import com.longluo.ebookreader.help.ImageLoader
import com.longluo.ebookreader.utils.gone
import com.longluo.ebookreader.utils.visible


class RssFavoritesAdapter(context: Context, val callBack: CallBack) :
    RecyclerAdapter<RssStar, ItemRssArticleBinding>(context) {

    override fun getViewBinding(parent: ViewGroup): ItemRssArticleBinding {
        return ItemRssArticleBinding.inflate(inflater, parent, false)
    }

    override fun convert(
        holder: ItemViewHolder,
        binding: ItemRssArticleBinding,
        item: RssStar,
        payloads: MutableList<Any>
    ) {
        binding.run {
            tvTitle.text = item.title
            tvPubDate.text = item.pubDate
            if (item.image.isNullOrBlank()) {
                imageView.gone()
            } else {
                ImageLoader.load(context, item.image)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            imageView.gone()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            imageView.visible()
                            return false
                        }

                    })
                    .into(imageView)
            }
        }
    }

    override fun registerListener(holder: ItemViewHolder, binding: ItemRssArticleBinding) {
        holder.itemView.setOnClickListener {
            getItem(holder.layoutPosition)?.let {
                callBack.readRss(it)
            }
        }
    }

    interface CallBack {
        fun readRss(rssStar: RssStar)
    }
}