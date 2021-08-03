package com.longluo.epublib.epub;

import com.longluo.epublib.domain.Resource;

import java.io.OutputStream;

@SuppressWarnings("unused")
public interface HtmlProcessor {
    void processHtmlResource(Resource resource, OutputStream out);
}
