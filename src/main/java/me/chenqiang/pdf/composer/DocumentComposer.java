package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

public class DocumentComposer {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentComposer.class);
	
	protected List<DocumentComponent> components;
	protected List<Consumer<? super Document>> attributes;	
	
	public DocumentComposer() {
		this.components = new ArrayList<>();
		this.attributes = new ArrayList<>();
	}
	
	public DocumentComposer append(DocumentComponent component) {
		this.components.add(component);
		return this;
	}
	
	public DocumentComposer set(Consumer<? super Document> attribute) {
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
