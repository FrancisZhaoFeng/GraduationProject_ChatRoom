package com.zhbit.crs.util;

import java.util.regex.Pattern;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class tools {
	public boolean checkInput(String str) {
		int num = 0;
		num = Pattern.compile("\\d").matcher(str).find() ? num + 1 : num;
		num = Pattern.compile("[a-z]").matcher(str).find() ? num + 1 : num;
		num = Pattern.compile("[A-Z]").matcher(str).find() ? num + 1 : num;
		num = str.contains("_") ? num + 1 : num;
		return num < 3;
	}

//	public void hide() {
//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//	}
//
//	public static void hideSystemKeyBoard(Context mcontext, View v) {
//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//	}
}
