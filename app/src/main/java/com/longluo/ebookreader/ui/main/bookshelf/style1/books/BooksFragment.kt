package com.longluo.ebookreader.ui.main.bookshelf.style1.books

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseFragment
import com.longluo.ebookreader.constant.AppConst
import com.longluo.ebookreader.constant.BookType
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.constant.PreferKey
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.Book
import com.longluo.ebookreader.databinding.FragmentBooksBinding
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.lib.theme.ATH
import com.longluo.ebookreader.lib.theme.accentColor
import com.longluo.ebookreader.ui.book.audio.AudioPlayActivity
import com.longluo.ebookreader.ui.book.info.BookInfoActivity
import com.longluo.ebookreader.ui.book.read.ReadBookActivity
import com.longluo.ebookreader.ui.main.MainViewModel
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
class BooksFragment : BaseFragment(R.layout.fragment_books),
    BaseBooksAdapter.CallBack {

    companion object {
        fun newInstance(position: Int, groupId: Long): BooksFragment {
            return BooksFragment().apply {
                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putLong("groupId", groupId)
                arguments = bundle
            }
        }
    }

    private val binding by viewBinding(FragmentBooksBinding::bind)
    private val activityViewModel: MainViewModel
            by activityViewModels()
    private lateinit var booksAdapter: BaseBooksAdapter<*>
    private var booksFlowJob: Job? = null
    private var position = 0
    private var groupId = -1L

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            position = it.getInt("position", 0)
            groupId = it.getLong("groupId", -1)
        }
        initRecyclerView()
        upRecyclerData()
    }

    private fun initRecyclerView() {
        ATH.applyEdgeEffectColor(binding.rvBookshelf)
        binding.refreshLayout.setColorSchemeColors(accentColor)
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            activityViewModel.upToc(booksAdapter.getItems())
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

    private fun upRecyclerData() {
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
                val books = when (getPrefInt(PreferKey.bookshelfSort)) {
                    1 -> list.sortedByDescending { it.latestChapterTime }
                    2 -> list.sortedWith { o1, o2 ->
                        o1.name.cnCompare(o2.name)
                    }
                    3 -> list.sortedBy { it.order }
                    else -> list.sortedByDescending { it.durChapterTime }
                }
                booksAdapter.setItems(books)
            }
        }
    }

    fun getBooks(): List<Book> {
        return booksAdapter.getItems()
    }

    fun gotoTop() {
        if (AppConfig.isEInkMode) {
            binding.rvBookshelf.scrollToPosition(0)
        } else {
            binding.rvBookshelf.smoothScrollToPosition(0)
        }
    }

    fun getBooksCount(): Int {
        return booksAdapter.itemCount
    }

    override fun open(book: Book) {
        when (book.type) {
            BookType.audio ->
                startActivity<AudioPlayActivity> {
                    putExtra("bookUrl", book.bookUrl)
                }
            else -> startActivity<ReadBookActivity> {
                putExtra("bookUrl", book.bookUrl)
            }
        }
    }

    override fun openBookInfo(book: Book) {
        startActivity<BookInfoActivity> {
            putExtra("name", book.name)
            putExtra("author", book.author)
        }
    }

    override fun isUpdate(bookUrl: String): Boolean {
        return bookUrl in activityViewModel.updateList
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