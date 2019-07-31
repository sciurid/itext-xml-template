package me.chenqiang.pdf.xml.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.TableComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.AttributeValueParser;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class TableHandler extends BasicTemplateElementHandler<TableComposer, Table> {
	private TableComposer tplTbl;
	public TableHandler(TemplateContext context, DocumentComposer tplDoc) {
		super(context, tplDoc::append);
	}
	
	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplTbl = new TableComposer();
		Element current = elementPath.getCurrent();		
		for (Attribute attr : current.attributes()) {
			String attrName = attr.getName();
			if (AttributeRegistry.WIDTHS.equals(attrName)) {
				AttributeValueParser parser = new AttributeValueParser(attr.getName(), attr.getValue());
				UnitValue [] widths = parser.getUnitValueArray();
				if(widths.length > 0) {
					this.tplTbl.setColumns(widths);
				}
			}
			else if (AttributeRegistry.COLUMNS.equals(attrName)) {
				AttributeValueParser parser = new AttributeValueParser(attr.getName(), attr.getValue());
				Integer columns = parser.getInteger();
				if(columns != null && columns > 0) {
					this.tplTbl.setColumns(columns);
				}
			} 
		}
		
		elementPath.addHandler("header", new TableRowHander(this.context, this.tplTbl.getHeader()));
		elementPath.addHandler("body", new TableRowHander(this.context, this.tplTbl.getBody()));
		elementPath.addHandler("footer", new TableRowHander(this.context, this.tplTbl.getFooter()));		
	}

	@Override
	protected TableComposer create(ElementPath elementPath) {
		return this.tplTbl;
	}

	@Override
	protected Map<String, BiFunction<String, String, ? extends Consumer<? super Table>>> getAttributeMap() {
		return this.context.getAttributeRegistry().getTableMap();
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
