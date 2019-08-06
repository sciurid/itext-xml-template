package me.chenqiang.pdf.component;

import com.itextpdf.layout.element.Cell;

import me.chenqiang.pdf.DocumentContext;

public interface TableCellComponent {
	public void process(Cell cell, DocumentContext context);
}
