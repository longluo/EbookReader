package com.longluo.ebookreader.ui.book.read.config

import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import com.longluo.ebookreader.R
import com.longluo.ebookreader.constant.PreferKey
import com.longluo.ebookreader.databinding.DialogPageKeyBinding
import com.longluo.ebookreader.lib.theme.backgroundColor
import com.longluo.ebookreader.utils.getPrefString
import com.longluo.ebookreader.utils.hideSoftInput
import com.longluo.ebookreader.utils.putPrefString
import splitties.views.onClick


class PageKeyDialog(context: Context) : Dialog(context, R.style.AppTheme_AlertDialog) {

    private val binding = DialogPageKeyBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        binding.run {
            contentView.setBackgroundColor(context.backgroundColor)
            etPrev.setText(context.getPrefString(PreferKey.prevKeys))
            etNext.setText(context.getPrefString(PreferKey.nextKeys))
            tvReset.onClick {
                etPrev.setText("")
                etNext.setText("")
            }
            tvOk.setOnClickListener {
                context.putPrefString(PreferKey.prevKeys, etPrev.text?.toString())
                context.putPrefString(PreferKey.nextKeys, etNext.text?.toString())
                dismiss()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode != KeyEvent.KEYCODE_BACK && keyCode != KeyEvent.KEYCODE_DEL) {
            if (binding.etPrev.hasFocus()) {
                val editableText = binding.etPrev.editableText
                if (editableText.isEmpty() or editableText.endsWith(",")) {
                    editableText.append(keyCode.toString())
                } else {
                    editableText.append(",").append(keyCode.toString())
                }
                return true
            } else if (binding.etNext.hasFocus()) {
                val editableText = binding.etNext.editableText
                if (editableText.isEmpty() or editableText.endsWith(",")) {
                    editableText.append(keyCode.toString())
                } else {
                    editableText.append(",").append(keyCode.toString())
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun dismiss() {
        super.dismiss()
        currentFocus?.hideSoftInput()
    }

}