package com.longluo.ebookreader.formats;

import com.longluo.ebookreader.book.AbstractBook;
import com.longluo.ebookreader.book.BookUtil;
import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.util.SystemInfo;
import org.pdfparse.model.PDFDocInfo;
import org.pdfparse.model.PDFDocument;

public class PDFPlugin extends ExternalFormatPlugin {
    public PDFPlugin(SystemInfo systemInfo) {
        super(systemInfo, "PDF");
    }

    @Override
    public String packageName() {
        return "com.longluo.ebookreader.plugin.pdf";
    }

    @Override
    public void readMetainfo(AbstractBook book) {
        final ZLFile file = BookUtil.fileByBook(book);
        if (file != file.getPhysicalFile()) {
            // TODO: throw BookReadingException
            System.err.println("Only physical PDF files are supported");
            return;
        }
        try {
            final PDFDocument doc = new PDFDocument(book.getPath());
            // TODO: solution for rc4 encryption
            if (!doc.isEncrypted()) {
                final PDFDocInfo info = doc.getDocumentInfo();
                book.setTitle(info.getTitle());
                book.addAuthor(info.getAuthor());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readUids(AbstractBook book) {
        if (book.uids().isEmpty()) {
            book.addUid(BookUtil.createUid(book, "SHA-256"));
        }
    }
}
