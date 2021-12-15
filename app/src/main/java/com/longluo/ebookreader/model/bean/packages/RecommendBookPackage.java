package com.longluo.ebookreader.model.bean.packages;

import com.longluo.ebookreader.model.bean.BaseBean;
import com.longluo.ebookreader.model.bean.CallBookBean;

import java.util.List;

/**

 */

public class RecommendBookPackage extends BaseBean {

    private List<CallBookBean> books;

    public List<CallBookBean> getBooks() {
        return books;
    }

    public void setBooks(List<CallBookBean> books) {
        this.books = books;
    }
}
