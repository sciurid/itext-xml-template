package me.chenqiang.pdf.component;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

public interface DocumentComponent {
	public void process(Document doc, PdfDocument pdf, PdfWriter writer);
}