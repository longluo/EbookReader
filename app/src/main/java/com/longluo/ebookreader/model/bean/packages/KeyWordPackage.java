package com.longluo.ebookreader.model.bean.packages;

import com.longluo.ebookreader.model.bean.BaseBean;

import java.util.List;



public class KeyWordPackage extends BaseBean {

    private List<String> keywords;

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
