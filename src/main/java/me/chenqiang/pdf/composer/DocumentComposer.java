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

import me.chenqiang.pdf.attribute.BackgroundColorAttribute;
import me.chenqiang.pdf.attribute.BorderAttribute;
import me.chenqiang.pdf.attribute.FontColorAttribute;
import me.chenqiang.pdf.attribute.PaperLayout;
import me.chenqiang.pdf.composer.DocumentComposer.DocumentComponent;
import me.chenqiang.pdf.configurability.DataParameterPlaceholder;
import me.chenqiang.pdf.configurability.StringParameterPlaceholder;
import me.chenqiang.pdf.configurability.StringStub;

public class DocumentComposer extends BasicElementPropertyContainerComposer<Document, DocumentComposer>
implements StringStub, Iterable<DocumentComponent>, AttributedComposer<Document>,
FontColorAttribute.Acceptor, BackgroundColorAttribute.Acceptor, BorderAttribute.Acceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentComposer.class);

	public static interface DocumentComponent {
		public void process(Document doc, PdfDocument pdf, PdfWriter writer);
	}

	protected List<DocumentComponent> components;
	protected List<Consumer<? super Document>> attributes;
	protected WatermarkMaker watermarkMaker;
	protected ComposerDirectory directory;
	protected PaperLayout paperLayout;

	public DocumentComposer() {
		this.components = new ArrayList<>();
		this.attributes = new ArrayList<>();
		this.watermarkMaker = new WatermarkMaker();
		this.directory = new ComposerDirectory();
		this.paperLayout = new PaperLayout();
	}

	public ComposerDirectory getDirectory() {
		return directory;
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

	public Document compose(PdfDocument pdf, PdfWriter writer, boolean close) {
		Document doc = new Document(pdf, this.paperLayout.getPageSize());
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
		this.components.stream().filter(comp -> comp instanceof StringStub)
				.forEach(comp -> ((StringStub) comp).substitute(params));
	}
	
	public void parameterize(Map<String, String> strs, Map<String, byte []> data) {
		if(strs != null) {
			for(Map.Entry<String, String> entry : strs.entrySet()) {
				List<StringParameterPlaceholder> placeholders = this.directory.getStringPlaceholders(entry.getKey());
				if(placeholders != null) {
					for(StringParameterPlaceholder placeholder : placeholders) {
						placeholder.setParameter(entry.getValue());
					}
				}
			}
		}
		if(data != null) {
			for(Map.Entry<String, byte []> entry : data.entrySet()) {
				List<DataParameterPlaceholder> placeholders = this.directory.getDataPlaceholders(entry.getKey());
				if(placeholders != null) {
					for(DataParameterPlaceholder placeholder : placeholders) {
						placeholder.setParameter(entry.getValue());
					}
				}
			}
		}
	}	

	@Override
	public Iterator<DocumentComponent> iterator() {
		return this.components.iterator();
	}
}
