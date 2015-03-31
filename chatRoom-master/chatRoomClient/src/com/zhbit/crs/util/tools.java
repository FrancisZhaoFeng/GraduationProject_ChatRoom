package com.zhbit.crs.util;

import java.util.regex.Pattern;

public class tools {
	public boolean checkInput(String str) {
		int num = 0;
		num = Pattern.compile("\\d").matcher(str).find() ? num + 1 : num;
		num = Pattern.compile("[a-z]").matcher(str).find() ? num + 1 : num;
		num = Pattern.compile("[A-Z]").matcher(str).find() ? num + 1 : num;
		num = str.contains("_") ? num + 1 : num;
		return num < 3;
	}
}
