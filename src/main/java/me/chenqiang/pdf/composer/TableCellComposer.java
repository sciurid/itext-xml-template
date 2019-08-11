package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.component.TableCellComponent;
import me.chenqiang.pdf.component.TableComponent;

public class TableCellComposer extends BasicElementComposer<Cell, TableCellComposer>
implements TableComponent, Iterable<TableCellComponent>
{
	protected final List<TableCellComponent> components;
	protected final int colspan;
	protected final int rowspan;
	protected final BiConsumer<Table, Cell> appender;

	public TableCellComposer(BiConsumer<Table, Cell> appender) {
		this(1, 1, appender);
	}
	
	public TableCellComposer(int rowspan, int colspan, BiConsumer<Table, Cell> appender) {
		super(Cell.class);
		this.components = new ArrayList<>();
		this.rowspan = rowspan;
		this.colspan = colspan;
		this.appender = appender;
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
	
	@Override
	public Iterator<TableCellComponent> iterator() {
		return this.components.iterator();
	}

	@Override
	public void process(Table tbl, DocumentContext context) {
		this.appender.accept(tbl, this.produce(context));
	}	
}
