package me.chenqiang.pdf.template;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AbstractElement;

public interface ElementTemplate<S extends AbstractElement<S>> {
	public <T extends AbstractElement<T>> S process(Document doc, PdfDocument pdf, PdfWriter writer, T container);
}
