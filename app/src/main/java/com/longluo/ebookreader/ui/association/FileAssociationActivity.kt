package com.longluo.ebookreader.ui.association

import android.os.Bundle
import androidx.activity.viewModels
import com.longluo.ebookreader.base.VMBaseActivity
import com.longluo.ebookreader.databinding.ActivityTranslucenceBinding
import com.longluo.ebookreader.utils.startActivity
import com.longluo.ebookreader.utils.toastOnUi
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding

class FileAssociationActivity :
    VMBaseActivity<ActivityTranslucenceBinding, FileAssociationViewModel>() {

    override val binding by viewBinding(ActivityTranslucenceBinding::inflate)

    override val viewModel by viewModels<FileAssociationViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.rotateLoading.show()
        viewModel.onLineImportLive.observe(this) {
            startActivity<OnLineImportActivity> {
                data = it
            }
            finish()
        }
        viewModel.importBookSourceLive.observe(this) {
            binding.rotateLoading.hide()
            ImportBookSourceDialog.start(supportFragmentManager, it, true)
        }
        viewModel.importRssSourceLive.observe(this) {
            binding.rotateLoading.hide()
            ImportRssSourceDialog.start(supportFragmentManager, it, true)
        }
        viewModel.importReplaceRuleLive.observe(this) {
            binding.rotateLoading.hide()
            ImportReplaceRuleDialog.start(supportFragmentManager, it, true)
        }
        viewModel.errorLiveData.observe(this, {
            binding.rotateLoading.hide()
            toastOnUi(it)
            finish()
        })
        viewModel.successLiveData.observe(this, {
            binding.rotateLoading.hide()
            startActivity(it)
            finish()
        })
        intent.data?.let { data ->
            viewModel.dispatchIndent(data)
        }
    }

}
