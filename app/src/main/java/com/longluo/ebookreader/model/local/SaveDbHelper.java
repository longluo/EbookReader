package com.longluo.ebookreader.model.local;

import com.longluo.ebookreader.model.bean.AuthorBean;
import com.longluo.ebookreader.model.bean.BookCommentBean;
import com.longluo.ebookreader.model.bean.BookHelpfulBean;
import com.longluo.ebookreader.model.bean.BookHelpsBean;
import com.longluo.ebookreader.model.bean.BookReviewBean;
import com.longluo.ebookreader.model.bean.DownloadTaskBean;
import com.longluo.ebookreader.model.bean.ReviewBookBean;
import com.longluo.ebookreader.model.bean.packages.BookSortPackage;

import java.util.List;


public interface SaveDbHelper {
    void saveBookHelps(List<BookHelpsBean> beans);

    void saveBookReviews(List<BookReviewBean> beans);

    void saveAuthors(List<AuthorBean> beans);

    void saveBooks(List<ReviewBookBean> beans);

    void saveBookHelpfuls(List<BookHelpfulBean> beans);

    void saveBookSortPackage(BookSortPackage bean);

    /*************DownloadTask*********************/
    void saveDownloadTask(DownloadTaskBean bean);
}
