package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.itextpdf.layout.element.Cell;

import me.chenqiang.pdf.attribute.BackgroundColorAttribute;
import me.chenqiang.pdf.attribute.BorderAttribute;
import me.chenqiang.pdf.attribute.FontColorAttribute;
import me.chenqiang.pdf.composer.ParagraphComposer.ParagraphComponent;
import me.chenqiang.pdf.composer.TableCellComposer.TableCellComponent;

public class TableRowComposer implements AttributedComposer<Cell>,
FontColorAttribute.Acceptor, BackgroundColorAttribute.Acceptor, BorderAttribute.Acceptor {
	protected final List<TableCellComposer> components;

	public TableRowComposer() {
		this.components = new ArrayList<>();
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
	public void accept(BorderAttribute common, BorderAttribute top, BorderAttribute right, BorderAttribute bottom,
			BorderAttribute left) {
		this.components.forEach(cell -> cell.accept(common, top, right, bottom, left));		
	}

	@Override
	public void accept(BackgroundColorAttribute fontColorAttr) {
		this.components.forEach(cell -> cell.accept(fontColorAttr));
	}

	@Override
	public void accept(FontColorAttribute fontColorAttr) {
		this.components.forEach(cell -> cell.accept(fontColorAttr));		
	}
	
	
}
