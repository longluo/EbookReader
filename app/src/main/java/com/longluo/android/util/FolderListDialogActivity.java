package com.longluo.android.util;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.ebookreader.R;

public class FolderListDialogActivity extends ListActivity {
	interface Key {
		String FOLDER_LIST            = "folder_list.folder_list";
		String ACTIVITY_TITLE         = "folder_list.title";
		String CHOOSER_TITLE          = "folder_list.chooser_title";
		String WRITABLE_FOLDERS_ONLY  = "folder_list.writable_folders_only";
	}

	private ArrayList<String> myFolderList;
	private String myChooserTitle;
	private boolean myChooseWritableDirectoriesOnly;
	private ZLResource myResource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.folder_list_dialog);

		final Intent intent = getIntent();
		myFolderList = intent.getStringArrayListExtra(Key.FOLDER_LIST);
		setTitle(intent.getStringExtra(Key.ACTIVITY_TITLE));
		myChooserTitle = intent.getStringExtra(Key.CHOOSER_TITLE);
		myChooseWritableDirectoriesOnly = intent.getBooleanExtra(Key.WRITABLE_FOLDERS_ONLY, true);
		myResource = ZLResource.resource("dialog").getResource("folderList");

		final ZLResource buttonResource = ZLResource.resource("dialog").getResource("button");
		final Button okButton = (Button)findViewById(R.id.folder_list_dialog_button_ok);
		okButton.setText(buttonResource.getResource("ok").getValue());
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK, new Intent().putExtra(Key.FOLDER_LIST, myFolderList));
				finish();
			}
		});
		final Button cancelButton = (Button)findViewById(R.id.folder_list_dialog_button_cancel);
		cancelButton.setText(buttonResource.getResource("cancel").getValue());
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		final DirectoriesAdapter adapter = new DirectoriesAdapter();
		setListAdapter(adapter);
		getListView().setOnItemClickListener(adapter);

		setResult(RESULT_CANCELED);
	}

	@Override
	protected void onActivityResult(int index, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			final String path = FileChooserUtil.folderPathFromData(data);
			final int existing = myFolderList.indexOf(path);
			if (existing == -1) {
				if (index == 0) {
					myFolderList.add(path);
				} else {
					myFolderList.set(index - 1, path);
				}
				((DirectoriesAdapter)getListAdapter()).notifyDataSetChanged();
			} else if (existing != index - 1) {
				UIMessageUtil.showMessageText(
					this, myResource.getResource("duplicate").getValue().replace("%s", path)
				);
			}
		}
	}

	private void showItemRemoveDialog(final int index) {
		final ZLResource resource = myResource.getResource("removeDialog");
		final ZLResource buttonResource = ZLResource.resource("dialog").getResource("button");
		new AlertDialog.Builder(FolderListDialogActivity.this)
			.setCancelable(false)
			.setTitle(resource.getValue())
			.setMessage(resource.getResource("message").getValue().replace("%s", myFolderList.get(index)))
			.setPositiveButton(buttonResource.getResource("yes").getValue(), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					myFolderList.remove(index);
					((DirectoriesAdapter)getListAdapter()).notifyDataSetChanged();
				}
			})
			.setNegativeButton(buttonResource.getResource("cancel").getValue(), null)
			.create().show();
	}

	private class DirectoriesAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
		@Override
		public int getCount() {
			return myFolderList.size() + 1;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public String getItem(int position) {
			return position != 0
				? myFolderList.get(position - 1)
				: myResource.getResource("addFolder").getValue();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final View view = convertView != null
				? convertView
				: LayoutInflater.from(FolderListDialogActivity.this).inflate(R.layout.folder_list_item, parent, false);

			((TextView)view.findViewById(R.id.folder_list_item_title)).setText(getItem(position));

			final View deleteButton = view.findViewById(R.id.folder_list_item_remove);

			if (position > 0 && myFolderList.size() > 1) {
				deleteButton.setVisibility(View.VISIBLE);
				deleteButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(final View v) {
						showItemRemoveDialog(position - 1);
					}
				});
			} else {
				deleteButton.setVisibility(View.INVISIBLE);
			}

			return view;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
			FileChooserUtil.runDirectoryChooser(
				FolderListDialogActivity.this,
				position,
				myChooserTitle,
				position == 0 ? "/" : myFolderList.get(position - 1),
				myChooseWritableDirectoriesOnly
			);
		}
	}
}
