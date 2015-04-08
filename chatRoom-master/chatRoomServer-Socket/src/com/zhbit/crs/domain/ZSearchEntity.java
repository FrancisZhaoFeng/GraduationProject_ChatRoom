package com.zhbit.crs.domain;


public class ZSearchEntity implements java.io.Serializable {

	private static final long serialVersionUID = -3286564461647015368L;
	private static int LOWER_BOUNDRY_OF_AGE = 5;
	private static int UPPER_BOUNDRY_OF_AGE = 97;
	public static int FEMALE_GENDER = 0;
	public static int MALE_GENDER = 1;
	public static int BOTH_GENDER = 2;
	public static boolean SEARCH_BY_NAME = false;
	public static boolean SEARCH_BY_ELSE = true;

	private boolean searchType = SEARCH_BY_NAME;
	private int ageLower = LOWER_BOUNDRY_OF_AGE;
	private int ageUpper = UPPER_BOUNDRY_OF_AGE;
	private int sex = BOTH_GENDER;  // 0 for lady, 1 for guy, 10 for both
	private String name = null;

	public ZSearchEntity(boolean type, int lage, int uage, int sex, String name) {
		if(type == SEARCH_BY_NAME || type == SEARCH_BY_ELSE) {
			this.searchType = type;
		}
		if(lage >= LOWER_BOUNDRY_OF_AGE && lage <= UPPER_BOUNDRY_OF_AGE) {
			this.ageLower = lage;
		}
		if(uage >= LOWER_BOUNDRY_OF_AGE && uage <= UPPER_BOUNDRY_OF_AGE) {
			this.ageUpper = uage;
		}
		if(sex == FEMALE_GENDER || sex == MALE_GENDER || sex == BOTH_GENDER) {
			this.sex = sex;
		}
		this.name = name;
	}
	
	public boolean getType() {
		return searchType;
	}

	public int getLowerAge() {
		return ageLower;
	}

	public int getUpperAge() {
		return ageUpper;
	}

	public int getSex() {
		return sex;
	}

	public String getName() {
		return name;
	}

}