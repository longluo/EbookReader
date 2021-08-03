package com.longluo.ebookreader.ui.book.info

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.VMBaseActivity
import com.longluo.ebookreader.constant.BookType
import com.longluo.ebookreader.constant.Theme
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.Book
import com.longluo.ebookreader.data.entities.BookChapter
import com.longluo.ebookreader.databinding.ActivityBookInfoBinding
import com.longluo.ebookreader.help.BlurTransformation
import com.longluo.ebookreader.help.ImageLoader
import com.longluo.ebookreader.lib.dialogs.alert
import com.longluo.ebookreader.lib.theme.backgroundColor
import com.longluo.ebookreader.lib.theme.bottomBackground
import com.longluo.ebookreader.lib.theme.getPrimaryTextColor
import com.longluo.ebookreader.ui.book.audio.AudioPlayActivity
import com.longluo.ebookreader.ui.book.changecover.ChangeCoverDialog
import com.longluo.ebookreader.ui.book.changesource.ChangeSourceDialog
import com.longluo.ebookreader.ui.book.group.GroupSelectDialog
import com.longluo.ebookreader.ui.book.info.edit.BookInfoEditActivity
import com.longluo.ebookreader.ui.book.read.ReadBookActivity
import com.longluo.ebookreader.ui.book.search.SearchActivity
import com.longluo.ebookreader.ui.book.source.edit.BookSourceEditActivity
import com.longluo.ebookreader.ui.book.toc.TocActivityResult
import com.longluo.ebookreader.ui.widget.image.CoverImageView
import com.longluo.ebookreader.utils.*
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BookInfoActivity :
    VMBaseActivity<ActivityBookInfoBinding, BookInfoViewModel>(toolBarTheme = Theme.Dark),
    GroupSelectDialog.CallBack,
    ChangeSourceDialog.CallBack,
    ChangeCoverDialog.CallBack {

    private val tocActivityResult = registerForActivityResult(TocActivityResult()) {
        it?.let {
            viewModel.bookData.value?.let { book ->
                launch {
                    withContext(IO) {
                        viewModel.durChapterIndex = it.first
                        book.durChapterIndex = it.first
                        book.durChapterPos = it.second
                        appDb.bookDao.update(book)
                    }
                    viewModel.chapterListData.value?.let { chapterList ->
                        binding.tvToc.text =
                            getString(R.string.toc_s, chapterList[book.durChapterIndex].title)
                    }
                    startReadActivity(book)
                }
            }
        } ?: let {
            if (!viewModel.inBookshelf) {
                viewModel.delBook()
            }
        }
    }
    private val readBookResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.refreshData(intent)
        if (it.resultCode == RESULT_OK) {
            viewModel.inBookshelf = true
            upTvBookshelf()
        }
    }
    private val infoEditResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            viewModel.upEditBook()
        }
    }

    override val binding by viewBinding(ActivityBookInfoBinding::inflate)
    override val viewModel by viewModels<BookInfoViewModel>()

    @SuppressLint("PrivateResource")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.titleBar.transparent()
        binding.arcView.setBgColor(backgroundColor)
        binding.llInfo.setBackgroundColor(backgroundColor)
        binding.scrollView.setBackgroundColor(backgroundColor)
        binding.flAction.setBackgroundColor(bottomBackground)
        binding.tvShelf.setTextColor(getPrimaryTextColor(ColorUtils.isColorLight(bottomBackground)))
        binding.tvToc.text = getString(R.string.toc_s, getString(R.string.loading))
        viewModel.bookData.observe(this, { showBook(it) })
        viewModel.chapterListData.observe(this, { upLoading(false, it) })
        viewModel.initData(intent)
        initOnClick()
    }

    override fun onCompatCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.book_info, menu)
        return super.onCompatCreateOptionsMenu(menu)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        menu.findItem(R.id.menu_can_update)?.isChecked =
            viewModel.bookData.value?.canUpdate ?: true
        menu.findItem(R.id.menu_login)?.isVisible =
            !viewModel.bookSource?.loginUrl.isNullOrBlank()
        return super.onMenuOpened(featureId, menu)
    }

    override fun onCompatOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit -> {
                if (viewModel.inBookshelf) {
                    viewModel.bookData.value?.let {
                        infoEditResult.launch(
                            Intent(this, BookInfoEditActivity::class.java)
                                .putExtra("bookUrl", it.bookUrl)
                        )
                    }
                } else {
                    toastOnUi(R.string.after_add_bookshelf)
                }
            }
            R.id.menu_share_it -> {
                viewModel.bookData.value?.let {
                    val bookJson = GSON.toJson(it)
                    val shareStr = "${it.bookUrl}#$bookJson"
                    shareWithQr(shareStr, it.name)
                }
            }
            R.id.menu_refresh -> {
                upLoading(true)
                viewModel.bookData.value?.let {
                    if (it.isLocalBook()) {
                        it.tocUrl = ""
                    }
                    viewModel.loadBookInfo(it, false)
                }
            }
            R.id.menu_copy_book_url -> viewModel.bookData.value?.bookUrl?.let {
                sendToClip(it)
            } ?: toastOnUi(R.string.no_book)
            R.id.menu_copy_toc_url -> viewModel.bookData.value?.tocUrl?.let {
                sendToClip(it)
            } ?: toastOnUi(R.string.no_book)
            R.id.menu_can_update -> {
                if (viewModel.inBookshelf) {
                    viewModel.bookData.value?.let {
                        it.canUpdate = !it.canUpdate
                        viewModel.saveBook()
                    }
                } else {
                    toastOnUi(R.string.after_add_bookshelf)
                }
            }
            R.id.menu_clear_cache -> viewModel.clearCache()
        }
        return super.onCompatOptionsItemSelected(item)
    }

    private fun showBook(book: Book) = binding.run {
        showCover(book)
        tvName.text = book.name
        tvAuthor.text = getString(R.string.author_show, book.getRealAuthor())
        tvOrigin.text = getString(R.string.origin_show, book.originName)
        tvLasted.text = getString(R.string.lasted_show, book.latestChapterTitle)
        tvIntro.text = book.getDisplayIntro()
        upTvBookshelf()
        val kinds = book.getKindList()
        if (kinds.isEmpty()) {
            lbKind.gone()
        } else {
            lbKind.visible()
            lbKind.setLabels(kinds)
        }
        upGroup(book.group)
    }

    private fun showCover(book: Book) {
        binding.ivCover.load(book.getDisplayCover(), book.name, book.author)
        ImageLoader.load(this, book.getDisplayCover())
            .transition(DrawableTransitionOptions.withCrossFade(1500))
            .thumbnail(defaultCover())
            .apply(bitmapTransform(BlurTransformation(this, 25)))
            .into(binding.bgBook)  //模糊、渐变、缩小效果
    }

    private fun defaultCover(): RequestBuilder<Drawable> {
        return ImageLoader.load(this, CoverImageView.defaultDrawable)
            .apply(bitmapTransform(BlurTransformation(this, 25)))
    }

    private fun upLoading(isLoading: Boolean, chapterList: List<BookChapter>? = null) {
        when {
            isLoading -> {
                binding.tvToc.text = getString(R.string.toc_s, getString(R.string.loading))
            }
            chapterList.isNullOrEmpty() -> {
                binding.tvToc.text = getString(R.string.toc_s, getString(R.string.error_load_toc))
            }
            else -> {
                viewModel.bookData.value?.let {
                    if (it.durChapterIndex < chapterList.size) {
                        binding.tvToc.text =
                            getString(R.string.toc_s, chapterList[it.durChapterIndex].title)
                    } else {
                        binding.tvToc.text = getString(R.string.toc_s, chapterList.last().title)
                    }
                }
            }
        }
    }

    private fun upTvBookshelf() {
        if (viewModel.inBookshelf) {
            binding.tvShelf.text = getString(R.string.remove_from_bookshelf)
        } else {
            binding.tvShelf.text = getString(R.string.add_to_shelf)
        }
    }

    private fun upGroup(groupId: Long) {
        viewModel.loadGroup(groupId) {
            if (it.isNullOrEmpty()) {
                binding.tvGroup.text = getString(R.string.group_s, getString(R.string.no_group))
            } else {
                binding.tvGroup.text = getString(R.string.group_s, it)
            }
        }
    }

    private fun initOnClick() = binding.run {
        ivCover.setOnClickListener {
            viewModel.bookData.value?.let {
                ChangeCoverDialog.show(supportFragmentManager, it.name, it.author)
            }
        }
        tvRead.setOnClickListener {
            viewModel.bookData.value?.let {
                readBook(it)
            }
        }
        tvShelf.setOnClickListener {
            if (viewModel.inBookshelf) {
                deleteBook()
            } else {
                viewModel.addToBookshelf {
                    upTvBookshelf()
                }
            }
        }
        tvOrigin.setOnClickListener {
            viewModel.bookData.value?.let {
                startActivity<BookSourceEditActivity> {
                    putExtra("data", it.origin)
                }
            }
        }
        tvChangeSource.setOnClickListener {
            viewModel.bookData.value?.let {
                ChangeSourceDialog.show(supportFragmentManager, it.name, it.author)
            }
        }
        tvTocView.setOnClickListener {
            if (!viewModel.inBookshelf) {
                viewModel.saveBook {
                    viewModel.saveChapterList {
                        openChapterList()
                    }
                }
            } else {
                openChapterList()
            }
        }
        tvChangeGroup.setOnClickListener {
            viewModel.bookData.value?.let {
                GroupSelectDialog.show(supportFragmentManager, it.group)
            }
        }
        tvAuthor.setOnClickListener {
            startActivity<SearchActivity> {
                putExtra("key", viewModel.bookData.value?.author)
            }
        }
        tvName.setOnClickListener {
            startActivity<SearchActivity> {
                putExtra("key", viewModel.bookData.value?.name)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun deleteBook() {
        viewModel.bookData.value?.let {
            if (it.isLocalBook()) {
                alert(
                    titleResource = R.string.sure,
                    messageResource = R.string.sure_del
                ) {
                    val checkBox = CheckBox(this@BookInfoActivity).apply {
                        setText(R.string.delete_book_file)
                    }
                    val view = LinearLayout(this@BookInfoActivity).apply {
                        setPadding(16.dp, 0, 16.dp, 0)
                        addView(checkBox)
                    }
                    customView { view }
                    positiveButton(R.string.yes) {
                        viewModel.delBook(checkBox.isChecked) {
                            finish()
                        }
                    }
                    negativeButton(R.string.no)
                }.show()
            } else {
                viewModel.delBook {
                    upTvBookshelf()
                }
            }
        }
    }

    private fun openChapterList() {
        if (viewModel.chapterListData.value.isNullOrEmpty()) {
            toastOnUi(R.string.chapter_list_empty)
            return
        }
        viewModel.bookData.value?.let {
            tocActivityResult.launch(it.bookUrl)
        }
    }

    private fun readBook(book: Book) {
        if (!viewModel.inBookshelf) {
            viewModel.saveBook {
                viewModel.saveChapterList {
                    startReadActivity(book)
                }
            }
        } else {
            viewModel.saveBook {
                startReadActivity(book)
            }
        }
    }

    private fun startReadActivity(book: Book) {
        when (book.type) {
            BookType.audio -> readBookResult.launch(
                Intent(this, AudioPlayActivity::class.java)
                    .putExtra("bookUrl", book.bookUrl)
                    .putExtra("inBookshelf", viewModel.inBookshelf)
            )
            else -> readBookResult.launch(
                Intent(this, ReadBookActivity::class.java)
                    .putExtra("bookUrl", book.bookUrl)
                    .putExtra("inBookshelf", viewModel.inBookshelf)
            )
        }
    }

    override val oldBook: Book?
        get() = viewModel.bookData.value

    override fun changeTo(book: Book) {
        upLoading(true)
        viewModel.changeTo(book)
    }

    override fun coverChangeTo(coverUrl: String) {
        viewModel.bookData.value?.let {
            it.coverUrl = coverUrl
            viewModel.saveBook()
            showCover(it)
        }
    }

    override fun upGroup(requestCode: Int, groupId: Long) {
        upGroup(groupId)
        viewModel.bookData.value?.group = groupId
        if (viewModel.inBookshelf) {
            viewModel.saveBook()
        }
    }

}