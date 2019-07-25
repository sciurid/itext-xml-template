package me.chenqiang.pdf.template;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

public class DocumentTemplate extends StyledTemplate<Document, DocumentTemplate>{
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTemplate.class);
	
	private List<DocumentComponentTemplate> components;
		
	public DocumentTemplate() {
		this.components = new ArrayList<>();
	}
	
	public void append(DocumentComponentTemplate component) {
		this.components.add(component);
	}
	
	public void process(Document document, PdfDocument pdf, PdfWriter writer) {
		this.apply(document);
		if(this.components.isEmpty()) {
			LOGGER.warn("Empty document found.");
			pdf.addNewPage();
		}
		else {
			this.components.forEach(component -> component.process(document, pdf, writer));
		}
	}
}
