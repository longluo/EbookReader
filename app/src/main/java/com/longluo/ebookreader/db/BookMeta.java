package com.longluo.ebookreader.db;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

public class BookMeta extends LitePalSupport implements Serializable {
    private int id;

    private String bookName;
    private String bookPath;

    private String format;

    // some books need convert to epub
    private boolean isCovert;
    private String convertFilePath;

    private long begin;
    private String charset;

    private String lastRead;

    private String lastChapter;

    private List<BookChapter> bookChapters;

    private String updated;

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

    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public void setBookChapters(List<BookChapter> chapters) {
        bookChapters = chapters;
        for (BookChapter chapter : bookChapters) {
            chapter.setBookId(String.valueOf(getId()));
        }
    }

    public List<BookChapter> getBookChapters() {
        return getBookChapterList();
    }

    public List<BookChapter> getBookChapterList() {
        if (bookChapters == null) {
            List<BookChapter> chapterList = LitePal.where("bookPath = ?", getBookPath()).find(BookChapter.class);
            synchronized (this) {
                if (bookChapters == null) {
                    bookChapters = chapterList;
                }
            }
        }

        return bookChapters;
    }

    public synchronized void resetBookChapterList() {
        bookChapters = null;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
