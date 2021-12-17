package com.longluo.ebookreader.event;

import com.longluo.ebookreader.model.bean.CollBookBean;


public class DeleteResponseEvent {
    public boolean isDelete;
    public CollBookBean collBook;

    public DeleteResponseEvent(boolean isDelete, CollBookBean collBook) {
        this.isDelete = isDelete;
        this.collBook = collBook;
    }
}
