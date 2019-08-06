package me.chenqiang.pdf.component;

import com.itextpdf.layout.element.Div;

import me.chenqiang.pdf.DocumentContext;

public interface DivComponent {
	public void process(Div div, DocumentContext context);
}