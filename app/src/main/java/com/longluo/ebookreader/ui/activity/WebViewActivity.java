package com.longluo.ebookreader.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.longluo.ebookreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {
    public static final String EXTRA_TABLE_HTML = "EXTRA_TABLE_HTML";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        toolbar.setTitle("Web View");
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
    }

    private void initData() {
        getWindow().setBackgroundDrawable(null);
        setSupportActionBar(toolbar);

        String tableHtml = getIntent().getStringExtra(EXTRA_TABLE_HTML);
        webView.loadData(tableHtml, "text/html", "UTF-8");
    }
}