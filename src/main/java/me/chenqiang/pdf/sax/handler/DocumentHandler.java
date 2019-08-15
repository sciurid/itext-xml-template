package me.chenqiang.pdf.sax.handler;

import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.Document;

import me.chenqiang.pdf.common.attribute.AttributeRegistry;
import me.chenqiang.pdf.common.attribute.AttributeUtils;
import me.chenqiang.pdf.sax.TemplateContext;
import me.chenqiang.pdf.sax.composer.DocumentComposer;
import me.chenqiang.pdf.sax.handler.resource.DefaultParagraphHandler;
import me.chenqiang.pdf.sax.handler.resource.PredefinedStyleHandler;

public final class DocumentHandler extends BasicTemplateElementHandler<DocumentComposer, Document> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentHandler.class);
	protected DocumentComposer tplDoc;
	protected Consumer<DocumentComposer> postprocessor;

	public DocumentHandler(TemplateContext context, Consumer<DocumentComposer> consumer) {
		super(context, null);
		this.postprocessor = consumer;
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
		
		new ForEachHandler(this.context, this.tplDoc::append).register(elementPath);
		new IfHandler(this.context, this.tplDoc::append).register(elementPath);
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
		this.tplDoc.setId(docId);
		this.postprocessor.accept(this.tplDoc);
		
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
		
		super.onEnd(elementPath);
	}

	@Override
	public void register(ElementPath path) {
		path.addHandler("document", this);
	}	
}
