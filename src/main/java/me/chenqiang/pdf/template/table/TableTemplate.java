package me.chenqiang.pdf.template.table;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.BlockElement;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import me.chenqiang.pdf.template.BasicElementTemplate;
import me.chenqiang.pdf.template.element.DocumentComponentTemplate;
import me.chenqiang.pdf.template.element.ElementTemplate;
import me.chenqiang.pdf.template.element.StringTemplate;

public class TableTemplate extends BasicElementTemplate<Table, TableTemplate>
implements DocumentComponentTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableTemplate.class);
	
	public static class Row {
		protected final List<TableComponent> components;
		protected final List<Consumer<Cell>> attributes;
		
		public Row() {
			this.components = new ArrayList<>();
			this.attributes = new ArrayList<>();
		}

		public void add(TableComponent tplCell) {
			if(tplCell instanceof CellTemplate) {
				((CellTemplate)tplCell).inheritAttributes(this.attributes);
			}
			this.components.add(tplCell);
		}
		
		public void set(Consumer<Cell> attribute) {
			this.attributes.add(attribute);
		}
	}	
	
	protected Supplier<Table> creator;
	protected Row header = new Row();
	protected Row content = new Row();
	protected Row footer = new Row();
	
	public TableTemplate(int numColumns) {
		this.creator = () -> new Table(numColumns);
	}
	public TableTemplate(float [] pointColumnWidths) {
		this.creator = () -> new Table(pointColumnWidths);		
	}
	public TableTemplate(UnitValue [] columnWidths) {
		this.creator = () -> new Table(columnWidths);
	}
	
	public Row getHeader() {
		return header;
	}

	public Row getContent() {
		return content;
	}

	public Row getFooter() {
		return footer;
	}
	
	@Deprecated
	protected static void appendAll(List<TableComponent> cells, Consumer<String> cs, Consumer<Cell> cc, Consumer<BlockElement<?>> cb, Runnable nr) {
		for(TableComponent cell : cells) {
			if(cell instanceof NewRow) {
				nr.run();
			}
			else if(cell instanceof StringTemplate) {
				cs.accept(cell.toString());
			}
			else if(cell instanceof ElementTemplate) {
				Object obj = ((ElementTemplate<?>) cell).produce(null);
				if(obj instanceof Cell) {
					cc.accept((Cell)obj);
					LOGGER.debug("Cell position: {}, {}", ((Cell)obj).getRow(), ((Cell)obj).getCol());
				}
				else if(obj instanceof BlockElement) {
					cb.accept((BlockElement<?>)obj);
				}
				else {
					LOGGER.error("Unsupported table content: {}", obj.getClass());
				}
			}
			else {
				LOGGER.error("Unrecognized TableComponent: {}", cell.getClass());
			}
		}
	}
	
	@Override
	protected Table create() {
		Table tbl = this.creator.get();
		int numColumns = tbl.getNumberOfColumns();
		LOGGER.debug("\r\nTable header: {} cells\\r\\nTable content: {} cells\\r\\nTable footer: {} cells", 
				this.header.components.size(), this.content.components.size(), this.footer.components.size());
		if(this.header.components.size() > numColumns) {
			LOGGER.warn("Table header: {} cells, exceeding number of columns : {}.", this.header.components.size(), numColumns);
		}
		if(this.footer.components.size() > numColumns) {
			LOGGER.warn("Table footer: {} cells, exceeding number of columns : {}.", this.footer.components.size(), numColumns);
		}
		appendAll(this.header.components, tbl::addHeaderCell, tbl::addHeaderCell, tbl::addHeaderCell, tbl::startNewRow);
		appendAll(this.content.components, tbl::addCell, tbl::addCell, tbl::addCell, tbl::startNewRow);
		appendAll(this.footer.components, tbl::addFooterCell, tbl::addFooterCell, tbl::addFooterCell, tbl::startNewRow);
		return tbl;
	}
	
	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {
		doc.add(this.<Void>produce(null));
	}

}
