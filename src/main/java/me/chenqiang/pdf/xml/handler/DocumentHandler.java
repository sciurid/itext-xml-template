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
import me.chenqiang.pdf.xml.context.AttributeUtils;
import me.chenqiang.pdf.xml.context.TemplateContext;
import me.chenqiang.pdf.xml.handler.resource.DefaultParagraphHandler;
import me.chenqiang.pdf.xml.handler.resource.PredefinedStyleHandler;

public final class DocumentHandler extends BasicTemplateElementHandler<DocumentComposer, Document> {
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
		
		new DefaultParagraphHandler(this.context).register(elementPath);
		new PredefinedStyleHandler(this.context).register(elementPath);
		
		new ParagraphHandler(this.context, this.tplDoc).register(elementPath);
		new TableHandler(this.context, this.tplDoc).register(elementPath);
		new ImageHandler(this.context, this.tplDoc).register(elementPath);
		new BarcodeHandler(this.context, this.tplDoc).register(elementPath);
		new DivHandler(this.context, this.tplDoc).register(elementPath);
		new NewPageHandler(this.context, this.tplDoc).register(elementPath);
		new WatermarkHandler(this.context, this.tplDoc).register(elementPath);
	}

	@Override
	protected DocumentComposer create(ElementPath elementPath) {
		return this.tplDoc;
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		
		this.tplDoc.setPaperLayout(AttributeUtils.getPaperLayout(current.attributes()));		
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

	@Override
	public void register(ElementPath path) {
		path.addHandler("document", this);
	}	
	
	
}
