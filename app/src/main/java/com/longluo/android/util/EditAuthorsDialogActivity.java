package com.longluo.android.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.longluo.ebookreader.R;
import com.longluo.zlibrary.core.resources.ZLResource;

import java.util.ArrayList;

public class EditAuthorsDialogActivity extends EditListDialogActivity {
    public static final int REQ_CODE = 002;

    private final String AUTHOR_NAME_FILTER = "[\\p{L}0-9_\\-& ]*";
    private AutoCompleteTextView myInputField;
    private int myEditPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_authors_dialog);

        myResource = ZLResource.resource("dialog").getResource("editAuthors");

        final Intent intent = getIntent();
        ArrayList<String> allAuthorList = intent.getStringArrayListExtra(EditListDialogActivity.Key.ALL_ITEMS_LIST);

        myInputField = (AutoCompleteTextView) findViewById(R.id.edit_authors_input_field);
        myInputField.setHint(myResource.getResource("addAuthor").getValue());
        myInputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addAuthor(myInputField.getText().toString().trim(), myEditPosition);
                    myInputField.setText("");
                    myEditPosition = -1;
                    return false;
                }
                return true;
            }
        });
        myInputField.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allAuthorList));

        parseUIElements();

        final AuthorsAdapter adapter = new AuthorsAdapter();
        setListAdapter(adapter);
        getListView().setOnItemClickListener(adapter);
        getListView().setOnItemLongClickListener(adapter);

        setResult(RESULT_CANCELED);
    }

    private void addAuthor(String author, int position) {
        if (author.length() != 0 && author.matches(AUTHOR_NAME_FILTER)) {
            if (position < 0) {
                if (!myEditList.contains(author)) {
                    myEditList.add(author);
                }
            } else {
                myEditList.set(position, author);
            }
            ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    protected void onChooseContextMenu(int index, int itemPosition) {
        switch (index) {
            case 0:
                editAuthor(itemPosition);
                break;
            case 1:
                deleteItem(itemPosition);
                break;
        }
    }

    private void editAuthor(int position) {
        myEditPosition = position;
        String s = (String) getListAdapter().getItem(position);
        myInputField.setText(s);
        myInputField.setSelection(myInputField.getText().length());
        myInputField.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(myInputField, InputMethodManager.SHOW_IMPLICIT);
    }

    private class AuthorsAdapter extends EditListAdapter {
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = super.getView(position, convertView, parent);

            final View deleteButton = view.findViewById(R.id.edit_item_remove);

            if (myEditList.size() > 1) {
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(final View v) {
                        deleteItem(position);
                    }
                });
            } else {
                deleteButton.setVisibility(View.INVISIBLE);
            }

            return view;
        }
    }
}
