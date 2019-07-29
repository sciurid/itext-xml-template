package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.itextpdf.layout.element.Cell;

import me.chenqiang.pdf.composer.ParagraphComposer.ParagraphComponent;
import me.chenqiang.pdf.composer.TableCellComposer.TableCellComponent;

public class TableRowComposer implements AttributedComposer<Cell> {
	protected final List<TableCellComposer> components;
	protected final List<Consumer<? super Cell>> attributes;

	public TableRowComposer() {
		this.components = new ArrayList<>();
		this.attributes = new ArrayList<>();
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
		this.attributes.add(attribute);
	}

	@Override
	public void setAllAttributes(Collection<? extends Consumer<? super Cell>> attribute) {
		this.attributes.addAll(attribute);
	}

	@SuppressWarnings("unchecked")
	public TableRowComposer set(Consumer<? super Cell> attribute) {
		this.setAttribute(attribute);
		return this;
	}

	@SuppressWarnings("unchecked")
	public TableRowComposer setAll(Collection<? extends Consumer<? super Cell>> attributes) {
		this.setAllAttributes(attributes);
		return this;
	}

	public List<Consumer<? super Cell>> getAttributes() {
		return attributes;
	}

}
