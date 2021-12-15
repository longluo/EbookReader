package com.longluo.ebookreader.model.flag;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.longluo.ebookreader.ERApplication;
import com.longluo.ebookreader.R;


public enum FindType {
    TOP(R.string.nb_fragment_find_top, R.drawable.ic_section_top),
    TOPIC(R.string.nb_fragment_find_topic, R.drawable.ic_section_topic),
    SORT(R.string.nb_fragment_find_sort, R.drawable.ic_section_sort),
    LISTEN(R.string.nb_fragment_find_listen, R.drawable.ic_section_listen);
    private final String typeName;
    private final int iconId;

    FindType(@StringRes int typeNameId, @DrawableRes int iconId) {
        this.typeName = ERApplication.getContext().getResources().getString(typeNameId);
        this.iconId = iconId;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getIconId() {
        return iconId;
    }
}
