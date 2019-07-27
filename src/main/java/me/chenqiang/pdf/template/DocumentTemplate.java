package me.chenqiang.pdf.template;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import me.chenqiang.pdf.template.element.DocumentComponentTemplate;

public class DocumentTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTemplate.class);
	
	protected List<DocumentComponentTemplate> components;
	protected List<Consumer<? super Document>> attributes;	
	
	public DocumentTemplate() {
		this.components = new ArrayList<>();
		this.attributes = new ArrayList<>();
	}
	
	public DocumentTemplate append(DocumentComponentTemplate component) {
		this.components.add(component);
		return this;
	}
	
	public DocumentTemplate set(Consumer<? super Document> attribute) {
		this.attributes.add(attribute);
		return this;
	}
	
	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {
		this.attributes.forEach(attr -> attr.accept(doc));
		if(this.components.isEmpty()) {
			LOGGER.warn("Empty document found.");
			pdf.addNewPage();
		}
		else {
			this.components.forEach(component -> component.process(doc, pdf, writer));
		}
	}
	
}
