package me.chenqiang.pdf.xml.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import me.chenqiang.pdf.composer.TableComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class TableRowHander implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableRowHander.class);
	protected TemplateContext context;
	protected TableComposer tbl;
	protected BiConsumer<Table, Cell> appender;

	public TableRowHander(TemplateContext context, TableComposer tbl, BiConsumer<Table, Cell> appender) {
		this.context = context;
		this.tbl = tbl;
		this.appender = appender;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {}", elementPath.getPath());
		
		List<String []> attributes = new ArrayList<>();
		Element current = elementPath.getCurrent();
		String styleId = current.attributeValue(AttributeRegistry.STYLE);	
		if(styleId != null){
			List<String []> styleAttributes = this.context.getPredefinedStyle(styleId);
			if(styleAttributes != null) {
				attributes.addAll(styleAttributes);
			}
		}
		current.attributes().forEach(attr -> attributes.add(new String[] {attr.getName(), attr.getValue()}));
		
		new TableCellHandler(this.context, this.tbl, this.appender, attributes).register(elementPath);
		new ForEachCellHandler(this.context, this.tbl, this.appender, attributes).register(elementPath);
		new IfCellHandler(this.context, this.tbl, this.appender, attributes).register(elementPath);
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		LOGGER.debug("[END] {}", elementPath.getPath());
	}
}
