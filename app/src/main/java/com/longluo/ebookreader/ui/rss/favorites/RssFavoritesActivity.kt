package com.longluo.ebookreader.ui.rss.favorites

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.longluo.ebookreader.base.BaseActivity
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.RssStar
import com.longluo.ebookreader.databinding.ActivityRssFavoritesBinding
import com.longluo.ebookreader.ui.rss.read.ReadRssActivity
import com.longluo.ebookreader.ui.widget.recycler.VerticalDivider
import com.longluo.ebookreader.utils.startActivity
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RssFavoritesActivity : BaseActivity<ActivityRssFavoritesBinding>(),
    RssFavoritesAdapter.CallBack {

    override val binding by viewBinding(ActivityRssFavoritesBinding::inflate)
    private lateinit var adapter: RssFavoritesAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initView()
        initData()
    }

    private fun initView() {
        binding.recyclerView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(VerticalDivider(this))
            adapter = RssFavoritesAdapter(this, this)
            it.adapter = adapter
        }
    }

    private fun initData() {
        launch {
            appDb.rssStarDao.liveAll().collect {
                adapter.setItems(it)
            }
        }
    }

    override fun readRss(rssStar: RssStar) {
        startActivity<ReadRssActivity> {
            putExtra("title", rssStar.title)
            putExtra("origin", rssStar.origin)
            putExtra("link", rssStar.link)
        }
    }
}