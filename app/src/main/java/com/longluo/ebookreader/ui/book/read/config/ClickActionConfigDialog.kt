package com.longluo.ebookreader.ui.book.read.config

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseDialogFragment
import com.longluo.ebookreader.constant.PreferKey
import com.longluo.ebookreader.databinding.DialogClickActionConfigBinding
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.lib.dialogs.selector
import com.longluo.ebookreader.ui.book.read.ReadBookActivity
import com.longluo.ebookreader.utils.getCompatColor
import com.longluo.ebookreader.utils.putPrefInt
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding


class ClickActionConfigDialog : BaseDialogFragment() {
    private val binding by viewBinding(DialogClickActionConfigBinding::bind)
    private val actions = linkedMapOf<Int, String>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setBackgroundDrawableResource(R.color.transparent)
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as ReadBookActivity).bottomDialog++
        return inflater.inflate(R.layout.dialog_click_action_config, container)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as ReadBookActivity).bottomDialog--
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        view.setBackgroundColor(getCompatColor(R.color.translucent))
        actions[-1] = getString(R.string.non_action)
        actions[0] = getString(R.string.menu)
        actions[1] = getString(R.string.next_page)
        actions[2] = getString(R.string.prev_page)
        actions[3] = getString(R.string.next_chapter)
        actions[4] = getString(R.string.previous_chapter)
        initData()
        initViewEvent()
    }

    private fun initData() = binding.run {
        tvTopLeft.text = actions[AppConfig.clickActionTL]
        tvTopCenter.text = actions[AppConfig.clickActionTC]
        tvTopRight.text = actions[AppConfig.clickActionTR]
        tvMiddleLeft.text = actions[AppConfig.clickActionML]
        tvMiddleRight.text = actions[AppConfig.clickActionMR]
        tvBottomLeft.text = actions[AppConfig.clickActionBL]
        tvBottomCenter.text = actions[AppConfig.clickActionBC]
        tvBottomRight.text = actions[AppConfig.clickActionBR]
    }

    private fun initViewEvent() {
        binding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.tvTopLeft.setOnClickListener {
            selectAction { action ->
                putPrefInt(PreferKey.clickActionTL, action)
                (it as? TextView)?.text = actions[action]
            }
        }
        binding.tvTopCenter.setOnClickListener {
            selectAction { action ->
                putPrefInt(PreferKey.clickActionTC, action)
                (it as? TextView)?.text = actions[action]
            }
        }
        binding.tvTopRight.setOnClickListener {
            selectAction { action ->
                putPrefInt(PreferKey.clickActionTR, action)
                (it as? TextView)?.text = actions[action]
            }
        }
        binding.tvMiddleLeft.setOnClickListener {
            selectAction { action ->
                putPrefInt(PreferKey.clickActionML, action)
                (it as? TextView)?.text = actions[action]
            }
        }
        binding.tvMiddleRight.setOnClickListener {
            selectAction { action ->
                putPrefInt(PreferKey.clickActionMR, action)
                (it as? TextView)?.text = actions[action]
            }
        }
        binding.tvBottomLeft.setOnClickListener {
            selectAction { action ->
                putPrefInt(PreferKey.clickActionBL, action)
                (it as? TextView)?.text = actions[action]
            }
        }
        binding.tvBottomCenter.setOnClickListener {
            selectAction { action ->
                putPrefInt(PreferKey.clickActionBC, action)
                (it as? TextView)?.text = actions[action]
            }
        }
        binding.tvBottomRight.setOnClickListener {
            selectAction { action ->
                putPrefInt(PreferKey.clickActionBR, action)
                (it as? TextView)?.text = actions[action]
            }
        }
    }

    private fun selectAction(success: (action: Int) -> Unit) {
        selector(
            getString(R.string.select_action),
            actions.values.toList()
        ) { _, index ->
            success.invoke(actions.keys.toList()[index])
        }
    }

}