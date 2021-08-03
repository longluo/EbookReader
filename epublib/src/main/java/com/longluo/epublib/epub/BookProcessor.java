package com.longluo.epublib.epub;

import com.longluo.epublib.domain.EpubBook;

/**
 * Post-processes a book.
 * <p>
 * Can be used to clean up a book after reading or before writing.
 *
 * @author paul
 */
public interface BookProcessor {

    /**
     * A BookProcessor that returns the input book unchanged.
     */
    BookProcessor IDENTITY_BOOKPROCESSOR = book -> book;

    EpubBook processBook(EpubBook book);
}
