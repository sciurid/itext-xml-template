package me.chenqiang.pdf.template.element;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

public interface DocumentComponentTemplate {
	public void process(Document doc, PdfDocument pdf, PdfWriter writer);
}
