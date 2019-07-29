package me.chenqiang.pdf.xml.handler;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import com.itextpdf.layout.element.Cell;

import me.chenqiang.pdf.composer.ComposerDirectory;
import me.chenqiang.pdf.composer.StringComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.composer.TableRowComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.AttributeValueParser;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class TableCellHandler extends BasicTemplateElementHandler<TableCellComposer, Cell> {
	private TableCellComposer tplCell;

	public TableCellHandler(TemplateContext context, ComposerDirectory directory, TableRowComposer row) {
		super(context, directory, row::add);
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
				if ("text".equals(nodeName) || "paragraph".equals(nodeName) || "image".equals(nodeName)) {
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
			String attrName = attr.getName();
			if (AttributeRegistry.ROW_SPAN.equals(attrName)) {
				AttributeValueParser parser = new AttributeValueParser(attr.getName(), attr.getValue());
				Integer rowspan = parser.getInteger();
				if (rowspan != null && rowspan > 1) {
					this.tplCell.setRowspan(rowspan);
				}
			} else if (AttributeRegistry.COL_SPAN.equals(attrName)) {
				AttributeValueParser parser = new AttributeValueParser(attr.getName(), attr.getValue());
				Integer cellspan = parser.getInteger();
				if (cellspan != null && cellspan > 1) {
					this.tplCell.setColspan(cellspan);
				}
			}
		}
		
		elementPath.addHandler("text", new TextHandler(this.context, this.directory, this.tplCell));
		elementPath.addHandler("paragraph", new ParagraphHandler(this.context, this.directory, this.tplCell));
		elementPath.addHandler("image", new ImageHandler(this.context, this.directory, this.tplCell));
		elementPath.addHandler("barcode", new BarcodeHandler(this.context, this.directory, this.tplCell));
	}

	@Override
	protected Map<String, BiFunction<String, String, ? extends Consumer<? super Cell>>> getAttributeMap() {
		return this.context.getAttributeRegistry().getCellMap();
	}	
}
