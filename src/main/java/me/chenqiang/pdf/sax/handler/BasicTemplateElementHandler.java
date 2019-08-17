package me.chenqiang.pdf.sax.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.ElementPropertyContainer;

import me.chenqiang.pdf.common.attribute.AttributeNames;
import me.chenqiang.pdf.common.attribute.AttributeRegistry;
import me.chenqiang.pdf.common.attribute.AttributeUtils;
import me.chenqiang.pdf.sax.TemplateContext;
import me.chenqiang.pdf.sax.composer.component.PdfElementComposer;

public abstract class BasicTemplateElementHandler<T extends PdfElementComposer<E, T>, E extends ElementPropertyContainer<E>>
		implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicTemplateElementHandler.class);

	protected TemplateContext context;
	protected int count;
	protected Consumer<? super T> parentAppender;

	public BasicTemplateElementHandler(TemplateContext context, Consumer<? super T> parentAppender) {
		this.context = context;
		this.parentAppender = parentAppender;
		this.count = 0;
	}

	protected abstract T create(ElementPath elementPath);

	public abstract void register(ElementPath path);

	protected List<String> listIgnoredAttributes() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(AttributeNames.ID);
		list.add(AttributeNames.STYLE);
		return list;
	}

	protected Map<String, String> collectAttributes(Element current, Map<String, String> origin) {
		Map<String, String> map = origin == null ? new LinkedHashMap<>() : new LinkedHashMap<>(origin);
		Optional<String> style = Optional.ofNullable(current.attributeValue(AttributeNames.STYLE));
		style.ifPresent(id -> {
			this.context.getPredefinedStyle(id).forEach(items -> map.put(items[0], items[1]));
		});
		current.attributes().forEach(attr -> map.put(attr.getName(), attr.getValue()));
		return map;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {} - {}", elementPath.getPath(), this.count);
		this.context.beginScope();
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
		T composer = this.create(elementPath);
		if (composer == null) {
			return;
		}
		Element current = elementPath.getCurrent();

		Map<String, String> attributes = this.collectAttributes(current, null);

		List<String> ignored = this.listIgnoredAttributes();
		AttributeUtils.getCompositeAttribute(attributes, ignored::add).setComposerAttribute(composer);
		ignored.forEach(attributes::remove);

		AttributeRegistry ar = this.context.getAttributeRegistry();
		attributes.forEach((key, value) -> {
			Consumer<E> setter = ar.get(composer.getElementClass(), key, value);
			if (setter != null) {
				composer.setAttribute(setter);
			}
		});

		if (this.parentAppender != null) {
			this.parentAppender.accept(composer);
		} else {
			if (!(this instanceof DocumentHandler)) {
				LOGGER.warn("No consumer of element {} - {} found.", elementPath.getPath(), this.count);
			}
		}
		this.context.endScope();
	}
}
