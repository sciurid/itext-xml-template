package me.chenqiang.pdf.xml.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.ElementPropertyContainer;

import me.chenqiang.pdf.composer.ComposerDirectory;
import me.chenqiang.pdf.composer.PdfElementComposer;
import me.chenqiang.pdf.configurability.DataParameterPlaceholder;
import me.chenqiang.pdf.configurability.StringParameterPlaceholder;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.AttributeUtils;
import me.chenqiang.pdf.xml.context.TemplateContext;

public abstract class BasicTemplateElementHandler<T extends PdfElementComposer<E>, E extends ElementPropertyContainer<E>>
		implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicTemplateElementHandler.class);

	protected TemplateContext context;
	protected ComposerDirectory directory;
	protected int count;
	protected Consumer<? super T> consumer;

	protected BasicTemplateElementHandler(TemplateContext context, ComposerDirectory directory,
			Consumer<? super T> consumer) {
		this.context = context;
		this.directory = directory;
		this.consumer = consumer;
		this.count = 0;
	}

	protected abstract T produce(ElementPath elementPath);

	protected abstract Map<String, BiFunction<String, String, ? extends Consumer<? super E>>> getAttributeMap();

	public abstract void register(ElementPath path);

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {} - {}", elementPath.getPath(), this.count);
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		T composer = this.produce(elementPath);
		Element current = elementPath.getCurrent();

		String composerId = current.attributeValue(AttributeRegistry.ID);
		if (composerId != null) {
			this.directory.registerIdentifiable(composerId, composer);
			if (composer instanceof StringParameterPlaceholder) {
				this.directory.registerStringPlaceholder(composerId, (StringParameterPlaceholder) composer);
			}
			if (composer instanceof DataParameterPlaceholder) {
				this.directory.registerDataPlaceholder(composerId, (DataParameterPlaceholder) composer);
			}
		}

		AttributeUtils.setComposerAttributes(current.attributes(), this.getAttributeMap(), composer);
		AttributeUtils.getCompositeAttribute(current.attributes()).setComposerAttribute(composer);

		if (this.consumer != null) {
			this.consumer.accept(composer);
		} else {
			if (!(this instanceof DocumentHandler)) {
				LOGGER.warn("No consumer of element {} - {} found.", elementPath.getPath(), this.count);
			}
		}
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
	}

	protected static final Set<String> WARNING_FREE = Set.of(AttributeRegistry.ID);

	public static <E> List<Consumer<? super E>> getModifiers(Element current,
			Map<String, BiFunction<String, String, ? extends Consumer<? super E>>> registryMap, Class<?> clazz) {
		List<Consumer<? super E>> res = new ArrayList<>(current.attributes().size());

		for (Attribute attr : current.attributes()) {
			String name = attr.getName();
			String value = attr.getValue();
			if (registryMap.containsKey(name)) {
				Consumer<? super E> modifier = registryMap.get(name).apply(name, value);
				if (modifier != null) {
					res.add(registryMap.get(name).apply(name, value));
				}
			} else {
				if (!WARNING_FREE.contains(name)) {
					LOGGER.info("Unrecognized attribute '{}' for {}", attr.getName(), clazz);
				}
			}
		}
		return res;
	}

}
