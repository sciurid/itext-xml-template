package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.attribute.PaperLayout;
import me.chenqiang.pdf.component.DocumentComponent;

public class DocumentComposer extends BasicElementComposer<Document, DocumentComposer>
		implements Iterable<DocumentComponent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentComposer.class);
	protected String id;

	protected List<DocumentComponent> components;
	protected WatermarkMaker watermarkMaker;
	protected PaperLayout paperLayout;

	protected String author;
	protected String creator;
	protected String title;
	protected String subject;
	protected String keywords;

	public DocumentComposer() {
		super(Document.class);
		this.components = new ArrayList<>();
		this.watermarkMaker = new WatermarkMaker();
		this.paperLayout = new PaperLayout();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	protected Document create(DocumentContext context) {
		throw new UnsupportedOperationException();
	}

	protected static final ChineseSplitCharacters CHINESE_SPLIT_CHARATERS = new ChineseSplitCharacters();

	public Document compose(PdfDocument pdf, PdfWriter writer, boolean close, DocumentContext context) {
		Document doc = new Document(pdf, this.paperLayout.getPageSize());
		doc.setSplitCharacters(CHINESE_SPLIT_CHARATERS);
		doc.setMargins(this.paperLayout.getMarginTop(), this.paperLayout.getMarginRight(),
				this.paperLayout.getMarginBottom(), this.paperLayout.getMarginLeft());
		pdf.addEventHandler(PdfDocumentEvent.END_PAGE, watermarkMaker);
		this.attributes.forEach(attr -> attr.accept(doc));
		if (this.components.isEmpty()) {
			LOGGER.warn("Empty document found.");
			pdf.addNewPage();
			doc.flush();
		} else {
			this.components.forEach(component -> {
				component.process(doc, pdf, writer, context);
				doc.flush();
			});
		}
		this.setMetadata(pdf);
		if (close) {
			doc.close();
		}
		return doc;
	}

	protected void setMetadata(PdfDocument pdf) {
		PdfDocumentInfo info = pdf.getDocumentInfo();
		info.addCreationDate().addModDate();
		if (this.author != null) {
			info.setAuthor(this.author);
		}
		if (this.creator != null) {
			info.setCreator(this.creator);
		}
		if (this.title != null) {
			info.setKeywords(this.title);
		}
		if (this.subject != null) {
			info.setSubject(this.subject);
		}
		if (this.keywords != null) {
			info.setKeywords(this.keywords);
		}
	}

	@Override
	public Iterator<DocumentComponent> iterator() {
		return this.components.iterator();
	}

	public DocumentComposer setAuthor(String author) {
		this.author = author;
		return this;
	}

	public DocumentComposer setCreator(String creator) {
		this.creator = creator;
		return this;
	}

	public DocumentComposer setTitle(String title) {
		this.title = title;
		return this;
	}

	public DocumentComposer setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public DocumentComposer setKeywords(String keywords) {
		this.keywords = keywords;
		return this;
	}
}
