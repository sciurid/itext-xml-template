package me.chenqiang.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;

public abstract class PdfDocumentEventHandler implements IEventHandler {

	@Override
	public void handleEvent(Event event) {
		PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        this.handleEvent(docEvent, pdfDoc, page);
	}
	
	protected abstract void handleEvent(PdfDocumentEvent event, PdfDocument pdfDoc, PdfPage page);
}