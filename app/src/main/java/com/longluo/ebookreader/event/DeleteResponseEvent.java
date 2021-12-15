package com.longluo.ebookreader.event;

import com.longluo.ebookreader.model.bean.CallBookBean;


public class DeleteResponseEvent {
    public boolean isDelete;
    public CallBookBean collBook;

    public DeleteResponseEvent(boolean isDelete, CallBookBean collBook) {
        this.isDelete = isDelete;
        this.collBook = collBook;
    }
}
