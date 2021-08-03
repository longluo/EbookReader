package com.longluo.ebookreader.fbreader.options;

import com.longluo.zlibrary.core.options.*;

public class FooterOptions {
	enum ProgressDisplayType {
		dontDisplay,
		asPages,
		asPercentage,
		asPagesAndPercentage
	}

	public final ZLBooleanOption ShowTOCMarks;
	public final ZLIntegerRangeOption MaxTOCMarks;
	public final ZLBooleanOption ShowClock;
	public final ZLBooleanOption ShowBattery;
	public final ZLEnumOption<ProgressDisplayType> ShowProgress;
	public final ZLStringOption Font;

	public FooterOptions() {
		ShowTOCMarks = new ZLBooleanOption("Options", "FooterShowTOCMarks", true);
		MaxTOCMarks = new ZLIntegerRangeOption("Options", "FooterMaxTOCMarks", 10, 1000, 100);
		ShowClock = new ZLBooleanOption("Options", "ShowClockInFooter", true);
		ShowBattery = new ZLBooleanOption("Options", "ShowBatteryInFooter", true);
		ShowProgress = new ZLEnumOption<ProgressDisplayType>(
			"Options", "DisplayProgressInFooter", ProgressDisplayType.asPages
		);
		final ZLBooleanOption oldShowProgress =
			new ZLBooleanOption("Options", "ShowProgressInFooter", true);
		if (!oldShowProgress.getValue()) {
			oldShowProgress.setValue(true);
			ShowProgress.setValue(ProgressDisplayType.dontDisplay);
		}
		Font = new ZLStringOption("Options", "FooterFont", "Droid Sans");
	}

	public boolean showProgressAsPercentage() {
		switch (ShowProgress.getValue()) {
			case asPercentage:
			case asPagesAndPercentage:
				return true;
			default:
				return false;
		}
	}

	public boolean showProgressAsPages() {
		switch (ShowProgress.getValue()) {
			case asPages:
			case asPagesAndPercentage:
				return true;
			default:
				return false;
		}
	}
}
