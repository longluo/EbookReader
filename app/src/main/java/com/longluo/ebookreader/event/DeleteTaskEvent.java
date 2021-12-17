package com.longluo.ebookreader.event;

import com.longluo.ebookreader.model.bean.CollBookBean;


public class DeleteTaskEvent {
    public CollBookBean collBook;

    public DeleteTaskEvent(CollBookBean collBook) {
        this.collBook = collBook;
    }
}
