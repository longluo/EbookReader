package com.longluo.ebookreader.model.bean.packages;

import com.longluo.ebookreader.model.bean.BaseBean;
import com.longluo.ebookreader.model.bean.BookSortBean;

import java.util.List;



public class BookSortPackage extends BaseBean {

    private List<BookSortBean> male;
    private List<BookSortBean> female;

    public List<BookSortBean> getMale() {
        return male;
    }

    public void setMale(List<BookSortBean> male) {
        this.male = male;
    }

    public List<BookSortBean> getFemale() {
        return female;
    }

    public void setFemale(List<BookSortBean> female) {
        this.female = female;
    }
}
