package me.chenqiang.pdf.xml.handler;

import java.util.List;
import java.util.function.BiConsumer;

import org.dom4j.ElementPath;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import me.chenqiang.pdf.composer.TableComposer;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class IfCellHandler extends IfHandler{
	protected TableComposer tplTbl;
	protected BiConsumer<Table, Cell> appender;
	protected List<String []> attributes;
	
	public IfCellHandler(TemplateContext context, TableComposer tplTbl,
			BiConsumer<Table, Cell> appender, List<String[]> attributes) {
		super(context, tplTbl::append);
		this.tplTbl = tplTbl;
		this.appender = appender;
		this.attributes = attributes;
	}

	@Override
	protected void registerSubHandlers(ElementPath elementPath) {
		new TableCellHandler(this.context, this.conditional, this.appender, attributes).register(elementPath);
	}
}
