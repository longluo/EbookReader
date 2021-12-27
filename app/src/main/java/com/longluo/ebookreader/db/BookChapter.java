package com.longluo.ebookreader.db;

import org.litepal.crud.DataSupport;

public class BookChapter extends DataSupport {
    private int id;

    private String bookId;

    private String bookPath;

    private String title;

    //在书籍文件中的起始位置
    private long start;

    //在书籍文件中的终止位置
    private long end;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
