package com.romens.yjkgrab.ui.widget.reddotface.util;

public interface Callback {
	void onBefore();

	boolean onRun();

	void onAfter(boolean b);
}
