package com.longluo.zlibrary.core.application;

import com.longluo.zlibrary.core.view.ZLViewWidget;

public interface ZLApplicationWindow {
    void setWindowTitle(String title);

    void showErrorMessage(String resourceKey);

    void showErrorMessage(String resourceKey, String parameter);

    ZLApplication.SynchronousExecutor createExecutor(String key);

    void processException(Exception e);

    void refresh();

    ZLViewWidget getViewWidget();

    void close();

    int getBatteryLevel();
}
