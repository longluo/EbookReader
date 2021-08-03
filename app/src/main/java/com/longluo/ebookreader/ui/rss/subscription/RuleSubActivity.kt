package com.longluo.ebookreader.ui.rss.subscription

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isGone
import androidx.recyclerview.widget.ItemTouchHelper
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseActivity
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.RuleSub
import com.longluo.ebookreader.databinding.ActivityRuleSubBinding
import com.longluo.ebookreader.databinding.DialogRuleSubEditBinding
import com.longluo.ebookreader.lib.dialogs.alert
import com.longluo.ebookreader.ui.association.ImportBookSourceDialog
import com.longluo.ebookreader.ui.association.ImportReplaceRuleDialog
import com.longluo.ebookreader.ui.association.ImportRssSourceDialog
import com.longluo.ebookreader.ui.widget.recycler.ItemTouchCallback
import com.longluo.ebookreader.utils.toastOnUi
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 规则订阅界面
 */
class RuleSubActivity : BaseActivity<ActivityRuleSubBinding>(),
    RuleSubAdapter.Callback {

    override val binding by viewBinding(ActivityRuleSubBinding::inflate)
    private lateinit var adapter: RuleSubAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initView()
        initData()
    }

    override fun onCompatCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.source_subscription, menu)
        return super.onCompatCreateOptionsMenu(menu)
    }

    override fun onCompatOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                val order = appDb.ruleSubDao.maxOrder + 1
                editSubscription(RuleSub(customOrder = order))
            }
        }
        return super.onCompatOptionsItemSelected(item)
    }

    private fun initView() {
        adapter = RuleSubAdapter(this, this)
        binding.recyclerView.adapter = adapter
        val itemTouchCallback = ItemTouchCallback(adapter)
        itemTouchCallback.isCanDrag = true
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.recyclerView)
    }

    private fun initData() {
        launch {
            appDb.ruleSubDao.flowAll().collect {
                binding.tvEmptyMsg.isGone = it.isNotEmpty()
                adapter.setItems(it)
            }
        }
    }

    override fun openSubscription(ruleSub: RuleSub) {
        when (ruleSub.type) {
            0 -> {
                ImportBookSourceDialog.start(supportFragmentManager, ruleSub.url)
            }
            1 -> {
                ImportRssSourceDialog.start(supportFragmentManager, ruleSub.url)
            }
            2 -> {
                ImportReplaceRuleDialog.start(supportFragmentManager, ruleSub.url)
            }
        }
    }

    override fun editSubscription(ruleSub: RuleSub) {
        alert(R.string.rule_subscription) {
            val alertBinding = DialogRuleSubEditBinding.inflate(layoutInflater).apply {
                spType.setSelection(ruleSub.type)
                etName.setText(ruleSub.name)
                etUrl.setText(ruleSub.url)
            }
            customView { alertBinding.root }
            okButton {
                launch {
                    ruleSub.type = alertBinding.spType.selectedItemPosition
                    ruleSub.name = alertBinding.etName.text?.toString() ?: ""
                    ruleSub.url = alertBinding.etUrl.text?.toString() ?: ""
                    val rs = withContext(IO) {
                        appDb.ruleSubDao.findByUrl(ruleSub.url)
                    }
                    if (rs != null && rs.id != ruleSub.id) {
                        toastOnUi("${getString(R.string.url_already)}(${rs.name})")
                        return@launch
                    }
                    withContext(IO) {
                        appDb.ruleSubDao.insert(ruleSub)
                    }
                }
            }
            cancelButton()
        }.show()
    }

    override fun delSubscription(ruleSub: RuleSub) {
        launch(IO) {
            appDb.ruleSubDao.delete(ruleSub)
        }
    }

    override fun updateSourceSub(vararg ruleSub: RuleSub) {
        launch(IO) {
            appDb.ruleSubDao.update(*ruleSub)
        }
    }

    override fun upOrder() {
        launch(IO) {
            val sourceSubs = appDb.ruleSubDao.all
            for ((index: Int, ruleSub: RuleSub) in sourceSubs.withIndex()) {
                ruleSub.customOrder = index + 1
            }
            appDb.ruleSubDao.update(*sourceSubs.toTypedArray())
        }
    }

}