package com.longluo.ebookreader.fbreader.options;

import com.longluo.zlibrary.core.options.*;
import com.longluo.zlibrary.core.util.ZLColor;

import com.longluo.ebookreader.fbreader.FBView;

public class ImageOptions {
	public final ZLColorOption ImageViewBackground;

	public final ZLEnumOption<FBView.ImageFitting> FitToScreen;
	public static enum TapActionEnum {
		doNothing, selectImage, openImageView
	}
	public final ZLEnumOption<TapActionEnum> TapAction;
	public final ZLBooleanOption MatchBackground;

	public ImageOptions() {
		ImageViewBackground =
			new ZLColorOption("Colors", "ImageViewBackground", new ZLColor(255, 255, 255));
		FitToScreen =
			new ZLEnumOption<FBView.ImageFitting>("Options", "FitImagesToScreen", FBView.ImageFitting.covers);
		TapAction =
			new ZLEnumOption<TapActionEnum>("Options", "ImageTappingAction", TapActionEnum.openImageView);
		MatchBackground =
			new ZLBooleanOption("Colors", "ImageMatchBackground", true);
	}
}
