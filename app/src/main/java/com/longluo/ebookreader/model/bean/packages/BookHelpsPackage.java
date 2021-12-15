package com.longluo.ebookreader.model.bean.packages;

import com.longluo.ebookreader.model.bean.BaseBean;
import com.longluo.ebookreader.model.bean.BookHelpsBean;

import java.util.List;


public class BookHelpsPackage extends BaseBean {

    private List<BookHelpsBean> helps;

    public List<BookHelpsBean> getHelps() {
        return helps;
    }

    public void setHelps(List<BookHelpsBean> helps) {
        this.helps = helps;
    }
}
