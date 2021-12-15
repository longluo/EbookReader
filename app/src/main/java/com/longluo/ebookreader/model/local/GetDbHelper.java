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

import io.reactivex.Single;


public interface GetDbHelper {
    Single<List<BookCommentBean>> getBookComments(String block, String sort, int start, int limited, String distillate);

    Single<List<BookHelpsBean>> getBookHelps(String sort, int start, int limited, String distillate);

    Single<List<BookReviewBean>> getBookReviews(String sort, String bookType, int start, int limited, String distillate);

    BookSortPackage getBookSortPackage();

    AuthorBean getAuthor(String id);

    ReviewBookBean getReviewBook(String id);

    BookHelpfulBean getBookHelpful(String id);

    /******************************/
    List<DownloadTaskBean> getDownloadTaskList();
}
