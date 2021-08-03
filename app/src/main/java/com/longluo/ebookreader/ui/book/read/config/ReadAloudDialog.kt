package com.longluo.ebookreader.ui.book.read.config

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseDialogFragment
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.databinding.DialogReadAloudBinding
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.lib.theme.bottomBackground
import com.longluo.ebookreader.lib.theme.getPrimaryTextColor
import com.longluo.ebookreader.service.BaseReadAloudService
import com.longluo.ebookreader.service.help.ReadAloud
import com.longluo.ebookreader.service.help.ReadBook
import com.longluo.ebookreader.ui.book.read.ReadBookActivity
import com.longluo.ebookreader.ui.widget.seekbar.SeekBarChangeListener
import com.longluo.ebookreader.utils.ColorUtils
import com.longluo.ebookreader.utils.getPrefBoolean
import com.longluo.ebookreader.utils.observeEvent
import com.longluo.ebookreader.utils.putPrefBoolean
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding


class ReadAloudDialog : BaseDialogFragment() {
    private var callBack: CallBack? = null
    private val binding by viewBinding(DialogReadAloudBinding::bind)

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
        return inflater.inflate(R.layout.dialog_read_aloud, container)
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        val bg = requireContext().bottomBackground
        val isLight = ColorUtils.isColorLight(bg)
        val textColor = requireContext().getPrimaryTextColor(isLight)
        binding.run {
            rootView.setBackgroundColor(bg)
            tvPre.setTextColor(textColor)
            tvNext.setTextColor(textColor)
            ivPlayPrev.setColorFilter(textColor)
            ivPlayPause.setColorFilter(textColor)
            ivPlayNext.setColorFilter(textColor)
            ivStop.setColorFilter(textColor)
            ivTimer.setColorFilter(textColor)
            tvTimer.setTextColor(textColor)
            tvTtsSpeed.setTextColor(textColor)
            ivCatalog.setColorFilter(textColor)
            tvCatalog.setTextColor(textColor)
            ivMainMenu.setColorFilter(textColor)
            tvMainMenu.setTextColor(textColor)
            ivToBackstage.setColorFilter(textColor)
            tvToBackstage.setTextColor(textColor)
            ivSetting.setColorFilter(textColor)
            tvSetting.setTextColor(textColor)
            cbTtsFollowSys.setTextColor(textColor)
        }
        initData()
        initEvent()
    }

    private fun initData() = binding.run {
        upPlayState()
        upTimerText(BaseReadAloudService.timeMinute)
        seekTimer.progress = BaseReadAloudService.timeMinute
        cbTtsFollowSys.isChecked = requireContext().getPrefBoolean("ttsFollowSys", true)
        seekTtsSpeechRate.isEnabled = !cbTtsFollowSys.isChecked
        seekTtsSpeechRate.progress = AppConfig.ttsSpeechRate
    }

    private fun initEvent() = binding.run {
        llMainMenu.setOnClickListener {
            callBack?.showMenuBar()
            dismissAllowingStateLoss()
        }
        llSetting.setOnClickListener {
            ReadAloudConfigDialog().show(childFragmentManager, "readAloudConfigDialog")
        }
        tvPre.setOnClickListener { ReadBook.moveToPrevChapter(upContent = true, toLast = false) }
        tvNext.setOnClickListener { ReadBook.moveToNextChapter(true) }
        ivStop.setOnClickListener {
            ReadAloud.stop(requireContext())
            dismissAllowingStateLoss()
        }
        ivPlayPause.setOnClickListener { callBack?.onClickReadAloud() }
        ivPlayPrev.setOnClickListener { ReadAloud.prevParagraph(requireContext()) }
        ivPlayNext.setOnClickListener { ReadAloud.nextParagraph(requireContext()) }
        llCatalog.setOnClickListener { callBack?.openChapterList() }
        llToBackstage.setOnClickListener { callBack?.finish() }
        cbTtsFollowSys.setOnCheckedChangeListener { _, isChecked ->
            requireContext().putPrefBoolean("ttsFollowSys", isChecked)
            seekTtsSpeechRate.isEnabled = !isChecked
            upTtsSpeechRate()
        }
        seekTtsSpeechRate.setOnSeekBarChangeListener(object : SeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                AppConfig.ttsSpeechRate = seekBar.progress
                upTtsSpeechRate()
            }
        })
        seekTimer.setOnSeekBarChangeListener(object : SeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                upTimerText(progress)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                ReadAloud.setTimer(requireContext(), seekTimer.progress)
            }
        })
    }

    private fun upPlayState() {
        if (!BaseReadAloudService.pause) {
            binding.ivPlayPause.setImageResource(R.drawable.ic_pause_24dp)
        } else {
            binding.ivPlayPause.setImageResource(R.drawable.ic_play_24dp)
        }
        val bg = requireContext().bottomBackground
        val isLight = ColorUtils.isColorLight(bg)
        val textColor = requireContext().getPrimaryTextColor(isLight)
        binding.ivPlayPause.setColorFilter(textColor)
    }

    private fun upTimerText(timeMinute: Int) {
        binding.tvTimer.text = requireContext().getString(R.string.timer_m, timeMinute)
    }

    private fun upTtsSpeechRate() {
        ReadAloud.upTtsSpeechRate(requireContext())
        if (!BaseReadAloudService.pause) {
            ReadAloud.pause(requireContext())
            ReadAloud.resume(requireContext())
        }
    }

    override fun observeLiveBus() {
        observeEvent<Int>(EventBus.ALOUD_STATE) { upPlayState() }
        observeEvent<Int>(EventBus.TTS_DS) { binding.seekTimer.progress = it }
    }

    interface CallBack {
        fun showMenuBar()
        fun openChapterList()
        fun onClickReadAloud()
        fun finish()
    }
}