package com.longluo.zlibrary.text.view;

public abstract class ZLTextElement {
	public final static ZLTextElement HSpace = new ZLTextElement() {};
	public final static ZLTextElement NBSpace = new ZLTextElement() {};
	public final static ZLTextElement AfterParagraph = new ZLTextElement() {};
	public final static ZLTextElement Indent = new ZLTextElement() {};
	public final static ZLTextElement StyleClose = new ZLTextElement() {};
}
