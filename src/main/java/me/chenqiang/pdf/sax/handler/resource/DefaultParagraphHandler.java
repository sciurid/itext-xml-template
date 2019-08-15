package me.chenqiang.pdf.sax.handler.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

import me.chenqiang.pdf.sax.TemplateContext;

public class DefaultParagraphHandler implements ElementHandler{
	protected TemplateContext context;
	
	public DefaultParagraphHandler(TemplateContext context) {
		this.context = context;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		List<String[]> arrAttr = 
				current.attributes().stream()
				.map(attr -> new String[] {attr.getName(), attr.getValue()})
				.collect(Collectors.toList());
		this.context.setDefaultParagraphStyle(arrAttr);	
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		// DO NOTHING.
	}

	public void register(ElementPath path) {
		path.addHandler("default-paragraph-style", this);
	}
}
