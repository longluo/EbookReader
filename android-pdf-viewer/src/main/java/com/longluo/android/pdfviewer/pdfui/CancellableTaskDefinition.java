package com.longluo.android.pdfviewer.pdfui;

public interface CancellableTaskDefinition<Params, Result> {
    Result doInBackground(Params... params);

    void doCancel();

    void doCleanup();
}
