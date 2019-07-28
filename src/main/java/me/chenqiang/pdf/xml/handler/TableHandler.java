package me.chenqiang.pdf.xml.handler;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;

import com.itextpdf.layout.property.UnitValue;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.TableComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;
import me.chenqiang.pdf.xml.AttributeValueParser;
import me.chenqiang.pdf.xml.TemplateContext;

public class TableHandler extends TemplateElementHandler<TableComposer> {
//	private static final Logger LOGGER = LoggerFactory.getLogger(TableNode.class);
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
			if(AttributeRegistry.ID.equals(attrName)) {
				String id = attr.getValue();
				this.context.getComposerDirectory().registerIdentifiable(id, this.tplTbl);
			}
			else if (AttributeRegistry.WIDTHS.equals(attrName)) {
				AttributeValueParser parser = new AttributeValueParser(attr.getName(), attr.getValue());
				UnitValue [] widths = parser.getUnitValues();
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
		this.tplTbl.setAllAttributes(getModifiers(current, this.context.getAttributeRegistry().getTableMap()));
		
		elementPath.addHandler("header", new TableRowHander(this.context, this.tplTbl.getHeader()));
		elementPath.addHandler("body", new TableRowHander(this.context, this.tplTbl.getBody()));
		elementPath.addHandler("footer", new TableRowHander(this.context, this.tplTbl.getFooter()));		
	}

	@Override
	protected TableComposer produce(ElementPath elementPath) {
		return this.tplTbl;
	}
}
