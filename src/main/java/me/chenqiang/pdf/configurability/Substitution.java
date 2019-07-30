package me.chenqiang.pdf.configurability;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Substitution {
	private Substitution() {}
	public static final Pattern VARIABLE = Pattern.compile("(?<!\\\\)\\$\\{([\\p{L}\\d]+)\\}");
	public static boolean isSubstitutable(String original) {
		return VARIABLE.matcher(original).find();
	}
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
	
	public static void substitute(Collection<?> components, Map<String, String> params) {
		if(params == null) {
			return;
		}
		components.stream()
		.filter(StringStub.class::isInstance)
		.map(StringStub.class::cast)
		.forEach(stub -> stub.substitute(params));
	}
	
	public static void reset(Collection<?> components) {
		components.stream()
		.filter(StringStub.class::isInstance)
		.map(StringStub.class::cast)
		.forEach(StringStub::reset);
	}
}
