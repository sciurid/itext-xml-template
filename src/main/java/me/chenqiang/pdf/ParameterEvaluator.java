package me.chenqiang.pdf;

import java.util.Map;

public interface ParameterEvaluator {
	public String evaluate(Map<String, Object> params, String text);
	public Object getProperty(Map<String, Object> params, String text);
}
