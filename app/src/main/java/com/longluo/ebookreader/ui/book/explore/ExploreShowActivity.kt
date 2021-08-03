package com.longluo.ebookreader.ui.book.explore

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.VMBaseActivity
import com.longluo.ebookreader.data.entities.Book
import com.longluo.ebookreader.data.entities.SearchBook
import com.longluo.ebookreader.databinding.ActivityExploreShowBinding
import com.longluo.ebookreader.databinding.ViewLoadMoreBinding
import com.longluo.ebookreader.ui.book.info.BookInfoActivity
import com.longluo.ebookreader.ui.widget.recycler.LoadMoreView
import com.longluo.ebookreader.ui.widget.recycler.VerticalDivider
import com.longluo.ebookreader.utils.startActivity
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding

class ExploreShowActivity : VMBaseActivity<ActivityExploreShowBinding, ExploreShowViewModel>(),
    ExploreShowAdapter.CallBack {
    override val binding by viewBinding(ActivityExploreShowBinding::inflate)
    override val viewModel by viewModels<ExploreShowViewModel>()

    private lateinit var adapter: ExploreShowAdapter
    private lateinit var loadMoreView: LoadMoreView
    private var isLoading = true

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.titleBar.title = intent.getStringExtra("exploreName")
        initRecyclerView()
        viewModel.booksData.observe(this) { upData(it) }
        viewModel.initData(intent)
        viewModel.errorLiveData.observe(this) {
            loadMoreView.error(it)
        }
    }

    private fun initRecyclerView() {
        adapter = ExploreShowAdapter(this, this)
        binding.recyclerView.addItemDecoration(VerticalDivider(this))
        binding.recyclerView.adapter = adapter
        loadMoreView = LoadMoreView(this)
        adapter.addFooterView {
            ViewLoadMoreBinding.bind(loadMoreView)
        }
        loadMoreView.startLoad()
        loadMoreView.setOnClickListener {
            if (!isLoading) {
                loadMoreView.hasMore()
                scrollToBottom()
                isLoading = true
            }
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    scrollToBottom()
                }
            }
        })
    }

    private fun scrollToBottom() {
        adapter.let {
            if (loadMoreView.hasMore && !isLoading) {
                viewModel.explore()
            }
        }
    }

    private fun upData(books: List<SearchBook>) {
        isLoading = false
        if (books.isEmpty() && adapter.isEmpty()) {
            loadMoreView.noMore(getString(R.string.empty))
        } else if (books.isEmpty()) {
            loadMoreView.noMore()
        } else if (adapter.getItems().contains(books.first()) && adapter.getItems()
                .contains(books.last())
        ) {
            loadMoreView.noMore()
        } else {
            adapter.addItems(books)
        }
    }

    override fun showBookInfo(book: Book) {
        startActivity<BookInfoActivity> {
            putExtra("name", book.name)
            putExtra("author", book.author)
        }
    }
}
