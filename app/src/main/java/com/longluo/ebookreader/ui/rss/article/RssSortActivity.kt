@file:Suppress("DEPRECATION")

package com.longluo.ebookreader.ui.rss.article

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.VMBaseActivity
import com.longluo.ebookreader.databinding.ActivityRssArtivlesBinding
import com.longluo.ebookreader.ui.rss.source.edit.RssSourceEditActivity
import com.longluo.ebookreader.utils.gone
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding
import com.longluo.ebookreader.utils.visible

class RssSortActivity : VMBaseActivity<ActivityRssArtivlesBinding, RssSortViewModel>() {

    override val binding by viewBinding(ActivityRssArtivlesBinding::inflate)
    override val viewModel by viewModels<RssSortViewModel>()
    private lateinit var adapter: TabFragmentPageAdapter
    private val fragments = linkedMapOf<String, Fragment>()
    private val upSourceResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            viewModel.initData(intent) {
                upFragments()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        adapter = TabFragmentPageAdapter()
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        viewModel.titleLiveData.observe(this, {
            binding.titleBar.title = it
        })
        viewModel.initData(intent) {
            upFragments()
        }
    }

    override fun onCompatCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.rss_articles, menu)
        return super.onCompatCreateOptionsMenu(menu)
    }

    override fun onCompatOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit_source -> viewModel.rssSource?.sourceUrl?.let {
                upSourceResult.launch(
                    Intent(this, RssSourceEditActivity::class.java)
                        .putExtra("data", it)
                )
            }
            R.id.menu_clear -> {
                viewModel.url?.let {
                    viewModel.clearArticles()
                }
            }
            R.id.menu_switch_layout -> {
                viewModel.switchLayout()
                upFragments()
            }
        }
        return super.onCompatOptionsItemSelected(item)
    }

    private fun upFragments() {
        fragments.clear()
        viewModel.rssSource?.sortUrls()?.forEach {
            fragments[it.key] = RssArticlesFragment.create(it.key, it.value)
        }
        if (fragments.size == 1) {
            binding.tabLayout.gone()
        } else {
            binding.tabLayout.visible()
        }
        adapter.notifyDataSetChanged()
    }

    private inner class TabFragmentPageAdapter :
        FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }

        override fun getPageTitle(position: Int): CharSequence {
            return fragments.keys.elementAt(position)
        }

        override fun getItem(position: Int): Fragment {
            return fragments.values.elementAt(position)
        }

        override fun getCount(): Int {
            return fragments.size
        }

    }

}