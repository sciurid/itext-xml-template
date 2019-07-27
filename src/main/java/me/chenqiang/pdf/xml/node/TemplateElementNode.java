package me.chenqiang.pdf.xml.node;

import java.util.function.Consumer;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.xml.StyleAttributeFactory;

public abstract class TemplateElementNode<T> implements ElementHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateElementNode.class);

	protected StyleAttributeFactory attrFactory;
	protected int count;
	protected Consumer<? super T> consumer;	
	
	protected TemplateElementNode(StyleAttributeFactory attrFactory, Consumer<? super T> consumer) {
		this.attrFactory = attrFactory;
		this.consumer = consumer;
		this.count = 0;
	}
	
	protected abstract T produce(ElementPath elementPath);

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {} - {}", elementPath.getPath(), this.count);		
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		this.consumer.accept(this.produce(elementPath));		
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
	}	
}
