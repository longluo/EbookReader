package com.longluo.ebookreader.event;

import com.longluo.ebookreader.model.bean.CallBookBean;


public class DeleteTaskEvent {
    public CallBookBean collBook;

    public DeleteTaskEvent(CallBookBean collBook) {
        this.collBook = collBook;
    }
}
