package me.chenqiang.pdf.xml.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
import me.chenqiang.pdf.utils.StringEscape;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.AttributeValueParser;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class TableCellHandler extends BasicTemplateElementHandler<TableCellComposer, Cell> {
	private TableCellComposer tplCell;

	public TableCellHandler(TemplateContext context, ComposerDirectory directory, TableRowComposer row) {
		super(context, directory, row::add);
	}

	@Override
	protected TableCellComposer create(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		this.resumeTextContent(current);
		return this.tplCell;
	}

	protected static final Set<String> HANDLED_ELEMENT = new TreeSet<>();
	static {
		HANDLED_ELEMENT.addAll(TextHandler.getElementNames());
		HANDLED_ELEMENT.addAll(ImageHandler.getElementNames());
		HANDLED_ELEMENT.addAll(BarcodeHandler.getElementNames());
		HANDLED_ELEMENT.addAll(ParagraphHandler.getElementNames());
		HANDLED_ELEMENT.addAll(DivHandler.getElementNames());
	}
	protected void resumeTextContent(Element current) {
		int counter = 0;
		for (Node node : current.content()) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = node.getName();
				if (HANDLED_ELEMENT.contains(nodeName)) {
					counter++;
				}
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				this.tplCell.insertAt(new StringComposer(StringEscape.escapeNodeText(node.getText())), counter++);
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
		new TextHandler(this.context, this.directory, this.tplCell).register(elementPath);
		new ParagraphHandler(this.context, this.directory, this.tplCell).register(elementPath);
		new ImageHandler(this.context, this.directory, this.tplCell).register(elementPath);
		new BarcodeHandler(this.context, this.directory, this.tplCell).register(elementPath);
		new DivHandler(this.context, this.directory, this.tplCell).register(elementPath);
	}

	@Override
	protected Map<String, BiFunction<String, String, ? extends Consumer<? super Cell>>> getAttributeMap() {
		return this.context.getAttributeRegistry().getCellMap();
	}
	
	public static List<String> getElementNames() {
		return Arrays.asList("cell", "td");
	}
	@Override
	public void register(ElementPath path) {
		for(String name : getElementNames()) {
			path.addHandler(name, this);
		}
	}	
}
