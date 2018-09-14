package com.gedoumi.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtil {

	public static Map<String, Object> parseJSON2Map(String rs) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONParser parser = new JSONParser();
		// 最外层解析
		JSONObject json;
		try {
			json = (JSONObject) parser.parse(rs);
			for (Object k : json.keySet()) {
				Object v = json.get(k);
				// 如果内层还是数组的话，继续解析
				if (v instanceof JSONArray) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					Iterator<JSONObject> it = ((JSONArray) v).iterator();
					while (it.hasNext()) {
						JSONObject json2 = it.next();
						list.add(parseJSON2Map(json2.toString()));
					}
					map.put(k.toString(), list);
				} else {
					map.put(k.toString(), v);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}

}
