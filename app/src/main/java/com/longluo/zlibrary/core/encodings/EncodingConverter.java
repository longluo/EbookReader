package com.longluo.zlibrary.core.encodings;

import java.nio.*;
import java.nio.charset.*;

public class EncodingConverter {
	public final String Name;
	private CharsetDecoder myDecoder;

	EncodingConverter(String encoding) {
		Name = encoding;
		myDecoder = Charset.forName(encoding).newDecoder()
			.onMalformedInput(CodingErrorAction.REPLACE)
			.onUnmappableCharacter(CodingErrorAction.REPLACE);
	}

	// we assume out is large enough for this conversion
	// returns number of filled chars in out buffer
	public int convert(byte[] in, int inOffset, int inLength, char[] out) {
		final ByteBuffer inBuffer = ByteBuffer.wrap(in, inOffset, inLength);
		final CharBuffer outBuffer = CharBuffer.wrap(out, 0, out.length);
		myDecoder.decode(inBuffer, outBuffer, false);
		return outBuffer.position();
	}

	public void reset() {
		myDecoder.reset();
	}
}
