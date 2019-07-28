package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import me.chenqiang.pdf.composer.DocumentComposer.DocumentComponent;
import me.chenqiang.pdf.composer.ParagraphComposer.ParagraphComponent;
import me.chenqiang.pdf.composer.TableCellComposer.TableCellComponent;

public class TableComposer extends BasicElementComposer<Table, TableComposer> implements DocumentComponent, Iterable<TableCellComposer> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableComposer.class);

	public static class Row {
		protected final List<TableCellComposer> components;
		protected final List<Consumer<? super Cell>> attributes;

		public Row() {
			this.components = new ArrayList<>();
			this.attributes = new ArrayList<>();
		}

		public Row add(TableCellComposer tplCell) {
			this.components.add(tplCell);
			return this;
		}

		public Row add(TableCellComponent tplCellComp) {
			TableCellComposer tplCell = new TableCellComposer();
			tplCell.append(tplCellComp);
			this.add(tplCell);
			return this;
		}

		public Row add(TableCellComponent[] tplCellComps) {
			TableCellComposer tplCell = new TableCellComposer();
			for (TableCellComponent tcc : tplCellComps) {
				tplCell.append(tcc);
			}
			this.add(tplCell);
			return this;
		}

		public Row add(ParagraphComponent[] tplParaComps) {
			ParagraphComposer tplPara = new ParagraphComposer();
			for (ParagraphComponent tplEle : tplParaComps) {
				tplPara.append(tplEle);
			}
			this.add(tplPara);
			return this;
		}

		public void set(Consumer<? super Cell> attribute) {
			this.attributes.add(attribute);
		}

		public void setAll(Collection<? extends Consumer<? super Cell>> attribute) {
			this.attributes.addAll(attribute);
		}

		public List<Consumer<? super Cell>> getAttributes() {
			return attributes;
		}

	}

	protected Supplier<Table> creator;
	protected Row header = new Row();
	protected Row body = new Row();
	protected Row footer = new Row();

	public void setColumns(int numColumns) {
		this.creator = () -> new Table(numColumns);
	}

	public void setColumns(float[] pointColumnWidths) {
		this.creator = () -> new Table(pointColumnWidths);
	}

	public void setColumns(UnitValue[] columnWidths) {
		this.creator = () -> new Table(columnWidths);
	}

	public Row getHeader() {
		return header;
	}

	public Row getBody() {
		return body;
	}

	public Row getFooter() {
		return footer;
	}

	protected static void appendAll(List<TableCellComposer> comps, Consumer<Cell> cc, Runnable nr) {
		for (TableCellComposer comp : comps) {
			if (comp instanceof TableCellComposer.NewRow) {
				nr.run();
				return;
			}
			Cell cell = comp.<Void>produce(null);
			cc.accept(cell);
			LOGGER.debug("Cell position: {}, {}", cell.getRow(), cell.getCol());
		}
	}

	@Override
	protected Table create() {
		if (this.creator == null) {
			LOGGER.error("Compulsory table attribute missing: 'cols' or 'widths'. Table ignored.");
			return null;
		}
		Table tbl = this.creator.get();
		int numColumns = tbl.getNumberOfColumns();
		LOGGER.debug("Table header({}) body({}) footer({})", this.header.components.size(), this.body.components.size(),
				this.footer.components.size());
		if (this.header.components.size() > numColumns) {
			LOGGER.warn("Table header: {} cells, exceeding number of columns : {}.", this.header.components.size(),
					numColumns);
		}
		if (this.footer.components.size() > numColumns) {
			LOGGER.warn("Table footer: {} cells, exceeding number of columns : {}.", this.footer.components.size(),
					numColumns);
		}
		appendAll(this.header.components, tbl::addHeaderCell, tbl::startNewRow);
		appendAll(this.body.components, tbl::addCell, tbl::startNewRow);
		appendAll(this.footer.components, tbl::addFooterCell, tbl::startNewRow);
		return tbl;
	}

	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {
		Table tbl = this.<Void>produce(null);
		if (tbl != null) {
			doc.add(tbl);
		}
	}

	@Override
	public void substitute(Map<String, String> params) {
		this.header.components.forEach(comp -> comp.substitute(params));
		this.body.components.forEach(comp -> comp.substitute(params));
		this.footer.components.forEach(comp -> comp.substitute(params));
	}

	@Override
	public Iterator<TableCellComposer> iterator() {
		List<TableCellComposer> all = new ArrayList<>(
				this.header.components.size() + this.body.components.size() + this.footer.components.size());
		all.addAll(this.header.components);
		all.addAll(this.body.components);
		all.addAll(this.footer.components);
		return all.iterator();
	}	
}
