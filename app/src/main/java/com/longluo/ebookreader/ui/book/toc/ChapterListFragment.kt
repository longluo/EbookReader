package com.longluo.ebookreader.ui.book.toc

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.VMBaseFragment
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.Book
import com.longluo.ebookreader.data.entities.BookChapter
import com.longluo.ebookreader.databinding.FragmentChapterListBinding
import com.longluo.ebookreader.help.BookHelp
import com.longluo.ebookreader.lib.theme.bottomBackground
import com.longluo.ebookreader.lib.theme.getPrimaryTextColor
import com.longluo.ebookreader.ui.widget.recycler.UpLinearLayoutManager
import com.longluo.ebookreader.ui.widget.recycler.VerticalDivider
import com.longluo.ebookreader.utils.ColorUtils
import com.longluo.ebookreader.utils.observeEvent
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

class ChapterListFragment : VMBaseFragment<TocViewModel>(R.layout.fragment_chapter_list),
    ChapterListAdapter.Callback,
    TocViewModel.ChapterListCallBack {
    override val viewModel by activityViewModels<TocViewModel>()
    private val binding by viewBinding(FragmentChapterListBinding::bind)
    lateinit var adapter: ChapterListAdapter
    private var durChapterIndex = 0
    private lateinit var mLayoutManager: UpLinearLayoutManager
    private var tocFlowJob: Job? = null
    private var scrollToDurChapter = false

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        viewModel.chapterCallBack = this@ChapterListFragment
        val bbg = bottomBackground
        val btc = requireContext().getPrimaryTextColor(ColorUtils.isColorLight(bbg))
        llChapterBaseInfo.setBackgroundColor(bbg)
        tvCurrentChapterInfo.setTextColor(btc)
        ivChapterTop.setColorFilter(btc)
        ivChapterBottom.setColorFilter(btc)
        initRecyclerView()
        initView()
        viewModel.bookData.observe(this@ChapterListFragment) {
            initBook(it)
        }
    }

    private fun initRecyclerView() {
        adapter = ChapterListAdapter(requireContext(), this)
        mLayoutManager = UpLinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.addItemDecoration(VerticalDivider(requireContext()))
        binding.recyclerView.adapter = adapter
    }

    private fun initView() = binding.run {
        ivChapterTop.setOnClickListener { mLayoutManager.scrollToPositionWithOffset(0, 0) }
        ivChapterBottom.setOnClickListener {
            if (adapter.itemCount > 0) {
                mLayoutManager.scrollToPositionWithOffset(adapter.itemCount - 1, 0)
            }
        }
        tvCurrentChapterInfo.setOnClickListener {
            mLayoutManager.scrollToPositionWithOffset(durChapterIndex, 0)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initBook(book: Book) {
        launch {
            upChapterList(null)
            durChapterIndex = book.durChapterIndex
            binding.tvCurrentChapterInfo.text =
                "${book.durChapterTitle}(${book.durChapterIndex + 1}/${book.totalChapterNum})"
            initCacheFileNames(book)
        }
    }

    private fun initCacheFileNames(book: Book) {
        launch(IO) {
            adapter.cacheFileNames.addAll(BookHelp.getChapterFiles(book))
            withContext(Main) {
                adapter.notifyItemRangeChanged(0, adapter.itemCount, true)
            }
        }
    }

    override fun observeLiveBus() {
        observeEvent<BookChapter>(EventBus.SAVE_CONTENT) { chapter ->
            viewModel.bookData.value?.bookUrl?.let { bookUrl ->
                if (chapter.bookUrl == bookUrl) {
                    adapter.cacheFileNames.add(chapter.getFileName())
                    adapter.notifyItemChanged(chapter.index, true)
                }
            }
        }
    }

    override fun upChapterList(searchKey: String?) {
        tocFlowJob?.cancel()
        tocFlowJob = launch {
            when {
                searchKey.isNullOrBlank() -> appDb.bookChapterDao.flowByBook(viewModel.bookUrl)
                else -> appDb.bookChapterDao.flowSearch(viewModel.bookUrl, searchKey)
            }.collect {
                adapter.setItems(it)
                if (searchKey.isNullOrBlank() && !scrollToDurChapter) {
                    mLayoutManager.scrollToPositionWithOffset(durChapterIndex, 0)
                    scrollToDurChapter = true
                }
            }
        }
    }

    override val isLocalBook: Boolean
        get() = viewModel.bookData.value?.isLocalBook() == true

    override fun durChapterIndex(): Int {
        return min(durChapterIndex, adapter.itemCount)
    }

    override fun openChapter(bookChapter: BookChapter) {
        activity?.run {
            setResult(RESULT_OK, Intent().putExtra("index", bookChapter.index))
            finish()
        }
    }

}