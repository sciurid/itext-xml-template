package me.chenqiang.pdf.sax.composer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.sax.composer.component.DocumentComponent;
import me.chenqiang.pdf.sax.composer.component.TableComponent;

public class TableComposer extends BasicElementComposer<Table, TableComposer>
		implements DocumentComponent, Iterable<TableComponent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableComposer.class);
	protected Supplier<Table> creator;
//	protected TableRowComposer header = new TableRowComposer();
//	protected TableRowComposer body = new TableRowComposer();
//	protected TableRowComposer footer = new TableRowComposer();
	
	protected List<TableComponent> components;
	
	public TableComposer() {
		super(Table.class);
		this.components = new ArrayList<>();
	}

	public void setColumns(int numColumns) {
		this.creator = () -> new Table(numColumns);
	}

	public void setColumns(float[] pointColumnWidths) {
		this.creator = () -> new Table(pointColumnWidths);
	}

	public void setColumns(UnitValue[] columnWidths) {
		this.creator = () -> new Table(columnWidths);
	}
	
	public void append(TableComponent comp) {
		this.components.add(comp);
	}

//	public TableRowComposer getHeader() {
//		return header;
//	}
//
//	public TableRowComposer getBody() {
//		return body;
//	}
//
//	public TableRowComposer getFooter() {
//		return footer;
//	}

//	protected static void appendAll(List<TableCellComposer> comps, Consumer<Cell> cc, Runnable nr, DocumentContext context) {
//		for (TableCellComposer comp : comps) {
//			if (comp instanceof TableCellComposer.NewRow) {
//				nr.run();
//				return;
//			}
//			Cell cell = comp.produce(context);
//			cc.accept(cell);
//			LOGGER.debug("Cell position: {}, {}", cell.getRow(), cell.getCol());
//		}
//	}

	@Override
	protected Table create(DocumentContext context) {
		if (this.creator == null) {
			LOGGER.error("Compulsory table attribute missing: 'cols' or 'widths'. Table ignored.");
			return null;
		}
		Table tbl = this.creator.get();
//		int numColumns = tbl.getNumberOfColumns();
//		LOGGER.debug("Table header({}) body({}) footer({})", this.header.components.size(), this.body.components.size(),
//				this.footer.components.size());
//		if (this.header.components.size() > numColumns) {
//			LOGGER.warn("Table header: {} cells, exceeding number of columns : {}.", this.header.components.size(),
//					numColumns);
//		}
//		if (this.footer.components.size() > numColumns) {
//			LOGGER.warn("Table footer: {} cells, exceeding number of columns : {}.", this.footer.components.size(),
//					numColumns);
//		}
//		appendAll(this.header.components, tbl::addHeaderCell, tbl::startNewRow, context);
//		appendAll(this.body.components, tbl::addCell, tbl::startNewRow, context);
//		appendAll(this.footer.components, tbl::addFooterCell, tbl::startNewRow, context);
		for(TableComponent comp : this.components) {
			comp.process(tbl, context);
		}		
		return tbl;
	}

	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer, DocumentContext context) {
		Table tbl = this.produce(context);
		if (tbl != null) {
			doc.add(tbl);
		}
	}
	
	@Override
	public Iterator<TableComponent> iterator() {
		return this.components.iterator();
	}
}
