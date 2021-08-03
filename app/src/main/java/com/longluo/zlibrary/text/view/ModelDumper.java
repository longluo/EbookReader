package com.longluo.zlibrary.text.view;

import com.longluo.zlibrary.text.model.ZLTextModel;
import com.longluo.zlibrary.text.model.ZLTextParagraph;

abstract class ModelDumper {
	static void dump(ZLTextModel model) {
		System.err.println("+++ MODEL DUMP +++");
		if (model == null) {
			System.err.println("MODEL IS NULL");
		} else {
			System.err.println("PARAGRAPHS: " + model.getParagraphsNumber());
			for (int i = 0; i < model.getParagraphsNumber(); ++i) {
				final ZLTextParagraph para = model.getParagraph(i);
				System.err.println("PARA NO " + i);
				for (ZLTextParagraph.EntryIterator it = para.iterator(); it.next(); ) {
					final int elemType = it.getType();
					switch (elemType) {
						default:
							System.err.println("ELEM elemType");
							break;
						case ZLTextParagraph.Entry.TEXT:
							System.err.println("ELEM TEXT: " + new String(it.getTextData(), it.getTextOffset(), it.getTextLength()));
							break;
						case ZLTextParagraph.Entry.IMAGE:
							System.err.println("ELEM IMAGE");
							break;
						case ZLTextParagraph.Entry.CONTROL:
							System.err.println("ELEM CONTROL " + it.getControlKind() + " " + it.getControlIsStart());
							break;
						case ZLTextParagraph.Entry.HYPERLINK_CONTROL:
							System.err.println("ELEM HYPERLINK_CONTROL");
							break;
						case ZLTextParagraph.Entry.STYLE_CSS:
							System.err.println("ELEM STYLE_CSS " + it.getStyleEntry());
							break;
						case ZLTextParagraph.Entry.STYLE_OTHER:
							System.err.println("ELEM STYLE_OTHER " + it.getStyleEntry());
							break;
						case ZLTextParagraph.Entry.STYLE_CLOSE:
							System.err.println("ELEM STYLE_CLOSE");
							break;
						case ZLTextParagraph.Entry.FIXED_HSPACE:
							System.err.println("ELEM FIXED_HSPACE");
							break;
					}
				}
			}
		}
		System.err.println("--- MODEL DUMP ---");
	}
}
