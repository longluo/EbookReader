package com.longluo.ebookreader.ui.book.read.config

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseDialogFragment
import com.longluo.ebookreader.databinding.DialogAutoReadBinding
import com.longluo.ebookreader.help.ReadBookConfig
import com.longluo.ebookreader.lib.theme.bottomBackground
import com.longluo.ebookreader.lib.theme.getPrimaryTextColor
import com.longluo.ebookreader.service.BaseReadAloudService
import com.longluo.ebookreader.service.help.ReadAloud
import com.longluo.ebookreader.ui.book.read.ReadBookActivity
import com.longluo.ebookreader.ui.widget.seekbar.SeekBarChangeListener
import com.longluo.ebookreader.utils.ColorUtils
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding


class AutoReadDialog : BaseDialogFragment() {
    var callBack: CallBack? = null

    private val binding by viewBinding(DialogAutoReadBinding::bind)

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            it.setBackgroundDrawableResource(R.color.background)
            it.decorView.setPadding(0, 0, 0, 0)
            val attr = it.attributes
            attr.dimAmount = 0.0f
            attr.gravity = Gravity.BOTTOM
            it.attributes = attr
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as ReadBookActivity).bottomDialog--
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as ReadBookActivity).bottomDialog++
        callBack = activity as? CallBack
        return inflater.inflate(R.layout.dialog_auto_read, container)
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        val bg = requireContext().bottomBackground
        val isLight = ColorUtils.isColorLight(bg)
        val textColor = requireContext().getPrimaryTextColor(isLight)
        root.setBackgroundColor(bg)
        tvReadSpeedTitle.setTextColor(textColor)
        tvReadSpeed.setTextColor(textColor)
        ivCatalog.setColorFilter(textColor)
        tvCatalog.setTextColor(textColor)
        ivMainMenu.setColorFilter(textColor)
        tvMainMenu.setTextColor(textColor)
        ivAutoPageStop.setColorFilter(textColor)
        tvAutoPageStop.setTextColor(textColor)
        ivSetting.setColorFilter(textColor)
        tvSetting.setTextColor(textColor)
        initOnChange()
        initData()
        initEvent()
    }

    private fun initData() {
        val speed = if (ReadBookConfig.autoReadSpeed < 10) 10 else ReadBookConfig.autoReadSpeed
        binding.tvReadSpeed.text = String.format("%ds", speed)
        binding.seekAutoRead.progress = speed
    }

    private fun initOnChange() {
        binding.seekAutoRead.setOnSeekBarChangeListener(object : SeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val speed = if (progress < 10) 10 else progress
                binding.tvReadSpeed.text = String.format("%ds", speed)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                ReadBookConfig.autoReadSpeed =
                    if (binding.seekAutoRead.progress < 10) 10 else binding.seekAutoRead.progress
                upTtsSpeechRate()
            }
        })
    }

    private fun initEvent() {
        binding.llMainMenu.setOnClickListener {
            callBack?.showMenuBar()
            dismissAllowingStateLoss()
        }
        binding.llSetting.setOnClickListener {
            ReadAloudConfigDialog().show(childFragmentManager, "readAloudConfigDialog")
        }
        binding.llCatalog.setOnClickListener { callBack?.openChapterList() }
        binding.llAutoPageStop.setOnClickListener {
            callBack?.autoPageStop()
            dismissAllowingStateLoss()
        }
    }

    private fun upTtsSpeechRate() {
        ReadAloud.upTtsSpeechRate(requireContext())
        if (!BaseReadAloudService.pause) {
            ReadAloud.pause(requireContext())
            ReadAloud.resume(requireContext())
        }
    }

    interface CallBack {
        fun showMenuBar()
        fun openChapterList()
        fun autoPageStop()
    }
}