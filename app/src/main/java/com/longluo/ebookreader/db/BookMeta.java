package com.longluo.ebookreader.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;


public class BookMeta extends DataSupport implements Serializable{
    private int id;

    private String bookName;
    private String bookPath;

    private String format;

    // some books need convert to epub
    private boolean isCovert;
    private String convertFilePath;

    private long begin;
    private String charset;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return this.bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPath() {
        return this.bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isCovert() {
        return isCovert;
    }

    public void setCovert(boolean covert) {
        isCovert = covert;
    }

    public String getConvertFilePath() {
        return convertFilePath;
    }

    public void setConvertFilePath(String convertFilePath) {
        this.convertFilePath = convertFilePath;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

}
