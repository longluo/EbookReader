package com.longluo.ebookreader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.longluo.ebookreader.R;

/**
 * Created by newbiechen on 17-4-29.
 * 1. 找到改写TextView内容的方法:是哪个 0 0，个人猜测是setText()
 * 2. 找到文章中存在《》的位置:
 * 3. 设置ForeSpan
 * 4. 添加点击事件
 */
public class BookTextView extends AppCompatTextView {

    private OnBookClickListener mBookListener;

    private int bookColor;

    public BookTextView(Context context) {
        this(context, null);
    }

    public BookTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BookTextView);
        bookColor = typedArray.getColor(R.styleable.BookTextView_bookColor, ContextCompat.getColor(getContext(), R.color.light_coffee));
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String value = text.toString();
        int indexStart = 0;
        int indexEnd = 0;

        indexStart = value.indexOf("《", indexStart);
        indexEnd = value.indexOf("》", indexEnd);
        SpannableStringBuilder builder = new SpannableStringBuilder(value);
        while (indexStart != -1 || indexEnd != -1) {
            final int start = indexStart + 1;
            final int end = indexEnd;
            builder.setSpan(new ForegroundColorSpan(bookColor), indexStart, indexEnd + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {

                    if (mBookListener != null) {
                        String bookName = value.substring(start, end);
                        mBookListener.onBookClick(bookName);
                    }
                }
            }, indexStart, indexEnd + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            indexStart = value.indexOf("《", indexStart + 1);
            indexEnd = value.indexOf("》", indexEnd + 1);
        }
        this.setMovementMethod(LinkMovementMethod.getInstance());
        super.setText(builder, type);
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        mBookListener = listener;
    }

    public interface OnBookClickListener {
        void onBookClick(String bookName);
    }
}
