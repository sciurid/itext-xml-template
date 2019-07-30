package me.chenqiang.pdf.xml.handler.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class PredefinedStyleHandler implements ElementHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(PredefinedStyleHandler.class);
	protected TemplateContext context;
	
	public PredefinedStyleHandler(TemplateContext context) {
		this.context = context;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		String id = current.attributeValue(AttributeRegistry.ID);
		if(id == null) {
			if(LOGGER.isWarnEnabled()) {
			String message = current.attributes().stream()
					.map(attr -> String.format("%s=%s", attr.getName(), attr.getValue())).collect(Collectors.joining(","));
			LOGGER.warn("Predefined style without ID is ignored. ({} [{}])", current.getPath(), message);
			}
		}
		List<String[]> arrAttr = 
				current.attributes().stream().filter(attr -> !AttributeRegistry.ID.equals(attr.getName()))
				.map(attr -> new String[] {attr.getName(), attr.getValue()})
				.collect(Collectors.toList());
		this.context.registerStyle(id, arrAttr);
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		
	}
	
	public void register(ElementPath path) {
		path.addHandler("style", this);
	}
}
