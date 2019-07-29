package me.chenqiang.pdf.xml.handler;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.composer.ComposerDirectory;
import me.chenqiang.pdf.composer.TableRowComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.CompositeAttribute;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class TableRowHander implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableRowHander.class);
	protected TemplateContext context;
	protected ComposerDirectory directory;
	protected TableRowComposer row;

	public TableRowHander(TemplateContext context, ComposerDirectory directory, TableRowComposer row) {
		this.context = context;
		this.directory = directory;
		this.row = row;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {}", elementPath.getPath());
		elementPath.addHandler("cell", new TableCellHandler(this.context, this.directory, this.row));
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		AttributeRegistry attributeRegistry = this.context.getAttributeRegistry();
		CompositeAttribute compositeAttribute = attributeRegistry.getCompositeAttribute(current.attributes());
		compositeAttribute.applyFontColor(this.row).applyBackgroundColor(this.row).applyBorder(this.row);		
		this.row.setAllAttributes(BasicTemplateElementHandler.getModifiers(current, attributeRegistry.getCellMap()));
		
		LOGGER.debug("[END] {}", elementPath.getPath());
	}
		
	

}
