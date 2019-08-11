package me.chenqiang.pdf.component;

import com.itextpdf.layout.element.Table;

import me.chenqiang.pdf.DocumentContext;

public interface TableComponent {
	public void process(Table tbl, DocumentContext context);
}
