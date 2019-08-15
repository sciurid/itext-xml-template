package me.chenqiang.pdf.sax.handler;

import java.util.Arrays;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.ElementPath;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import me.chenqiang.pdf.common.attribute.AttributeNames;
import me.chenqiang.pdf.common.attribute.AttributeValueParser;
import me.chenqiang.pdf.sax.TemplateContext;
import me.chenqiang.pdf.sax.composer.DocumentComposer;
import me.chenqiang.pdf.sax.composer.TableComposer;

public class TableHandler extends BasicTemplateElementHandler<TableComposer, Table> {
	private TableComposer tplTbl;
	public TableHandler(TemplateContext context, DocumentComposer tplDoc) {
		super(context, tplDoc::append);
	}
	
	
	
	@Override
	protected List<String> listIgnoredAttributes() {
		 List<String> list = super.listIgnoredAttributes();
		 list.addAll(LAYOUT_ATTRS);
		 return list;
	}

	protected static final List<String> LAYOUT_ATTRS = List.of(AttributeNames.WIDTHS, AttributeNames.COLUMNS);

	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplTbl = new TableComposer();
		Element current = elementPath.getCurrent();		
		
		String value;
		value = current.attributeValue(AttributeNames.COLUMNS);
		if(value != null) {
			Integer columns = new AttributeValueParser(AttributeNames.WIDTHS, value).getInteger();
			if(columns != null) {
				this.tplTbl.setColumns(columns);
			}
		}
		value = current.attributeValue(AttributeNames.WIDTHS);
		if(value != null) {
			UnitValue [] widths = new AttributeValueParser(AttributeNames.WIDTHS, value).getUnitValueArray();
			if(widths != null && widths.length > 0) {
				this.tplTbl.setColumns(widths);
			}
		}	
		
		elementPath.addHandler("header", new TableRowHander(this.context, this.tplTbl, Table::addHeaderCell));
		elementPath.addHandler("body", new TableRowHander(this.context, this.tplTbl, Table::addCell));
		elementPath.addHandler("footer", new TableRowHander(this.context, this.tplTbl, Table::addFooterCell));		
	}

	@Override
	protected TableComposer create(ElementPath elementPath) {
		return this.tplTbl;
	}

	public static List<String> getElementNames() {
		return Arrays.asList("table");
	}
	@Override
	public void register(ElementPath path) {
		for(String name : getElementNames()) {
			path.addHandler(name, this);
		}
	}	
}
