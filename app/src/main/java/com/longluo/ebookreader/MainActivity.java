package com.longluo.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_showepub)
    Button btnEpub;

    @BindView(R.id.btn_open_folder)
    Button btnOpenFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_showepub)
    public void openEpubBook() {
        Intent intent = new Intent(MainActivity.this, EpubActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_open_folder)
    public void openFileList() {
        Intent intent = new Intent(MainActivity.this, FileListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
