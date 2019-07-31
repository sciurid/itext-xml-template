package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import me.chenqiang.pdf.attribute.PaperLayout;
import me.chenqiang.pdf.component.Copyable;
import me.chenqiang.pdf.component.DocumentComponent;
import me.chenqiang.pdf.component.StringStub;

public class DocumentComposer extends BasicElementComposer<Document, DocumentComposer>
implements StringStub, Iterable<DocumentComponent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentComposer.class);

	protected List<DocumentComponent> components;
	protected WatermarkMaker watermarkMaker;
	protected PaperLayout paperLayout;

	public DocumentComposer() {
		super(Document.class);
		this.components = new ArrayList<>();
		this.watermarkMaker = new WatermarkMaker();
		this.paperLayout = new PaperLayout();
	}
	
	protected DocumentComposer(DocumentComposer origin) {
		super(origin);
		this.components = new ArrayList<>(origin.components.size());
		for(DocumentComponent comp : origin.components) {
			if(comp instanceof Copyable) {
				this.components.add((DocumentComponent)((Copyable<?>)comp).copy());
			}
			else {
				this.components.add(comp);
			}
		}
		this.paperLayout = origin.paperLayout == null ? null : origin.paperLayout.copy();
		this.watermarkMaker = origin.watermarkMaker == null ? null : origin.watermarkMaker.copy();
	}

	public WatermarkMaker getWatermarkMaker() {
		return watermarkMaker;
	}
	
	public DocumentComposer setPaperLayout(PaperLayout paperLayout) {
		this.paperLayout = paperLayout;
		return this;
	}

	public DocumentComposer append(DocumentComponent component) {
		this.components.add(component);
		return this;
	}

	@Override
	public void setAttribute(Consumer<? super Document> attribute) {
		this.attributes.add(attribute);
	}

	@Override
	public void setAllAttributes(Collection<? extends Consumer<? super Document>> attributes) {
		this.attributes.addAll(attributes);
	}
	
	@Override
	protected Document create() {
		throw new UnsupportedOperationException();
	}

	protected static final ChineseSplitCharacters CHINESE_SPLIT_CHARATERS = new ChineseSplitCharacters();
	public Document compose(PdfDocument pdf, PdfWriter writer, boolean close) {
		Document doc = new Document(pdf, this.paperLayout.getPageSize());
		doc.setSplitCharacters(CHINESE_SPLIT_CHARATERS);
		doc.setMargins(this.paperLayout.getMarginTop(), this.paperLayout.getMarginRight(),
				this.paperLayout.getMarginBottom(), this.paperLayout.getMarginLeft());
		pdf.addEventHandler(PdfDocumentEvent.END_PAGE, watermarkMaker);
		this.attributes.forEach(attr -> attr.accept(doc));
		if (this.components.isEmpty()) {
			LOGGER.warn("Empty document found.");
			pdf.addNewPage();
		} else {
			this.components.forEach(component -> component.process(doc, pdf, writer));
		}
		if(close) {
			doc.close();
		}
		return doc;
	}

	@Override
	public void substitute(Map<String, String> params) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<DocumentComponent> iterator() {
		return this.components.iterator();
	}

	@Override
	public DocumentComposer copy() {
		return new DocumentComposer(this);
	}
}
