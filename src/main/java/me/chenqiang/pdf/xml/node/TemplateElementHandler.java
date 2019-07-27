package me.chenqiang.pdf.xml.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.xml.AttributeRegistry;

public abstract class TemplateElementHandler<T> implements ElementHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateElementHandler.class);

	protected AttributeRegistry attrFactory;
	protected int count;
	protected Consumer<? super T> consumer;	
	
	protected TemplateElementHandler(AttributeRegistry attrFactory, Consumer<? super T> consumer) {
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
	
	protected static <E> List<Consumer<? super E>> getModifiers(Element current, 
			Map<String, BiFunction<String, String, ? extends Consumer<? super E>> > registryMap) {
		List<Consumer<? super E>> res = new ArrayList<>(current.attributes().size());
		for(Attribute attr : current.attributes()) {
			String name = attr.getName();
			String value = attr.getValue();
			if(registryMap.containsKey(name)) {
				Consumer<? super E> modifier = registryMap.get(name).apply(name, value);
				if(modifier != null) {
					res.add(registryMap.get(name).apply(name, value));
				}
			}
			else {
				LOGGER.error("Not recognized attribute: {}", attr.getName());
			}
		}
		return res;
	}
	
}
