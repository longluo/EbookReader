package com.longluo.zlibrary.text.model;

public interface ZLTextAlignmentType {
	byte ALIGN_UNDEFINED = 0;
	byte ALIGN_LEFT = 1;
	byte ALIGN_RIGHT = 2;
	byte ALIGN_CENTER = 3;
	byte ALIGN_JUSTIFY = 4;
	byte ALIGN_LINESTART = 5; // left for LTR languages and right for RTL
}
