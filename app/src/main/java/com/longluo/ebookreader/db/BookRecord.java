package com.longluo.ebookreader.db;

import org.litepal.crud.LitePalSupport;

public class BookRecord extends LitePalSupport {
    private int id;

    private String bookId;

    private String bookPath;

    //阅读到了第几章
    private int chapter;
    //当前的页码
    private int pagePos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getPagePos() {
        return pagePos;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }
}
