package me.chenqiang.pdf.sax.composer.component;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import me.chenqiang.pdf.DocumentContext;

public interface DocumentComponent {
	public void process(Document doc, PdfDocument pdf, PdfWriter writer, DocumentContext context);
}