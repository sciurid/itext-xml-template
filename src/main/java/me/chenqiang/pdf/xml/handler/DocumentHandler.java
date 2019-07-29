package me.chenqiang.pdf.xml.handler;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.Document;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.TemplateContext;

public final class DocumentHandler extends BasicTemplateElementHandler<DocumentComposer, Document> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentHandler.class);
	protected DocumentComposer tplDoc;
	protected BiConsumer<String, DocumentComposer> postprocessor;

	public DocumentHandler(TemplateContext context, BiConsumer<String, DocumentComposer> postprocessor) {
		super(context, null, null);
		this.postprocessor = postprocessor;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplDoc = new DocumentComposer();		
		this.directory = this.tplDoc.getDirectory();
		elementPath.addHandler("paragraph", new ParagraphHandler(this.context, this.directory, this.tplDoc));
		elementPath.addHandler("table", new TableHandler(this.context, this.directory, this.tplDoc));
		elementPath.addHandler("image", new ImageHandler(this.context, this.directory, this.tplDoc));
		elementPath.addHandler("barcode", new BarcodeHandler(this.context, this.directory, this.tplDoc));
		elementPath.addHandler("newpage", new NewPageHandler(this.context, this.directory, this.tplDoc));
		elementPath.addHandler("watermark", new WatermarkHandler(this.context, this.tplDoc));
	}

	@Override
	protected DocumentComposer produce(ElementPath elementPath) {
		return this.tplDoc;
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		
		this.tplDoc.setPaperLayout(this.context.getAttributeRegistry().getPaperLayout(current.attributes()));		
		String docId = current.attributeValue(AttributeRegistry.ID);
		if (docId == null) {
			LOGGER.warn("Attribute 'id' is not set for document. @{}", this.count);
		}
		this.postprocessor.accept(docId, this.tplDoc);
		
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
		
		super.onEnd(elementPath);
	}

	@Override
	protected Map<String, BiFunction<String, String, ? extends Consumer<? super Document>>> getAttributeMap() {
		return this.context.getAttributeRegistry().getDocumentMap();
	}	
}
