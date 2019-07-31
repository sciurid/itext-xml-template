package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.itextpdf.layout.element.Cell;

import me.chenqiang.pdf.component.Copyable;
import me.chenqiang.pdf.component.TableCellComponent;

public class TableCellComposer extends BasicElementComposer<Cell, TableCellComposer>
implements Iterable<TableCellComponent>
{
	protected final List<TableCellComponent> components;
	protected int colspan = 1;
	protected int rowspan = 1;

	public TableCellComposer() {
		super(Cell.class);
		this.components = new ArrayList<>();
	}
	
	protected TableCellComposer(TableCellComposer origin) {
		super(origin);
		this.components = new ArrayList<>(origin.components.size());
		for(TableCellComponent comp : origin.components) {
			if(comp instanceof Copyable) {
				this.components.add((TableCellComponent)((Copyable<?>)comp).copy());
			}
			else {
				this.components.add(comp);
			}
		}
		this.colspan = origin.colspan;
		this.rowspan = origin.rowspan;
	}
	
	public TableCellComposer setColspan(int colspan) {
		this.colspan = colspan;
		return this;
	}

	public TableCellComposer setRowspan(int rowspan) {
		this.rowspan = rowspan;
		return this;
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
	protected Cell create() {
		Cell cell = (this.rowspan > 1 || this.colspan > 1) ? new Cell(this.rowspan, this.colspan) : new Cell();
		this.components.forEach(item -> item.process(cell));
		return cell;
	}
	
	public static class NewRow extends TableCellComposer {
		private NewRow() {
		}
		public static final NewRow INSTANCE = new NewRow();

		@Override
		public TableCellComposer setColspan(int colspan) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TableCellComposer setRowspan(int rowspan) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void inheritAttributes(List<? extends Consumer<? super Cell>> rowAttributes) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TableCellComposer append(TableCellComponent component) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected Cell create() {
			throw new UnsupportedOperationException();
		}
		
	}

	@Override
	public TableCellComposer copy() {
		return new TableCellComposer(this);
	}

	@Override
	public Iterator<TableCellComponent> iterator() {
		return this.components.iterator();
	}
}
