package com.longluo.ebookreader.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.base.BaseFragment;
import com.longluo.ebookreader.db.BookContent;
import com.longluo.ebookreader.ui.adapter.BookContentAdapter;
import com.longluo.ebookreader.model.PageFactory;
import com.longluo.ebookreader.widget.itemdecoration.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;

public class BookContentsFragment extends BaseFragment {
    public static final String ARGUMENT_BOOK_PATH = "bookPath";

    @BindView(R.id.rv_book_content)
    RecyclerView rvBookContent;

    private PageFactory pageFactory;
    private ArrayList<BookContent> catalogueList = new ArrayList<>();
    private BookContentAdapter bookContentAdapter;

    public static BookContentsFragment newInstance(String bookPath) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_BOOK_PATH, bookPath);
        BookContentsFragment contentsFragment = new BookContentsFragment();
        contentsFragment.setArguments(bundle);
        return contentsFragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_book_contents;
    }

    @Override
    protected void initData(View view) {
        pageFactory = PageFactory.getInstance();
        catalogueList.addAll(pageFactory.getDirectoryList());

        bookContentAdapter = new BookContentAdapter(getContext(), catalogueList);
        rvBookContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBookContent.setAdapter(bookContentAdapter);
        rvBookContent.addItemDecoration(new DividerItemDecoration(getActivity()));

        bookContentAdapter.setCharter(pageFactory.getCurrentCharter());
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
