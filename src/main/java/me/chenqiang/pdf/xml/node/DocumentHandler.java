package me.chenqiang.pdf.xml.node;


import java.util.function.Consumer;

import org.dom4j.ElementPath;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;

public class DocumentHandler extends TemplateElementHandler<DocumentComposer> {
//	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentNode.class);
	protected DocumentComposer tplDoc;		

	public DocumentHandler(AttributeRegistry attrFactory, Consumer<DocumentComposer> consumer) {
		super(attrFactory, consumer);
	}

	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplDoc = new DocumentComposer();
		elementPath.addHandler("paragraph", new ParagraphHandler(this.attrFactory, this.tplDoc));		
		elementPath.addHandler("table", new TableHandler(this.attrFactory, this.tplDoc));		
	}

	@Override
	protected DocumentComposer produce(ElementPath elementPath) {
		return this.tplDoc;
	}
	
}
