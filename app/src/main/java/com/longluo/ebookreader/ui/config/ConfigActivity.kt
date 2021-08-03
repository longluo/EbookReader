package com.longluo.ebookreader.ui.config

import android.os.Bundle
import androidx.activity.viewModels
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.VMBaseActivity
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.databinding.ActivityConfigBinding

import com.longluo.ebookreader.utils.observeEvent
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding

class ConfigActivity : VMBaseActivity<ActivityConfigBinding, ConfigViewModel>() {

    override val binding by viewBinding(ActivityConfigBinding::inflate)
    override val viewModel by viewModels<ConfigViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        intent.getIntExtra("configType", -1).let {
            if (it != -1) viewModel.configType = it
        }

        when (viewModel.configType) {
            ConfigViewModel.TYPE_CONFIG -> {
                binding.titleBar.title = getString(R.string.other_setting)
                val fTag = "otherConfigFragment"
                var configFragment = supportFragmentManager.findFragmentByTag(fTag)
                if (configFragment == null) configFragment = OtherConfigFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.configFrameLayout, configFragment, fTag)
                    .commit()
            }
            ConfigViewModel.TYPE_THEME_CONFIG -> {
                binding.titleBar.title = getString(R.string.theme_setting)
                val fTag = "themeConfigFragment"
                var configFragment = supportFragmentManager.findFragmentByTag(fTag)
                if (configFragment == null) configFragment = ThemeConfigFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.configFrameLayout, configFragment, fTag)
                    .commit()
            }
            ConfigViewModel.TYPE_WEB_DAV_CONFIG -> {
                binding.titleBar.title = getString(R.string.backup_restore)
                val fTag = "backupConfigFragment"
                var configFragment = supportFragmentManager.findFragmentByTag(fTag)
                if (configFragment == null) configFragment = BackupConfigFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.configFrameLayout, configFragment, fTag)
                    .commit()
            }
        }

    }

    override fun observeLiveBus() {
        super.observeLiveBus()
        observeEvent<String>(EventBus.RECREATE) {
            recreate()
        }
    }
}