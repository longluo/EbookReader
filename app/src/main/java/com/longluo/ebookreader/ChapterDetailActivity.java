package com.longluo.ebookreader;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

public class ChapterDetailActivity extends AppCompatActivity {
    private static final String TAG = "ChapterDetailActivity";
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail);
        ButterKnife.bind(this);

        String href = getIntent().getStringExtra("href");
        Log.i(TAG, "onCreate: href=" + href);
        try {
            EpubReader reader = new EpubReader();
            InputStream in = getAssets().open("176116.epub");
            Book book = reader.readEpub(in);
            Resource byHref = book.getResources().getByHref(href);

            byte[] data = byHref.getData();   //和 resource.getInputStream() 返回的都是html格式的文章内容，只不过读取方式不一样
            String strHtml = StringUtils.bytes2Hex(data);
            Log.i(TAG, "initView: strHtml= " + strHtml);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadDataWithBaseURL(null, strHtml, "text/html", "utf-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

