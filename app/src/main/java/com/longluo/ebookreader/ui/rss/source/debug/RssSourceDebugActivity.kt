package com.longluo.ebookreader.ui.rss.source.debug

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.VMBaseActivity
import com.longluo.ebookreader.databinding.ActivitySourceDebugBinding
import com.longluo.ebookreader.lib.theme.ATH
import com.longluo.ebookreader.lib.theme.accentColor
import com.longluo.ebookreader.ui.widget.dialog.TextDialog
import com.longluo.ebookreader.utils.gone
import com.longluo.ebookreader.utils.toastOnUi
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch


class RssSourceDebugActivity : VMBaseActivity<ActivitySourceDebugBinding, RssSourceDebugModel>() {

    override val binding by viewBinding(ActivitySourceDebugBinding::inflate)
    override val viewModel by viewModels<RssSourceDebugModel>()

    private lateinit var adapter: RssSourceDebugAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initRecyclerView()
        initSearchView()
        viewModel.observe { state, msg ->
            launch {
                adapter.addItem(msg)
                if (state == -1 || state == 1000) {
                    binding.rotateLoading.hide()
                }
            }
        }
        viewModel.initData(intent.getStringExtra("key")) {
            startSearch()
        }
    }

    override fun onCompatCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.rss_source_debug, menu)
        return super.onCompatCreateOptionsMenu(menu)
    }

    override fun onCompatOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_list_src ->
                TextDialog.show(supportFragmentManager, viewModel.listSrc)
            R.id.menu_content_src ->
                TextDialog.show(supportFragmentManager, viewModel.contentSrc)
        }
        return super.onCompatOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        ATH.applyEdgeEffectColor(binding.recyclerView)
        adapter = RssSourceDebugAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.rotateLoading.loadingColor = accentColor
    }

    private fun initSearchView() {
        binding.titleBar.findViewById<SearchView>(R.id.search_view).gone()
    }

    private fun startSearch() {
        adapter.clearItems()
        viewModel.startDebug({
            binding.rotateLoading.show()
        }, {
            toastOnUi("未获取到源")
        })
    }
}