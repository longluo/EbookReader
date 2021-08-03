package com.longluo.ebookreader.ui.book.source.debug

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.VMBaseActivity
import com.longluo.ebookreader.databinding.ActivitySourceDebugBinding
import com.longluo.ebookreader.help.LocalConfig
import com.longluo.ebookreader.lib.theme.ATH
import com.longluo.ebookreader.lib.theme.accentColor
import com.longluo.ebookreader.ui.qrcode.QrCodeResult
import com.longluo.ebookreader.ui.widget.dialog.TextDialog
import com.longluo.ebookreader.utils.toastOnUi
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch

class BookSourceDebugActivity : VMBaseActivity<ActivitySourceDebugBinding, BookSourceDebugModel>() {

    override val binding by viewBinding(ActivitySourceDebugBinding::inflate)
    override val viewModel by viewModels<BookSourceDebugModel>()

    private lateinit var adapter: BookSourceDebugAdapter
    private lateinit var searchView: SearchView
    private val qrCodeResult = registerForActivityResult(QrCodeResult()) {
        it?.let {
            startSearch(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        searchView = binding.titleBar.findViewById(R.id.search_view)
        viewModel.init(intent.getStringExtra("key"))
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
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (!LocalConfig.debugHelpVersionIsLast) {
            showHelp()
        }
    }

    private fun initRecyclerView() {
        ATH.applyEdgeEffectColor(binding.recyclerView)
        adapter = BookSourceDebugAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.rotateLoading.loadingColor = accentColor
    }

    private fun initSearchView() {
        searchView.onActionViewExpanded()
        searchView.isSubmitButtonEnabled = true
        searchView.queryHint = getString(R.string.search_book_key)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                startSearch(query ?: "我的")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun startSearch(key: String) {
        adapter.clearItems()
        viewModel.startDebug(key, {
            binding.rotateLoading.show()
        }, {
            toastOnUi("未获取到书源")
        })
    }

    override fun onCompatCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.book_source_debug, menu)
        return super.onCompatCreateOptionsMenu(menu)
    }

    override fun onCompatOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_scan -> qrCodeResult.launch(null)
            R.id.menu_search_src ->
                TextDialog.show(supportFragmentManager, viewModel.searchSrc)
            R.id.menu_book_src ->
                TextDialog.show(supportFragmentManager, viewModel.bookSrc)
            R.id.menu_toc_src ->
                TextDialog.show(supportFragmentManager, viewModel.tocSrc)
            R.id.menu_content_src ->
                TextDialog.show(supportFragmentManager, viewModel.contentSrc)
            R.id.menu_help -> showHelp()
        }
        return super.onCompatOptionsItemSelected(item)
    }

    private fun showHelp() {
        val text = String(assets.open("help/debugHelp.md").readBytes())
        TextDialog.show(supportFragmentManager, text, TextDialog.MD)
    }

}