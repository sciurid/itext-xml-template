package me.chenqiang.pdf.xml.handler;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.composer.TableComposer;
import me.chenqiang.pdf.composer.TableComposer.Row;
import me.chenqiang.pdf.xml.TemplateContext;

public class TableRowHander implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableRowHander.class);
	protected TemplateContext context;
	protected TableComposer.Row row;

	public TableRowHander(TemplateContext context, Row row) {
		this.context = context;
		this.row = row;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {}", elementPath.getPath());
		Element current = elementPath.getCurrent();
		this.row.setAll(BasicTemplateElementHandler.getModifiers(current, this.context.getAttributeRegistry().getCellMap()));
		elementPath.addHandler("cell", new TableCellHandler(this.context, this.row));
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		LOGGER.debug("[END] {}", elementPath.getPath());
	}

}
