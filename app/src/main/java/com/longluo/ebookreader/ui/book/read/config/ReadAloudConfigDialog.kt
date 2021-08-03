package com.longluo.ebookreader.ui.book.read.config

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BasePreferenceFragment
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.constant.PreferKey
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.lib.theme.ATH
import com.longluo.ebookreader.lib.theme.backgroundColor
import com.longluo.ebookreader.service.BaseReadAloudService
import com.longluo.ebookreader.service.help.ReadAloud
import com.longluo.ebookreader.utils.getPrefLong
import com.longluo.ebookreader.utils.getSize
import com.longluo.ebookreader.utils.postEvent
import splitties.init.appCtx

class ReadAloudConfigDialog : DialogFragment() {
    private val readAloudPreferTag = "readAloudPreferTag"

    override fun onStart() {
        super.onStart()
        val dm = requireActivity().getSize()
        dialog?.window?.let {
            it.setBackgroundDrawableResource(R.color.transparent)
            it.setLayout((dm.widthPixels * 0.9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = LinearLayout(requireContext())
        view.setBackgroundColor(requireContext().backgroundColor)
        view.id = R.id.tag1
        container?.addView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var preferenceFragment = childFragmentManager.findFragmentByTag(readAloudPreferTag)
        if (preferenceFragment == null) preferenceFragment = ReadAloudPreferenceFragment()
        childFragmentManager.beginTransaction()
            .replace(view.id, preferenceFragment, readAloudPreferTag)
            .commit()
    }

    class ReadAloudPreferenceFragment : BasePreferenceFragment(),
        SharedPreferences.OnSharedPreferenceChangeListener {

        private val speakEngineSummary: String
            get() {
                val eid = appCtx.getPrefLong(PreferKey.speakEngine)
                val ht = appDb.httpTTSDao.get(eid)
                return ht?.name ?: getString(R.string.system_tts)
            }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.pref_config_aloud)
            upPreferenceSummary(
                findPreference(PreferKey.speakEngine),
                speakEngineSummary
            )
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            ATH.applyEdgeEffectColor(listView)
        }

        override fun onResume() {
            super.onResume()
            preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
            super.onPause()
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            when (preference?.key) {
                PreferKey.speakEngine ->
                    SpeakEngineDialog().show(childFragmentManager, "speakEngine")
            }
            return super.onPreferenceTreeClick(preference)
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            when (key) {
                PreferKey.readAloudByPage -> {
                    if (BaseReadAloudService.isRun) {
                        postEvent(EventBus.MEDIA_BUTTON, false)
                    }
                }
                PreferKey.speakEngine -> {
                    upPreferenceSummary(findPreference(key), speakEngineSummary)
                    ReadAloud.upReadAloudClass()
                }
            }
        }

        private fun upPreferenceSummary(preference: Preference?, value: String) {
            when (preference) {
                is ListPreference -> {
                    val index = preference.findIndexOfValue(value)
                    preference.summary = if (index >= 0) preference.entries[index] else null
                }
                else -> {
                    preference?.summary = value
                }
            }
        }

    }
}