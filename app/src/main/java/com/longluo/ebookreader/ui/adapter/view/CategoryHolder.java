package com.longluo.ebookreader.ui.adapter.view;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.model.bean.BookChapterBean;
import com.longluo.ebookreader.utils.BookManager;
import com.longluo.ebookreader.ui.base.adapter.ViewHolderImpl;
import com.longluo.ebookreader.widget.page.TxtChapter;



public class CategoryHolder extends ViewHolderImpl<TxtChapter> {

    private TextView mTvChapter;

    @Override
    public void initView() {
        mTvChapter = findById(R.id.category_tv_chapter);
    }

    @Override
    public void onBind(TxtChapter value, int pos){
        //首先判断是否该章已下载
        Drawable drawable = null;

        //TODO:目录显示设计的有点不好，需要靠成员变量是否为null来判断。
        //如果没有链接地址表示是本地文件
        if (value.getLink() == null){
            drawable = ContextCompat.getDrawable(getContext(),R.drawable.selector_category_load);
        }
        else {
            if (value.getBookId() != null
                    && BookManager
                    .isChapterCached(value.getBookId(),value.getTitle())){
                drawable = ContextCompat.getDrawable(getContext(),R.drawable.selector_category_load);
            }
            else {
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.selector_category_unload);
            }
        }

        mTvChapter.setSelected(false);
        mTvChapter.setTextColor(ContextCompat.getColor(getContext(),R.color.nb_text_default));
        mTvChapter.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
        mTvChapter.setText(value.getTitle());
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_category;
    }

    public void setSelectedChapter(){
        mTvChapter.setTextColor(ContextCompat.getColor(getContext(),R.color.light_red));
        mTvChapter.setSelected(true);
    }
}
