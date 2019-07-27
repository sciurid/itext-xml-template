package me.chenqiang.pdf.xml.node;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.composer.TableComposer;
import me.chenqiang.pdf.composer.TableComposer.Row;
import me.chenqiang.pdf.xml.AttributeRegistry;

public class TableRowHander implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableRowHander.class);
	protected AttributeRegistry attrFactory;
	protected TableComposer.Row row;

	public TableRowHander(AttributeRegistry attrFactory, Row row) {
		this.attrFactory = attrFactory;
		this.row = row;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {}", elementPath.getPath());
		Element current = elementPath.getCurrent();
		this.row.setAll(TemplateElementHandler.getModifiers(current, this.attrFactory.getBlockElementMap()));
		elementPath.addHandler("cell", new TableCellHandler(this.attrFactory, this.row));
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		LOGGER.debug("[END] {}", elementPath.getPath());
	}

}
