package me.chenqiang.pdf.xml.handler;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.attribute.BackgroundColorAttribute;
import me.chenqiang.pdf.attribute.FontColorAttribute;
import me.chenqiang.pdf.composer.TableRowComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.CompositeAttribute;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class TableRowHander implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableRowHander.class);
	protected TemplateContext context;
	protected TableRowComposer row;

	public TableRowHander(TemplateContext context, TableRowComposer row) {
		this.context = context;
		this.row = row;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {}", elementPath.getPath());
		Element current = elementPath.getCurrent();
		AttributeRegistry attributeRegistry = this.context.getAttributeRegistry();
		CompositeAttribute compositeAttribute = attributeRegistry.getCompositeAttribute(current.attributes());
		FontColorAttribute fontColorAttr = compositeAttribute.getFontColor();
		if(fontColorAttr != null) {
			this.row.setAttribute(fontColorAttr::apply);
		}
		BackgroundColorAttribute backgroundColorAttr = compositeAttribute.getBackgroundColor();
		if(backgroundColorAttr != null) {
			this.row.setAttribute(backgroundColorAttr::apply);
		}
		this.row.setAllAttributes(BasicTemplateElementHandler.getModifiers(current, attributeRegistry.getCellMap()));
		elementPath.addHandler("cell", new TableCellHandler(this.context, this.row));
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		LOGGER.debug("[END] {}", elementPath.getPath());
	}

}
