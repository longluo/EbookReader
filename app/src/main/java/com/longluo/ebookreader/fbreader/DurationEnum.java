package com.longluo.ebookreader.fbreader;

public enum DurationEnum {
	duration1(1000), duration3(3000), duration5(5000), duration10(10000), duration20(20000), duration40(40000), duration60(60000), duration300(300000);

	public final int Value;

	DurationEnum(int value) {
		Value = value;
	}
}
