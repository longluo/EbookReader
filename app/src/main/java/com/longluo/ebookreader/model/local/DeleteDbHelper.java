package com.longluo.ebookreader.model.local;

import com.longluo.ebookreader.model.bean.AuthorBean;
import com.longluo.ebookreader.model.bean.BookCommentBean;
import com.longluo.ebookreader.model.bean.BookHelpfulBean;
import com.longluo.ebookreader.model.bean.BookHelpsBean;
import com.longluo.ebookreader.model.bean.BookReviewBean;
import com.longluo.ebookreader.model.bean.ReviewBookBean;

import java.util.List;


public interface DeleteDbHelper {
    void deleteBookComments(List<BookCommentBean> beans);

    void deleteBookReviews(List<BookReviewBean> beans);

    void deleteBookHelps(List<BookHelpsBean> beans);

    void deleteAuthors(List<AuthorBean> beans);

    void deleteBooks(List<ReviewBookBean> beans);

    void deleteBookHelpful(List<BookHelpfulBean> beans);

    void deleteAll();
}
