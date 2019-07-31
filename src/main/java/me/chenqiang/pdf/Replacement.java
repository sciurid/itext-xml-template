package me.chenqiang.pdf;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.chenqiang.pdf.component.DataParameterPlaceholder;
import me.chenqiang.pdf.component.PdfElementComposer;
import me.chenqiang.pdf.component.StringParameterPlaceholder;
import me.chenqiang.pdf.component.StringStub;
import me.chenqiang.pdf.composer.DocumentComposer;

public final class Replacement {
	private Replacement() {}
	public static final Pattern VARIABLE = Pattern.compile("(?<!\\\\)\\$\\{([\\p{L}\\d]+)\\}");
	public static boolean isSubstitutable(String original) {
		return VARIABLE.matcher(original).find();
	}
	
	public static DocumentComposer replace(DocumentComposer tpl, Map<String, String> subMap, 
			Map<String, String> textParams, Map<String, byte[]> dataParams) {
		DocumentComposer composer = tpl.copy();
		substitute(composer, subMap);
		parameterize(composer, textParams, dataParams);
		return composer;		
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
	
	public static void substitute(Iterable<?> components, final Map<String, String> params) {
		if(params == null) {
			return;
		}
		for(Object comp : components) {
			if(comp instanceof StringStub) {
				((StringStub)comp).substitute(params);
			}
			if(comp instanceof Iterable) {
				substitute((Iterable<?>)comp, params);
			}
		}	
	}
	
	public static void parameterize(Iterable<?> item, Map<String, String> textMap, Map<String, byte []> dataMap) {
		for(Object comp : item) {
			if(comp instanceof PdfElementComposer) {
				String id = ((PdfElementComposer<?, ?>) comp).getId();
				if(id != null) {
					if(comp instanceof StringParameterPlaceholder && textMap != null && textMap.containsKey(id)) {
						((StringParameterPlaceholder)comp).setParameter(textMap.get(id));
					}
					else if(comp instanceof DataParameterPlaceholder && dataMap != null && dataMap.containsKey(id)) {
						((DataParameterPlaceholder)comp).setParameter(dataMap.get(id));
					}
				}
			}
			if(comp instanceof Iterable<?>) {
				parameterize((Iterable<?>)comp, textMap, dataMap);
			}
		};
	}
}
