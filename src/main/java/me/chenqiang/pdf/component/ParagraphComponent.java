package me.chenqiang.pdf.component;

import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.DocumentContext;

public interface ParagraphComponent {
	public void process(Paragraph para, DocumentContext context);
}