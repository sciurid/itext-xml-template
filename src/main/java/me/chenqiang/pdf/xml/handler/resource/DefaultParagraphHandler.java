package me.chenqiang.pdf.xml.handler.resource;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

import me.chenqiang.pdf.xml.context.TemplateContext;

public class DefaultParagraphHandler implements ElementHandler{
	protected TemplateContext context; 

	@Override
	public void onStart(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		// DO NOTHING.
	}

}
