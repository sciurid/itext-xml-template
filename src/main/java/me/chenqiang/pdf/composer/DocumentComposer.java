package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

public class DocumentComposer implements StringStub, Iterable<DocumentComponent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentComposer.class);

	protected String id;
	protected List<DocumentComponent> components;
	protected List<Consumer<? super Document>> attributes;
	protected List<Pair<String, IEventHandler>> handlers;

	public DocumentComposer() {
		this.components = new ArrayList<>();
		this.attributes = new ArrayList<>();
		this.handlers = new ArrayList<>();
	}

	public DocumentComposer append(DocumentComponent component) {
		this.components.add(component);
		return this;
	}

	public DocumentComposer setAttribute(Consumer<? super Document> attribute) {
		this.attributes.add(attribute);
		return this;
	}
	
	public DocumentComposer registerEventHandler(String event, IEventHandler handler) {
		this.handlers.add(Pair.of(event, handler));
		return this;
	}

	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {
		this.handlers.forEach(pair -> {
			pdf.addEventHandler(pair.getKey(), pair.getValue());
		});
		this.attributes.forEach(attr -> attr.accept(doc));
		if (this.components.isEmpty()) {
			LOGGER.warn("Empty document found.");
			pdf.addNewPage();
		} else {
			this.components.forEach(component -> component.process(doc, pdf, writer));
		}
	}

	@Override
	public void substitute(Map<String, String> params) {
		this.components.stream()
		.filter(comp -> comp instanceof StringStub)
		.forEach(comp -> ((StringStub)comp).substitute(params));
	}

	@Override
	public Iterator<DocumentComponent> iterator() {
		return this.components.iterator();
	}
}
