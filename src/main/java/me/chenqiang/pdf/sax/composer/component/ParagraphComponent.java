package me.chenqiang.pdf.sax.composer.component;

import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.DocumentContext;

public interface ParagraphComponent {
	public void process(Paragraph para, DocumentContext context);
}