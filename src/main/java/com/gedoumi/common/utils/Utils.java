package com.gedoumi.common.utils;

public class Utils {
	/**
	 * 根据选课组合返回拳种类型，多个
	 * 
	 * @Title: getMoreBoxingType
	 * @Description: TODO
	 * @param xkzh
	 * @return
	 * @return: String
	 * @author: zengmin
	 * @date: 2017年10月23日 下午3:17:59
	 */
	public static String getMoreBoxingType(String xkzh) {
		if ("AAA".equals(xkzh)) {
			return "5,2,6";
		} else if ("AAB".equals(xkzh)) {
			return "1,3,4";
		} else if ("AAC".equals(xkzh)) {
			return "5,3,2";
		} else if ("AAD".equals(xkzh)) {
			return "6,5,2";
		} else if ("ABA".equals(xkzh)) {
			return "2,5,6";
		} else if ("ABB".equals(xkzh)) {
			return "1,3,4";
		} else if ("ABC".equals(xkzh)) {
			return "1,3,5";
		} else if ("ABD".equals(xkzh)) {
			return "6,5,1";
		} else if ("BAA".equals(xkzh)) {
			return "5,2,4";
		} else if ("BAB".equals(xkzh)) {
			return "1,4,3";
		} else if ("BAC".equals(xkzh)) {
			return "5,2,3";
		} else if ("BAD".equals(xkzh)) {
			return "5,2,6";
		} else if ("BBA".equals(xkzh)) {
			return "2,5,4";
		} else if ("BBB".equals(xkzh)) {
			return "1,4,2";
		} else if ("BBC".equals(xkzh)) {
			return "1,2,5";
		} else if ("BBD".equals(xkzh)) {
			return "5,2,6";
		}
		return "";
	}
}	
