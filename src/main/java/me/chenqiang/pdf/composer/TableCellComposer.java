package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.itextpdf.layout.element.Cell;

public class TableCellComposer extends BasicElementPropertyContainerComposer<Cell, TableCellComposer>
implements ElementComposer<Cell>{
	
	public static interface TableCellComponent {
		public void process(Cell cell);
	}
	
	protected final List<TableCellComponent> components;
	protected int colspan = 1;
	protected int rowspan = 1;

	public TableCellComposer() {
		this.components = new ArrayList<>();
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
	public void substitute(Map<String, String> params) {
		this.components.stream()
		.filter(comp -> comp instanceof StringStub)
		.forEach(comp -> ((StringStub)comp).substitute(params));
	}
	
}
