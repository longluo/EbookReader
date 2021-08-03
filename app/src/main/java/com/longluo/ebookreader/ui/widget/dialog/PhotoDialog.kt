package com.longluo.ebookreader.ui.widget.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseDialogFragment
import com.longluo.ebookreader.databinding.DialogPhotoViewBinding
import com.longluo.ebookreader.service.help.ReadBook
import com.longluo.ebookreader.ui.book.read.page.provider.ImageProvider
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding


class PhotoDialog : BaseDialogFragment() {

    companion object {

        fun show(
            fragmentManager: FragmentManager,
            chapterIndex: Int,
            src: String,
        ) {
            PhotoDialog().apply {
                val bundle = Bundle()
                bundle.putInt("chapterIndex", chapterIndex)
                bundle.putString("src", src)
                arguments = bundle
            }.show(fragmentManager, "photoDialog")
        }

    }

    private val binding by viewBinding(DialogPhotoViewBinding::bind)

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.dialog_photo_view, container)
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val chapterIndex = it.getInt("chapterIndex")
            val src = it.getString("src")
            ReadBook.book?.let { book ->
                src?.let {
                    execute {
                        ImageProvider.getImage(book, chapterIndex, src)
                    }.onSuccess { bitmap ->
                        if (bitmap != null) {
                            binding.photoView.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }

    }

}
