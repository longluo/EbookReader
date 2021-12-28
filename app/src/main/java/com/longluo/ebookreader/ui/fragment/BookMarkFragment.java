package com.longluo.ebookreader.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.base.BaseFragment;
import com.longluo.ebookreader.db.BookMark;
import com.longluo.ebookreader.ui.adapter.BookMarkAdapter;
import com.longluo.ebookreader.model.PageFactory;
import com.longluo.ebookreader.widget.itemdecoration.DividerItemDecoration;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BookMarkFragment extends BaseFragment {
    private static final String BOOK_PATH = "book_path";

    @BindView(R.id.rv_book_mark)
    RecyclerView rvBookMark;

    private String bookPath;
    private List<BookMark> bookMarkList;
    private BookMarkAdapter bookMarkAdapter;
    private PageFactory pageFactory;

    public static BookMarkFragment newInstance(String bookPath) {
        Bundle bundle = new Bundle();
        bundle.putString(BOOK_PATH, bookPath);
        BookMarkFragment bookMarkFragment = new BookMarkFragment();
        bookMarkFragment.setArguments(bundle);
        return bookMarkFragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_bookmark;
    }

    @Override
    protected void initData(View view) {
        pageFactory = PageFactory.getInstance();
        Bundle bundle = getArguments();
        if (bundle != null) {
            bookPath = bundle.getString(BOOK_PATH);
        }

        bookMarkList = new ArrayList<>();
        bookMarkList = LitePal.where("bookPath = ?", bookPath).order("time").find(BookMark.class);

        bookMarkAdapter = new BookMarkAdapter(getActivity(), bookMarkList);
        rvBookMark.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBookMark.setAdapter(bookMarkAdapter);
        rvBookMark.addItemDecoration(new DividerItemDecoration(getActivity()));

        bookMarkAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {

        bookMarkAdapter.setOnItemClickListener(new BookMarkAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                pageFactory.changeChapter(bookMarkList.get(position).getBegin());
                getActivity().finish();
            }
        });

        bookMarkAdapter.setOnItemLongClickListener(new BookMarkAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("是否删除书签？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LitePal.delete(BookMark.class, bookMarkList.get(position).getId());
                                bookMarkList.clear();
                                bookMarkList.addAll(LitePal.where("bookPath = ?", bookPath).find(BookMark.class));
                                bookMarkAdapter.notifyDataSetChanged();
                            }
                        }).setCancelable(true).show();
            }
        });
    }
}
