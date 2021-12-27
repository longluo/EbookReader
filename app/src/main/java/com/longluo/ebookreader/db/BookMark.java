package com.longluo.ebookreader.db;

import org.litepal.crud.DataSupport;

public class BookMark extends DataSupport {
    private int id;

    private long begin; // 书签记录页面的结束点位置

    private String text;

    private String time;
    
    private String bookPath;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getBegin() {
        return this.begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

}
