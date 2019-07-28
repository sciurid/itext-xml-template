package me.chenqiang.pdf.xml.handler;

import java.util.function.BiConsumer;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;
import me.chenqiang.pdf.xml.TemplateContext;

public final class DocumentHandler extends BasicTemplateElementHandler<DocumentComposer> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentHandler.class);
	protected DocumentComposer tplDoc;
	protected BiConsumer<String, DocumentComposer> postprocessor;

	public DocumentHandler(TemplateContext context, BiConsumer<String, DocumentComposer> postprocessor) {
		super(context, null);
		this.postprocessor = postprocessor;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplDoc = new DocumentComposer();
		elementPath.addHandler("paragraph", new ParagraphHandler(this.context, this.tplDoc));
		elementPath.addHandler("table", new TableHandler(this.context, this.tplDoc));
		elementPath.addHandler("image", new ImageHandler(this.context, this.tplDoc));
		elementPath.addHandler("barcode", new BarcodeHandler(this.context, this.tplDoc));
		elementPath.addHandler("newpage", new NewPageHandler(this.context, this.tplDoc));
		elementPath.addHandler("watermark", new WatermarkHandler(this.context, this.tplDoc));
	}

	@Override
	protected DocumentComposer produce(ElementPath elementPath) {
		return this.tplDoc;
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		String docId = current.attributeValue(AttributeRegistry.ID);
		if (docId == null) {
			LOGGER.error("Attribute 'id' is not set for document. @{}", this.count);
		}
		else {
			this.postprocessor.accept(docId, this.tplDoc);
		}
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
	}
}
