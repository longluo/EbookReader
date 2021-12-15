package com.longluo.ebookreader.model.bean.packages;

import com.longluo.ebookreader.model.bean.BaseBean;
import com.longluo.ebookreader.model.bean.BookReviewBean;

import java.util.List;



public class BookReviewPackage extends BaseBean {

    private List<BookReviewBean> reviews;

    public List<BookReviewBean> getReviews() {
        return reviews;
    }

    public void setReviews(List<BookReviewBean> reviews) {
        this.reviews = reviews;
    }
}
