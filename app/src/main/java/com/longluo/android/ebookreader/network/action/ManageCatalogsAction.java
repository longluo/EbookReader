package com.longluo.android.ebookreader.network.action;

import android.app.Activity;
import android.content.Intent;

import com.longluo.android.ebookreader.network.CatalogManagerActivity;
import com.longluo.android.ebookreader.network.NetworkLibraryActivity;
import com.longluo.android.util.OrientationUtil;
import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.tree.ManageCatalogsItemTree;
import com.longluo.ebookreader.network.tree.RootTree;
import com.longluo.ebookreader.R;

import java.util.ArrayList;

public class ManageCatalogsAction extends RootAction {
    public ManageCatalogsAction(Activity activity) {
        super(activity, ActionCode.MANAGE_CATALOGS, "manageCatalogs", R.drawable.ic_menu_filter);
    }

    @Override
    public boolean isVisible(NetworkTree tree) {
        return tree instanceof RootTree || tree instanceof ManageCatalogsItemTree;
    }

    @Override
    public void run(NetworkTree tree) {
        final ArrayList<String> ids = new ArrayList<String>(myLibrary.activeIds());
        final ArrayList<String> inactiveIds = new ArrayList<String>(myLibrary.allIds());
        inactiveIds.removeAll(ids);

        OrientationUtil.startActivityForResult(
                myActivity,
                new Intent(myActivity.getApplicationContext(), CatalogManagerActivity.class)
                        .putStringArrayListExtra(NetworkLibraryActivity.ENABLED_CATALOG_IDS_KEY, ids)
                        .putStringArrayListExtra(NetworkLibraryActivity.DISABLED_CATALOG_IDS_KEY, inactiveIds),
                NetworkLibraryActivity.REQUEST_MANAGE_CATALOGS
        );
    }
}
