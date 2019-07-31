package me.chenqiang.pdf.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Substitution {
	private Substitution() {}
	public static final Pattern VARIABLE = Pattern.compile("(?<!\\\\)\\$\\{([\\p{L}\\d]+)\\}");
	public static String substitute(String original, Map<String, String> params) {
		Matcher m = VARIABLE.matcher(original);
		StringBuffer sb = new StringBuffer(original.length());
		while (m.find()) {
			String key = m.group(1);
			String value = params.getOrDefault(key, "");
			m.appendReplacement(sb, Matcher.quoteReplacement(value == null ? "" : value));
		}
		m.appendTail(sb);
		return sb.toString().replaceAll("\\\\\\$\\{", Matcher.quoteReplacement("${"));
	}
	
	public static boolean isSubstitutable(String original) {
		return VARIABLE.matcher(original).find();
	}
}
