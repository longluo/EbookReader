package com.longluo.ebookreader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.longluo.ebookreader.R;

import org.sufficientlysecure.htmltextview.ClickableTableSpan;
import org.sufficientlysecure.htmltextview.DrawTableLinkSpan;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.OnClickATagListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.longluo.ebookreader.ui.activity.WebViewActivity.EXTRA_TABLE_HTML;

public class HtmlViewActivity extends AppCompatActivity {
    private static final String LOG_TAG = HtmlViewActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.html_text)
    HtmlTextView tvHtml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_view);
        ButterKnife.bind(this);

        toolbar.setTitle("Html View");
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
        initListener();
    }

    private void initData() {
        //text.setRemoveFromHtmlSpace(false); // default is true
        tvHtml.setClickableTableSpan(new ClickableTableSpanImpl());
        DrawTableLinkSpan drawTableLinkSpan = new DrawTableLinkSpan();
        drawTableLinkSpan.setTableLinkText("[tap for table]");
        tvHtml.setDrawTableLinkSpan(drawTableLinkSpan);

        // Best to use indentation that matches screen density.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        tvHtml.setListIndentPx(metrics.density * 10);

        tvHtml.blockQuoteBackgroundColor = getResources().getColor(R.color.whitish);
        tvHtml.blockQuoteStripColor = getResources().getColor(R.color.blue);

        tvHtml.setHtml(R.raw.example, new HtmlResImageGetter(getBaseContext()));
    }

    private void initListener() {
        // a tag click listener
        tvHtml.setOnClickATagListener(new OnClickATagListener() {
            @Override
            public boolean onClick(View widget, String spannedText, @Nullable String href) {
                final Toast toast = Toast.makeText(HtmlViewActivity.this, null, Toast.LENGTH_SHORT);
                toast.setText(href);
                toast.show();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // The html table(s) are individually passed through to the ClickableTableSpan implementation
    // presumably for a WebView activity.
    class ClickableTableSpanImpl extends ClickableTableSpan {
        @Override
        public ClickableTableSpan newInstance() {
            return new ClickableTableSpanImpl();
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(HtmlViewActivity.this, WebViewActivity.class);
            intent.putExtra(EXTRA_TABLE_HTML, getTableHtml());
            startActivity(intent);
        }
    }
}