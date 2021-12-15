package com.longluo.ebookreader.event;

import com.longluo.ebookreader.model.flag.BookDistillate;
import com.longluo.ebookreader.model.flag.BookSort;
import com.longluo.ebookreader.model.flag.BookType;


public class SelectorEvent {

    public BookDistillate distillate;

    public BookType type;

    public BookSort sort;

    public SelectorEvent(BookDistillate distillate,
                         BookType type,
                         BookSort sort) {
        this.distillate = distillate;
        this.type = type;
        this.sort = sort;
    }
}
