package me.chenqiang.pdf;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtilEvaluator implements ParameterEvaluator {
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtilEvaluator.class);
	public static final Pattern VARIABLE = Pattern.compile("(?<!\\\\)\\$\\{\\s*([\\p{L}\\d\\.\\(\\)\\[\\]]+)\\s*\\}");
	
	@Override
	public String evaluate(Map<String, Object> params, String text) {
		Matcher m = VARIABLE.matcher(text);
		StringBuffer sb = new StringBuffer(text.length() * 2);
		while (m.find()) {
			String key = m.group(1);
			try {
				String value = BeanUtils.getProperty(params, key);
				m.appendReplacement(sb, Matcher.quoteReplacement(value == null ? String.format("${%s}", key) : value));
			}
			catch(RuntimeException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LOGGER.error("ERROR IN EVALUATING BEAN PROPERTY VALUE STRING: ", e);
				m.appendReplacement(sb, String.format("EXPRESSION_ERROR: \"%s\"", key));
			}
			
		}
		m.appendTail(sb);
		return sb.toString().replaceAll("\\\\\\$\\{", Matcher.quoteReplacement("${"));		
	}

	@Override
	public Object getProperty(Map<String, Object> params, String text) {
		Matcher m = VARIABLE.matcher(text.trim());
		if(m.matches()) {
			try {
				return PropertyUtils.getProperty(params, m.group(1));
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LOGGER.error("ERROR IN EVALUATING BEAN PROPERTY VALUE OBJECT: ", e);
				return null;
			}
		}
		return null;
	}

}
