package me.chenqiang.pdf.xml.node;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import me.chenqiang.pdf.composer.StringComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.composer.TableComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;
import me.chenqiang.pdf.xml.AttributeValueParser;

public class TableCellHandler extends TemplateElementHandler<TableCellComposer> {
//	private static final Logger LOGGER = LoggerFactory.getLogger(TableCellNode.class);
	private TableCellComposer tplCell;
	private TableComposer.Row row;

	public TableCellHandler(AttributeRegistry attrFactory, TableComposer.Row row) {
		super(attrFactory, row::add);
		this.row = row;
	}

	@Override
	protected TableCellComposer produce(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		this.resumeTextContent(current);
		return this.tplCell;
	}

	protected void resumeTextContent(Element current) {
		int counter = 0;
		for (Node node : current.content()) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = node.getName();
				if ("text".equals(nodeName) || "paragraph".equals(nodeName)) {
					counter++;
				}
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				String cleared = node.getText().replaceAll("[\\r\\n]+\\s+", "");
				this.tplCell.insertAt(new StringComposer(cleared), counter++);
			}
		}
	}

	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplCell = new TableCellComposer();
		
		Element current = elementPath.getCurrent();
		for (Attribute attr : current.attributes()) {
			if (AttributeRegistry.ROW_SPAN.equals(attr.getName())) {
				AttributeValueParser parser = new AttributeValueParser(attr.getName(), attr.getValue());
				Integer rowspan = parser.getInteger();
				if (rowspan != null && rowspan > 1) {
					this.tplCell.setRowspan(rowspan);
				}
			} else if (AttributeRegistry.COL_SPAN.equals(attr.getName())) {
				AttributeValueParser parser = new AttributeValueParser(attr.getName(), attr.getValue());
				Integer cellspan = parser.getInteger();
				if (cellspan != null && cellspan > 1) {
					this.tplCell.setColspan(cellspan);
				}
			}
		}
		this.tplCell.inheritAttributes(this.row.getAttributes());
		this.tplCell.setAll(getModifiers(current, this.attrFactory.getBlockElementMap()));
		
		elementPath.addHandler("text", new TextHandler(this.attrFactory, this.tplCell));
		elementPath.addHandler("paragraph", new ParagraphHandler(this.attrFactory, this.tplCell));
	}
}
