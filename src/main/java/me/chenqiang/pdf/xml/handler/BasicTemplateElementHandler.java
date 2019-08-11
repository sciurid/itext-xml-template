package me.chenqiang.pdf.xml.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.ElementPropertyContainer;

import me.chenqiang.pdf.component.PdfElementComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.AttributeUtils;
import me.chenqiang.pdf.xml.context.TemplateContext;

public abstract class BasicTemplateElementHandler<T extends PdfElementComposer<E, T>, E extends ElementPropertyContainer<E>>
		implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicTemplateElementHandler.class);

	protected TemplateContext context;
	protected int count;
	protected Consumer<? super T> consumer;

	protected BasicTemplateElementHandler(TemplateContext context, Consumer<? super T> consumer) {
		this.context = context;
		this.consumer = consumer;
		this.count = 0;
	}

	protected abstract T create(ElementPath elementPath);

	protected abstract Map<String, BiFunction<String, String, ? extends Consumer<? super E>>> getAttributeMap();

	public abstract void register(ElementPath path);

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {} - {}", elementPath.getPath(), this.count);
		this.context.beginScope();
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
		T composer = this.create(elementPath);
		if(composer == null) {
			return;
		}
		Element current = elementPath.getCurrent();

		String composerId = current.attributeValue(AttributeRegistry.ID);
		if (composerId != null) {
			composer.setId(composerId);
		}

		String styleId = current.attributeValue(AttributeRegistry.STYLE);
		List<String []> attributes = new ArrayList<>();
		if(styleId != null){
			List<String []> styleAttributes = this.context.getPredefinedStyle(styleId);
			if(styleAttributes != null) {
				attributes.addAll(styleAttributes);
//				List<String[]> filtered = styleAttributes.stream().filter(attr -> attributeMap.containsKey(attr[0])).collect(Collectors.toList());
//				AttributeUtils.setComposerAttributes(styleAttributes, cellMap, this.row);
//				AttributeUtils.getCompositeAttribute(styleAttributes).setComposerAttribute(this.row);
			}
		}
		current.attributes().forEach(attr -> attributes.add(new String[] {attr.getName(), attr.getValue()}));
		
		Map<String, BiFunction<String, String, ? extends Consumer<? super E>>> attributeMap = this.getAttributeMap();
		AttributeUtils.setComposerAttributes(attributes, attributeMap, composer);
		AttributeUtils.getCompositeAttribute(attributes).setComposerAttribute(composer);
		
		if (this.consumer != null) {
			this.consumer.accept(composer);
		} else {
			if (!(this instanceof DocumentHandler)) {
				LOGGER.warn("No consumer of element {} - {} found.", elementPath.getPath(), this.count);
			}
		}
		this.context.endScope();
	}
}
