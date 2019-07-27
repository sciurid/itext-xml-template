package me.chenqiang.pdf.xml.node;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;

import com.itextpdf.layout.property.UnitValue;

import me.chenqiang.pdf.xml.AttributeValueParser;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.TableComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;

public class TableHandler extends TemplateElementHandler<TableComposer> {
//	private static final Logger LOGGER = LoggerFactory.getLogger(TableNode.class);
	private TableComposer tplTbl;
	public TableHandler(AttributeRegistry attrFactory, DocumentComposer tplDoc) {
		super(attrFactory, tplDoc::append);
	}
	
	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplTbl = new TableComposer();
		Element current = elementPath.getCurrent();
		for (Attribute attr : current.attributes()) {
			if (AttributeRegistry.WIDTHS.equals(attr.getName())) {
				AttributeValueParser parser = new AttributeValueParser(attr.getName(), attr.getValue());
				UnitValue [] widths = parser.getUnitValues();
				if(widths.length > 0) {
					this.tplTbl.setColumns(widths);
				}
			}
			else if (AttributeRegistry.COLUMNS.equals(attr.getName())) {
				AttributeValueParser parser = new AttributeValueParser(attr.getName(), attr.getValue());
				Integer columns = parser.getInteger();
				if(columns != null && columns > 0) {
					this.tplTbl.setColumns(columns);
				}
			} 
		}
		this.tplTbl.setAll(getModifiers(current, this.attrFactory.getTableMap()));
		
		elementPath.addHandler("header", new TableRowHander(attrFactory, this.tplTbl.getHeader()));
		elementPath.addHandler("body", new TableRowHander(attrFactory, this.tplTbl.getBody()));
		elementPath.addHandler("footer", new TableRowHander(attrFactory, this.tplTbl.getFooter()));		
	}

	@Override
	protected TableComposer produce(ElementPath elementPath) {
		return this.tplTbl;
	}
}
