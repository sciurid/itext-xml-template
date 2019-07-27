package me.chenqiang.pdf.template.table;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.itextpdf.layout.element.Cell;

import me.chenqiang.pdf.template.BasicElementTemplate;
import me.chenqiang.pdf.template.element.CellComponent;
import me.chenqiang.pdf.template.element.ElementTemplate;

public class CellTemplate extends BasicElementTemplate<Cell, CellTemplate>
implements ElementTemplate<Cell>, TableComponent{
	protected final List<CellComponent> components;
	protected int colspan = 1;
	protected int rowspan = 1;

	public CellTemplate() {
		this.components = new ArrayList<>();
	}
	
	public CellTemplate setColspan(int colspan) {
		this.colspan = colspan;
		return this;
	}

	public CellTemplate setRowspan(int rowspan) {
		this.rowspan = rowspan;
		return this;
	}

	public void inheritAttributes(List<? extends Consumer<? super Cell>> rowAttributes) {
		this.attributes.addAll(0, rowAttributes);
	}
	
	public CellTemplate append(CellComponent component) {
		this.components.add(component);
		return this;
	}
	
	@Override
	protected Cell create() {
		Cell cell = (this.rowspan > 1 || this.colspan > 1) ? new Cell(this.rowspan, this.colspan) : new Cell();
		this.components.forEach(item -> item.process(cell));
		return cell;
	}
}
