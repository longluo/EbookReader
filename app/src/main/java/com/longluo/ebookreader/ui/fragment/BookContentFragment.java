package com.longluo.ebookreader.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.base.BaseFragment;
import com.longluo.ebookreader.db.BookContent;
import com.longluo.ebookreader.ui.adapter.BookContentAdapter;
import com.longluo.ebookreader.ui.adapter.BookMarkAdapter;
import com.longluo.ebookreader.util.PageFactory;

import java.util.ArrayList;

import butterknife.BindView;

public class BookContentFragment extends BaseFragment {
    public static final String ARGUMENT = "argument";

    private PageFactory pageFactory;
    ArrayList<BookContent> catalogueList = new ArrayList<>();

    @BindView(R.id.rv_book_content)
    RecyclerView rvBookContent;

    BookContentAdapter bookContentAdapter;

    public static BookContentFragment newInstance(String bookpath) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, bookpath);
        BookContentFragment bookContentFragment = new BookContentFragment();
        bookContentFragment.setArguments(bundle);
        return bookContentFragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_book_content;
    }

    @Override
    protected void initData(View view) {
        pageFactory = PageFactory.getInstance();
        catalogueList.addAll(pageFactory.getDirectoryList());
        bookContentAdapter = new BookContentAdapter(getContext(), catalogueList);
        bookContentAdapter.setCharter(pageFactory.getCurrentCharter());

        rvBookContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBookContent.setAdapter(bookContentAdapter);
        rvBookContent.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL));

        bookContentAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        bookContentAdapter.setOnItemClickListener(new BookContentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                pageFactory.changeChapter(catalogueList.get(position).getBookContentStartPos());
                getActivity().finish();
            }
        });
    }
}
