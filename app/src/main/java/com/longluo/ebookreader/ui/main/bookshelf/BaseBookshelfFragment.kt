package com.longluo.ebookreader.ui.main.bookshelf

import android.annotation.SuppressLint
import android.view.MenuItem
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.VMBaseFragment
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.constant.PreferKey
import com.longluo.ebookreader.data.entities.Book
import com.longluo.ebookreader.databinding.DialogBookshelfConfigBinding
import com.longluo.ebookreader.databinding.DialogEditTextBinding
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.lib.dialogs.alert
import com.longluo.ebookreader.ui.book.arrange.ArrangeBookActivity
import com.longluo.ebookreader.ui.book.cache.CacheActivity
import com.longluo.ebookreader.ui.book.group.GroupManageDialog
import com.longluo.ebookreader.ui.book.local.ImportBookActivity
import com.longluo.ebookreader.ui.book.search.SearchActivity
import com.longluo.ebookreader.ui.document.FilePicker
import com.longluo.ebookreader.ui.document.FilePickerParam
import com.longluo.ebookreader.ui.main.MainViewModel
import com.longluo.ebookreader.utils.*

abstract class BaseBookshelfFragment(layoutId: Int) : VMBaseFragment<BookshelfViewModel>(layoutId) {

    val activityViewModel by activityViewModels<MainViewModel>()
    override val viewModel by viewModels<BookshelfViewModel>()

    private val importBookshelf = registerForActivityResult(FilePicker()) {
        it?.readText(requireContext())?.let { text ->
            viewModel.importBookshelf(text, groupId)
        }
    }

    abstract val groupId: Long
    abstract val books: List<Book>

    abstract fun gotoTop()

    override fun onCompatOptionsItemSelected(item: MenuItem) {
        super.onCompatOptionsItemSelected(item)
        when (item.itemId) {
            R.id.menu_search -> startActivity<SearchActivity>()
            R.id.menu_update_toc -> activityViewModel.upToc(books)
            R.id.menu_bookshelf_layout -> configBookshelf()
            R.id.menu_group_manage -> GroupManageDialog()
                .show(childFragmentManager, "groupManageDialog")
            R.id.menu_add_local -> startActivity<ImportBookActivity>()
            R.id.menu_add_url -> addBookByUrl()
            R.id.menu_arrange_bookshelf -> startActivity<ArrangeBookActivity> {
                putExtra("groupId", groupId)
            }
            R.id.menu_download -> startActivity<CacheActivity> {
                putExtra("groupId", groupId)
            }
            R.id.menu_export_bookshelf -> viewModel.exportBookshelf(books) {
                activity?.share(it)
            }
            R.id.menu_import_bookshelf -> importBookshelfAlert(groupId)
        }
    }

    @SuppressLint("InflateParams")
    fun addBookByUrl() {
        alert(titleResource = R.string.add_book_url) {
            val alertBinding = DialogEditTextBinding.inflate(layoutInflater)
            customView { alertBinding.root }
            okButton {
                alertBinding.editView.text?.toString()?.let {
                    viewModel.addBookByUrl(it)
                }
            }
            noButton()
        }.show()
    }

    @SuppressLint("InflateParams")
    fun configBookshelf() {
        alert(titleResource = R.string.bookshelf_layout) {
            val bookshelfLayout = getPrefInt(PreferKey.bookshelfLayout)
            val bookshelfSort = getPrefInt(PreferKey.bookshelfSort)
            val alertBinding =
                DialogBookshelfConfigBinding.inflate(layoutInflater)
                    .apply {
                        spGroupStyle.setSelection(AppConfig.bookGroupStyle)
                        swShowUnread.isChecked = AppConfig.showUnread
                        rgLayout.checkByIndex(bookshelfLayout)
                        rgSort.checkByIndex(bookshelfSort)
                    }
            customView { alertBinding.root }
            okButton {
                alertBinding.apply {
                    if (AppConfig.bookGroupStyle != spGroupStyle.selectedItemPosition) {
                        AppConfig.bookGroupStyle = spGroupStyle.selectedItemPosition
                        postEvent(EventBus.NOTIFY_MAIN, false)
                    }
                    if (AppConfig.showUnread != swShowUnread.isChecked) {
                        AppConfig.showUnread = swShowUnread.isChecked
                        postEvent(EventBus.BOOKSHELF_REFRESH, "")
                    }
                    var changed = false
                    if (bookshelfLayout != rgLayout.getCheckedIndex()) {
                        putPrefInt(PreferKey.bookshelfLayout, rgLayout.getCheckedIndex())
                        changed = true
                    }
                    if (bookshelfSort != rgSort.getCheckedIndex()) {
                        putPrefInt(PreferKey.bookshelfSort, rgSort.getCheckedIndex())
                        changed = true
                    }
                    if (changed) {
                        postEvent(EventBus.RECREATE, "")
                    }
                }
            }
            noButton()
        }.show()
    }


    private fun importBookshelfAlert(groupId: Long) {
        alert(titleResource = R.string.import_bookshelf) {
            val alertBinding = DialogEditTextBinding.inflate(layoutInflater).apply {
                editView.hint = "url/json"
            }
            customView { alertBinding.root }
            okButton {
                alertBinding.editView.text?.toString()?.let {
                    viewModel.importBookshelf(it, groupId)
                }
            }
            noButton()
            neutralButton(R.string.select_file) {
                importBookshelf.launch(
                    FilePickerParam(
                        mode = FilePicker.FILE,
                        allowExtensions = arrayOf("txt", "json")
                    )
                )
            }
        }.show()
    }

}