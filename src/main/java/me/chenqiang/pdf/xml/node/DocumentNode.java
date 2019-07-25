package me.chenqiang.pdf.xml.node;


import java.util.function.Consumer;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.template.DocumentTemplate;
import me.chenqiang.pdf.xml.StyleAttributeFactory;

public class DocumentNode implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentNode.class);
	private StyleAttributeFactory attrFactory;
	private int count;
	
	protected DocumentTemplate tplDoc;		
	protected Consumer<DocumentTemplate> consumer;

	public DocumentNode(StyleAttributeFactory attrFactory, Consumer<DocumentTemplate> consumer) {
		this.attrFactory = attrFactory;
		this.consumer = consumer;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {} - {}", elementPath.getPath(), this.count);
		this.tplDoc = new DocumentTemplate();
		elementPath.addHandler("paragraph", new ParagraphNode(this.tplDoc, this.attrFactory));		
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		this.consumer.accept(this.tplDoc);
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
	}

}
