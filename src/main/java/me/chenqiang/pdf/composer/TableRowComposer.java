package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.itextpdf.layout.element.Cell;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.component.ParagraphComponent;
import me.chenqiang.pdf.component.PdfElementComposer;
import me.chenqiang.pdf.component.TableCellComponent;

public class TableRowComposer implements PdfElementComposer<Cell, TableRowComposer> {
	protected String id;
	protected final List<TableCellComposer> components;

	public TableRowComposer() {
		this.components = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Class<Cell> getElementClass() {
		return Cell.class;
	}

	public TableRowComposer add(TableCellComposer tplCell) {
		this.components.add(tplCell);
		return this;
	}

	public TableRowComposer add(TableCellComponent tplCellComp) {
		TableCellComposer tplCell = new TableCellComposer();
		tplCell.append(tplCellComp);
		this.add(tplCell);
		return this;
	}

	public TableRowComposer add(TableCellComponent[] tplCellComps) {
		TableCellComposer tplCell = new TableCellComposer();
		for (TableCellComponent tcc : tplCellComps) {
			tplCell.append(tcc);
		}
		this.add(tplCell);
		return this;
	}

	public TableRowComposer add(ParagraphComponent[] tplParaComps) {
		ParagraphComposer tplPara = new ParagraphComposer();
		for (ParagraphComponent tplEle : tplParaComps) {
			tplPara.append(tplEle);
		}
		this.add(tplPara);
		return this;
	}

	@Override
	public void setAttribute(Consumer<? super Cell> attribute) {
		this.components.forEach(cell -> cell.setAttribute(attribute));
	}

	@Override
	public void setAllAttributes(Collection<? extends Consumer<? super Cell>> attribute) {
		this.components.forEach(cell -> cell.setAllAttributes(attribute));
	}

	public TableRowComposer set(Consumer<? super Cell> attribute) {
		this.setAttribute(attribute);
		return this;
	}

	public TableRowComposer setAll(Collection<? extends Consumer<? super Cell>> attributes) {
		this.setAllAttributes(attributes);
		return this;
	}

	@Override
	public Cell produce(DocumentContext context) {
		throw new UnsupportedOperationException();
	}

}
