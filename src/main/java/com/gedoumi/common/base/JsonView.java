package com.gedoumi.common.base;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.alibaba.fastjson.JSONObject;

public class JsonView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();  
		out.print(JSONObject.toJSONString(model));
	}

}
