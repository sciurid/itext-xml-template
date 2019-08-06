package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.itextpdf.layout.element.Cell;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.component.TableCellComponent;

public class TableCellComposer extends BasicElementComposer<Cell, TableCellComposer>
implements Iterable<TableCellComponent>
{
	protected final List<TableCellComponent> components;
	protected final int colspan;
	protected final int rowspan;

	public TableCellComposer() {
		this(1, 1);
	}
	
	public TableCellComposer(int rowspan, int colspan) {
		super(Cell.class);
		this.components = new ArrayList<>();
		this.rowspan = rowspan;
		this.colspan = colspan;
	}
	
	public void inheritAttributes(List<? extends Consumer<? super Cell>> rowAttributes) {
		this.attributes.addAll(0, rowAttributes);
	}
	
	public TableCellComposer append(TableCellComponent component) {
		if(component != null) {
			this.components.add(component);
		}
		return this;
	}
	
	public TableCellComposer insertAt(TableCellComponent component, int pos) {
		if(component != null) {
			this.components.add(pos, component);
		}
		return this;
	}
	
	@Override
	protected Cell create(DocumentContext context) {
		Cell cell = (this.rowspan > 1 || this.colspan > 1) ? new Cell(this.rowspan, this.colspan) : new Cell();
		this.components.forEach(item -> item.process(cell, context));
		return cell;
	}
	
	public static class NewRow extends TableCellComposer {
		private NewRow() {
		}
		public static final NewRow INSTANCE = new NewRow();

		@Override
		public void inheritAttributes(List<? extends Consumer<? super Cell>> rowAttributes) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TableCellComposer append(TableCellComponent component) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected Cell create(DocumentContext context) {
			throw new UnsupportedOperationException();
		}
		
	}

	@Override
	public Iterator<TableCellComponent> iterator() {
		return this.components.iterator();
	}
}
