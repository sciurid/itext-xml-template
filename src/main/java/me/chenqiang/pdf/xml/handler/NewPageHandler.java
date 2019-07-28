package me.chenqiang.pdf.xml.handler;

import org.dom4j.ElementPath;

import me.chenqiang.pdf.composer.AreaBreakComposer;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.xml.TemplateContext;

public class NewPageHandler extends BasicTemplateElementHandler<AreaBreakComposer>{
	
	public NewPageHandler(TemplateContext context, DocumentComposer doc) {
		super(context, doc::append);
	}

	@Override
	protected AreaBreakComposer produce(ElementPath elementPath) {
		return AreaBreakComposer.getNextPage();
	}

}
