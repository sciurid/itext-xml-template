package me.chenqiang.pdf.xml.handler;

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

import me.chenqiang.pdf.attribute.BackgroundColorAttribute;
import me.chenqiang.pdf.attribute.BorderAttribute;
import me.chenqiang.pdf.attribute.FontColorAttribute;
import me.chenqiang.pdf.composer.AttributedComposer;
import me.chenqiang.pdf.composer.ComposerDirectory;
import me.chenqiang.pdf.configurability.DataParameterPlaceholder;
import me.chenqiang.pdf.configurability.StringParameterPlaceholder;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.CompositeAttribute;
import me.chenqiang.pdf.xml.context.TemplateContext;

public abstract class BasicTemplateElementHandler<T, E> implements ElementHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicTemplateElementHandler.class);

	protected TemplateContext context;
	protected ComposerDirectory directory;
	protected int count;
	protected Consumer<? super T> consumer;	
	
	protected BasicTemplateElementHandler(TemplateContext context, ComposerDirectory directory, Consumer<? super T> consumer) {
		this.context = context;
		this.directory = directory;
		this.consumer = consumer;
		this.count = 0;
	}
	
	protected abstract T produce(ElementPath elementPath);
	protected abstract Map<String, BiFunction<String, String, ? extends Consumer<? super E>> > getAttributeMap();

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {} - {}", elementPath.getPath(), this.count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEnd(ElementPath elementPath) {
		T element = this.produce(elementPath);
		Element current = elementPath.getCurrent();
		
		String composerId = current.attributeValue(AttributeRegistry.ID);
		if(composerId != null) {
			this.directory.registerIdentifiable(composerId, element);
			if(element instanceof StringParameterPlaceholder) {
				this.directory.registerStringPlaceholder(composerId, (StringParameterPlaceholder)element);
			}
			if(element instanceof DataParameterPlaceholder) {
				this.directory.registerDataPlaceholder(composerId, (DataParameterPlaceholder)element);
			}
		}
		
		AttributeRegistry attributeRegistry = this.context.getAttributeRegistry();
		CompositeAttribute compositeAttribute = attributeRegistry.getCompositeAttribute(current.attributes());
		if(element instanceof FontColorAttribute.Acceptor) {
			compositeAttribute.applyFontColor((FontColorAttribute.Acceptor)element);
		}
		if(element instanceof BackgroundColorAttribute.Acceptor) {
			compositeAttribute.applyBackgroundColor((BackgroundColorAttribute.Acceptor)element);
		}
		if(element instanceof BorderAttribute.Acceptor) {
			compositeAttribute.applyBorder((BorderAttribute.Acceptor)element);
		}
		if(element instanceof AttributedComposer) {
			Map<String, BiFunction<String, String, ? extends Consumer<? super E>>> attributeMap = this.getAttributeMap();
			if(attributeMap != null) {
				((AttributedComposer<E>) element).setAllAttributes(getModifiers(current, attributeMap));
			}
		}
		
		if(this.consumer != null) {			
			this.consumer.accept(element);	
		}
		else {
			LOGGER.debug("No consumer of element {} - {} found.", elementPath.getPath(), this.count);
		}
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
	}
	
	public static <E> List<Consumer<? super E>> getModifiers(Element current, 
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
				LOGGER.info("Unrecognized attribute: {}", attr.getName());
			}
		}
		return res;
	}
	
}
