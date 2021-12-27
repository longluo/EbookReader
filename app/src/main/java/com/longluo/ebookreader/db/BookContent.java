package com.longluo.ebookreader.db;

import org.litepal.crud.LitePalSupport;

public class BookContent extends LitePalSupport {
    private int id;

    private String bookPath;

    private String bookContent;

    private long bookContentStartPos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookContent() {
        return this.bookContent;
    }

    public void setBookContent(String bookContent) {
        this.bookContent = bookContent;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String ebookpath) {
        this.bookPath = ebookpath;
    }

    public long getBookContentStartPos() {
        return bookContentStartPos;
    }

    public void setBookContentStartPos(long bookContentStartPos) {
        this.bookContentStartPos = bookContentStartPos;
    }
}

