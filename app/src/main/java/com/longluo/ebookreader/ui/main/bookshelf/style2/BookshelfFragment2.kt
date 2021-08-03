package com.longluo.ebookreader.ui.main.bookshelf.style2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.longluo.ebookreader.R
import com.longluo.ebookreader.constant.AppConst
import com.longluo.ebookreader.constant.BookType
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.constant.PreferKey
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.Book
import com.longluo.ebookreader.data.entities.BookGroup
import com.longluo.ebookreader.databinding.FragmentBookshelf1Binding
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.lib.theme.ATH
import com.longluo.ebookreader.lib.theme.accentColor
import com.longluo.ebookreader.ui.book.audio.AudioPlayActivity
import com.longluo.ebookreader.ui.book.group.GroupEdit
import com.longluo.ebookreader.ui.book.info.BookInfoActivity
import com.longluo.ebookreader.ui.book.read.ReadBookActivity
import com.longluo.ebookreader.ui.book.search.SearchActivity
import com.longluo.ebookreader.ui.main.bookshelf.BaseBookshelfFragment
import com.longluo.ebookreader.utils.cnCompare
import com.longluo.ebookreader.utils.getPrefInt
import com.longluo.ebookreader.utils.observeEvent
import com.longluo.ebookreader.utils.startActivity
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.max

/**
 * 书架界面
 */
class BookshelfFragment2 : BaseBookshelfFragment(R.layout.fragment_bookshelf1),
    SearchView.OnQueryTextListener,
    BaseBooksAdapter.CallBack {

    private val binding by viewBinding(FragmentBookshelf1Binding::bind)
    private lateinit var booksAdapter: BaseBooksAdapter<*>
    override var groupId = AppConst.bookGroupNoneId
    private var booksFlowJob: Job? = null
    private var bookGroups: List<BookGroup> = emptyList()
    override var books: List<Book> = emptyList()

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        setSupportToolbar(binding.titleBar.toolbar)
        initRecyclerView()
        initGroupData()
        initBooksData()
    }

    override fun onCompatCreateOptionsMenu(menu: Menu) {
        menuInflater.inflate(R.menu.main_bookshelf, menu)
    }

    private fun initRecyclerView() {
        ATH.applyEdgeEffectColor(binding.rvBookshelf)
        binding.refreshLayout.setColorSchemeColors(accentColor)
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            activityViewModel.upToc(books)
        }
        val bookshelfLayout = getPrefInt(PreferKey.bookshelfLayout)
        if (bookshelfLayout == 0) {
            binding.rvBookshelf.layoutManager = LinearLayoutManager(context)
            booksAdapter = BooksAdapterList(requireContext(), this)
        } else {
            binding.rvBookshelf.layoutManager = GridLayoutManager(context, bookshelfLayout + 2)
            booksAdapter = BooksAdapterGrid(requireContext(), this)
        }
        binding.rvBookshelf.adapter = booksAdapter
        booksAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                val layoutManager = binding.rvBookshelf.layoutManager
                if (positionStart == 0 && layoutManager is LinearLayoutManager) {
                    val scrollTo = layoutManager.findFirstVisibleItemPosition() - itemCount
                    binding.rvBookshelf.scrollToPosition(max(0, scrollTo))
                }
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                val layoutManager = binding.rvBookshelf.layoutManager
                if (toPosition == 0 && layoutManager is LinearLayoutManager) {
                    val scrollTo = layoutManager.findFirstVisibleItemPosition() - itemCount
                    binding.rvBookshelf.scrollToPosition(max(0, scrollTo))
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initGroupData() {
        launch {
            appDb.bookGroupDao.flowShow().collect {
                if (it != bookGroups) {
                    bookGroups = it
                    booksAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initBooksData() {
        booksFlowJob?.cancel()
        booksFlowJob = launch {
            when (groupId) {
                AppConst.bookGroupAllId -> appDb.bookDao.flowAll()
                AppConst.bookGroupLocalId -> appDb.bookDao.flowLocal()
                AppConst.bookGroupAudioId -> appDb.bookDao.flowAudio()
                AppConst.bookGroupNoneId -> appDb.bookDao.flowNoGroup()
                else -> appDb.bookDao.flowByGroup(groupId)
            }.collect { list ->
                binding.tvEmptyMsg.isGone = list.isNotEmpty()
                books = when (getPrefInt(PreferKey.bookshelfSort)) {
                    1 -> list.sortedByDescending {
                        it.latestChapterTime
                    }
                    2 -> list.sortedWith { o1, o2 ->
                        o1.name.cnCompare(o2.name)
                    }
                    3 -> list.sortedBy {
                        it.order
                    }
                    else -> list.sortedByDescending {
                        it.durChapterTime
                    }
                }
                booksAdapter.notifyDataSetChanged()
            }
        }
    }

    fun back(): Boolean {
        if (groupId != AppConst.bookGroupNoneId) {
            groupId = AppConst.bookGroupNoneId
            initBooksData()
            return true
        }
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        SearchActivity.start(requireContext(), query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun gotoTop() {
        if (AppConfig.isEInkMode) {
            binding.rvBookshelf.scrollToPosition(0)
        } else {
            binding.rvBookshelf.smoothScrollToPosition(0)
        }
    }

    override fun onItemClick(position: Int) {
        when (val item = getItem(position)) {
            is Book -> when (item.type) {
                BookType.audio ->
                    startActivity<AudioPlayActivity> {
                        putExtra("bookUrl", item.bookUrl)
                    }
                else -> startActivity<ReadBookActivity> {
                    putExtra("bookUrl", item.bookUrl)
                }
            }
            is BookGroup -> {
                groupId = item.groupId
                initBooksData()
            }
        }
    }

    override fun onItemLongClick(position: Int) {
        when (val item = getItem(position)) {
            is Book -> startActivity<BookInfoActivity> {
                putExtra("name", item.name)
                putExtra("author", item.author)
            }
            is BookGroup -> GroupEdit.show(requireContext(), layoutInflater, item)
        }
    }

    override fun isUpdate(bookUrl: String): Boolean {
        return bookUrl in activityViewModel.updateList
    }

    override fun getItemCount(): Int {
        return if (groupId == AppConst.bookGroupNoneId) {
            bookGroups.size + books.size
        } else {
            books.size
        }
    }

    override fun getItemType(position: Int): Int {
        return if (groupId == AppConst.bookGroupNoneId) {
            if (position < bookGroups.size) 1 else 0
        } else {
            0
        }
    }

    override fun getItem(position: Int): Any {
        return if (groupId == AppConst.bookGroupNoneId) {
            if (position < bookGroups.size) {
                bookGroups[position]
            } else {
                books[position - bookGroups.size]
            }
        } else {
            books[position]
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun observeLiveBus() {
        super.observeLiveBus()
        observeEvent<String>(EventBus.UP_BOOKSHELF) {
            booksAdapter.notification(it)
        }
        observeEvent<String>(EventBus.BOOKSHELF_REFRESH) {
            booksAdapter.notifyDataSetChanged()
        }
    }
}