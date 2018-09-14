package com.gedoumi.wx.bean;

import java.util.Map;

/**
 * 微信模板短息的基类
 * 
 * @date 2013-08-08
 */
public class TemplateWXMsgKey {
	private String value;
	private String color;
	
	
	public TemplateWXMsgKey(String value,String color){
		this.value=value;
		this.color=color;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}


}
