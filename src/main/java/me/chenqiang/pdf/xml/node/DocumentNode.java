package me.chenqiang.pdf.xml.node;


import java.util.function.Consumer;

import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.template.DocumentTemplate;
import me.chenqiang.pdf.xml.StyleAttributeFactory;

public class DocumentNode extends TemplateElementNode<DocumentTemplate> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentNode.class);
	protected DocumentTemplate tplDoc;		

	public DocumentNode(StyleAttributeFactory attrFactory, Consumer<DocumentTemplate> consumer) {
		super(attrFactory, consumer);
	}

	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplDoc = new DocumentTemplate();
		elementPath.addHandler("paragraph", new ParagraphNode(this.attrFactory, this.tplDoc));		
	}

	@Override
	protected DocumentTemplate produce(ElementPath elementPath) {
		return this.tplDoc;
	}
	
}
