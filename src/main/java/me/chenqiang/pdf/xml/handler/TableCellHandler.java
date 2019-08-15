package me.chenqiang.pdf.xml.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import me.chenqiang.pdf.composer.ForEachComposer;
import me.chenqiang.pdf.composer.IfComposer;
import me.chenqiang.pdf.composer.StringComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.composer.TableComposer;
import me.chenqiang.pdf.utils.StringEscape;
import me.chenqiang.pdf.xml.context.AttributeNames;
import me.chenqiang.pdf.xml.context.AttributeValueParser;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class TableCellHandler extends BasicTemplateElementHandler<TableCellComposer, Cell> {
	protected TableCellComposer tplCell;
	protected TableComposer tplTbl;
	protected BiConsumer<Table, Cell> appender;
	protected List<String []> attributes;
	
	public TableCellHandler(TemplateContext context, TableComposer tplTbl, BiConsumer<Table, Cell> appender, List<String []> attributes) {
		super(context, tplTbl::append);
		this.appender = appender;
		this.attributes = attributes;
	}
	
	public TableCellHandler(TemplateContext context, ForEachComposer foreach, BiConsumer<Table, Cell> appender, List<String []> attributes) {
		super(context, foreach::append);
		this.appender = appender;
		this.attributes = attributes;
	}

	public TableCellHandler(TemplateContext context, IfComposer conditional, BiConsumer<Table, Cell> appender,
			List<String[]> attributes) {
		super(context, conditional::append);
		this.appender = appender;
		this.attributes = attributes;
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
		
		Element current = elementPath.getCurrent();
		int rowspan = 1;
		int colspan = 1;
		
		String rss = current.attributeValue(AttributeNames.ROW_SPAN);
		if(rss != null) {
			Integer r = new AttributeValueParser(AttributeNames.ROW_SPAN, rss).getInteger();
			if (r != null && r > 1) {
				rowspan = r;
			}
		}
		String css = current.attributeValue(AttributeNames.COL_SPAN);
		if(css != null) {
			Integer c = new AttributeValueParser(AttributeNames.COL_SPAN, css).getInteger();
			if (c != null && c > 1) {
				colspan = c;
			}
		}
		if(rowspan == 1 && colspan == 1) {
			this.tplCell = new TableCellComposer(this.appender);
		}
		else {
			this.tplCell = new TableCellComposer(rowspan, colspan, this.appender);
		}
				
		new TextHandler(this.context, this.tplCell).register(elementPath);
		new ParagraphHandler(this.context, this.tplCell).register(elementPath);
		new ImageHandler(this.context, this.tplCell).register(elementPath);
		new BarcodeHandler(this.context, this.tplCell).register(elementPath);
		new DivHandler(this.context, this.tplCell).register(elementPath);
		

		new ForEachHandler(this.context, this.tplCell::append).register(elementPath);
	}
	
	
	
	@Override
	public void onEnd(ElementPath elementPath) {
		// TODO Auto-generated method stub
		super.onEnd(elementPath);
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
