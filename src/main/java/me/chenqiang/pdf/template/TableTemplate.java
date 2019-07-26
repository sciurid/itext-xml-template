package me.chenqiang.pdf.template;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

public class TableTemplate extends StyledTemplate<Table, TableTemplate>
implements DocumentComponentTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableTemplate.class);
	
	public static class Row extends StyledTemplate<Cell, Row> {
		protected List<CellTemplate> cells;
		
		public Row() {
			this.cells = new ArrayList<>();
		}

		public void add(CellTemplate tplCell) {
			tplCell.inherit(this.modifiers);
			this.cells.add(tplCell);
		}
	}	
	
	protected Supplier<Table> creator;
	protected Row header;
	protected Row content;
	protected Row footer;
	
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
	
	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {
		Table tbl = this.creator.get();
		int numColumns = tbl.getNumberOfColumns();
		if(this.header.cells.size() > numColumns) {
			LOGGER.warn("Table header: {} cells, exceeding number of columns : {}.", this.header.cells.size(), numColumns);
		}
		else {
			LOGGER.debug("Table header: {} cells", this.header.cells.size());
		}
		
		
		
		
		if(this.footer.cells.size() > numColumns) {
			LOGGER.warn("Table footer: {} cells, exceeding number of columns : {}.", this.footer.cells.size(), numColumns);
		}
		else {
			LOGGER.debug("Table footer: {} cells", this.footer.cells.size());
		}
		
		tbl.complete();
		doc.add(tbl);
	}

}
