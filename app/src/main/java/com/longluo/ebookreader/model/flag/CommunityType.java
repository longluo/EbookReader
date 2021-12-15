package com.longluo.ebookreader.model.flag;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.longluo.ebookreader.ERApplication;
import com.longluo.ebookreader.R;

public enum CommunityType {

    COMMENT(R.string.nb_fragment_community_comment, "ramble", R.drawable.ic_section_comment),
    REVIEW(R.string.nb_fragment_community_discussion, "", R.drawable.ic_section_discuss),
    HELP(R.string.nb_fragment_community_help, "", R.drawable.ic_section_help),
    GIRL(R.string.nb_fragment_community_girl, "girl", R.drawable.ic_section_girl),
    COMPOSE(R.string.nb_fragment_community_compose, "original", R.drawable.ic_section_compose);

    private final String typeName;
    private final String netName;
    private final int iconId;

    CommunityType(@StringRes int typeId, String netName, @DrawableRes int iconId) {
        this.typeName = ERApplication.getContext().getResources().getString(typeId);
        this.netName = netName;
        this.iconId = iconId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getNetName() {
        return netName;
    }

    public int getIconId() {
        return iconId;
    }
}
