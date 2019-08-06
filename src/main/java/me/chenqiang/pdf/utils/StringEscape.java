package me.chenqiang.pdf.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringEscape {	
	private StringEscape() {
	}
	
	protected static final Pattern ESCAPING_PATTERN = Pattern.compile("\\\\\\\\|\\\\n|\\\\r|\\\\t");
	protected static final Map<String, String> REPLACEMENT = Map.of(
			"\\\\", "\\", "\\r", "\r", "\\n", "\n",	"\\t", "\t"
			);
	public static String escapeNodeText(String text) {
		if(text == null) {
			return null;
		}
		Matcher m = ESCAPING_PATTERN.matcher(text.replaceAll("(\\s*[\\r\\n]+\\s*)+", ""));
		StringBuilder sb = new StringBuilder();
		while(m.find()) {
			m.appendReplacement(sb, Matcher.quoteReplacement(REPLACEMENT.get(m.group())));
		}
		m.appendTail(sb);
		return sb.toString();
	}
}
