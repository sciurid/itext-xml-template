package me.chenqiang.pdf.common.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dom4j.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.ElementPropertyContainer;

import me.chenqiang.pdf.component.PdfElementComposer;

public final class AttributeUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(AttributeUtils.class);
	public static final BiFunction<String, String, Consumer<Object>> DO_NOTHING = (name, value) -> {
		return item -> {
		};
	};

	protected static final Set<String> WARNING_FREE;
	static {
		WARNING_FREE = Set.of(AttributeRegistry.ID, AttributeRegistry.FONT_COLOR, AttributeRegistry.FONT_OPACITY,
				AttributeRegistry.BACKGROUND_COLOR, AttributeRegistry.BACKGROUND_OPACITY, AttributeRegistry.BORDER_TYPE,
				AttributeRegistry.BORDER_WIDTH, AttributeRegistry.BORDER_COLOR, AttributeRegistry.BORDER_OPACITY,
				AttributeRegistry.BORDER_TYPE_TOP, AttributeRegistry.BORDER_WIDTH_TOP,
				AttributeRegistry.BORDER_COLOR_TOP, AttributeRegistry.BORDER_OPACITY_TOP,
				AttributeRegistry.BORDER_TYPE_RIGHT, AttributeRegistry.BORDER_WIDTH_RIGHT,
				AttributeRegistry.BORDER_COLOR_RIGHT, AttributeRegistry.BORDER_OPACITY_RIGHT,
				AttributeRegistry.BORDER_TYPE_BOTTOM, AttributeRegistry.BORDER_WIDTH_BOTTOM,
				AttributeRegistry.BORDER_COLOR_BOTTOM, AttributeRegistry.BORDER_OPACITY_BOTTOM,
				AttributeRegistry.BORDER_TYPE_LEFT, AttributeRegistry.BORDER_WIDTH_LEFT,
				AttributeRegistry.BORDER_COLOR_LEFT, AttributeRegistry.BORDER_OPACITY_LEFT,
				AttributeRegistry.WIDTHS, AttributeRegistry.COLUMNS, AttributeRegistry.FORMAT, AttributeRegistry.STYLE);
	}

	public static <E extends ElementPropertyContainer<E>, S extends PdfElementComposer<E, S>> void setComposerAttributes(
			Iterable<String[]> attrs,
			Map<String, BiFunction<String, String, ? extends Consumer<? super E>>> registryMap,
			PdfElementComposer<E, S> composer) {
		AttributeUtils.<E, String[]>assignAttributes(attrs, arr -> arr[0], arr -> arr[1], registryMap,
				composer::setAttribute, composer.getClass());
	}

	public static <E> List<Consumer<? super E>> getComposerAttributes(List<Attribute> xmlAttrs,
			Map<String, BiFunction<String, String, ? extends Consumer<? super E>>> registryMap, Class<?> clazz) {
		List<Consumer<? super E>> list = new ArrayList<>();
		assignAttributes(xmlAttrs, registryMap, list::add, clazz);
		return list;
	}

	protected static <E> void assignAttributes(List<Attribute> xmlAttrs,
			Map<String, BiFunction<String, String, ? extends Consumer<? super E>>> registryMap,
			Consumer<Consumer<? super E>> collector, Class<?> clazz) {
		assignAttributes(xmlAttrs, Attribute::getName, Attribute::getValue, registryMap, collector, clazz);
	}

	protected static <E, A> void assignAttributes(Iterable<A> attrs, Function<A, String> nameGetter,
			Function<A, String> valueGetter,
			Map<String, BiFunction<String, String, ? extends Consumer<? super E>>> registryMap,
			Consumer<Consumer<? super E>> collector, Class<?> clazz) {
		for (A attr : attrs) {
			String name = nameGetter.apply(attr);
			String value = valueGetter.apply(attr);
			if (registryMap.containsKey(name)) {
				Consumer<? super E> modifier = registryMap.get(name).apply(name, value);
				if (modifier != null) {
					collector.accept(registryMap.get(name).apply(name, value));
				}
			} else {
				if (!WARNING_FREE.contains(name)) {
					LOGGER.info("Unrecognized attribute '{}' for {}", name, clazz);
				}
			}
		}
	}

	public static CompositeAttribute getCompositeAttribute(List<Attribute> attributes, Consumer<String> remover) {
		return getCompositeAttribute(attributes, Attribute::getName, Attribute::getValue, remover);
	}

	public static CompositeAttribute getCompositeAttribute(Iterable<String[]> attributes, Consumer<String> remover) {
		return getCompositeAttribute(attributes, arr -> arr[0], arr -> arr[1], remover);
	}
	
	public static CompositeAttribute getCompositeAttribute(Map<String, String> attributes, Consumer<String> remover) {
		return getCompositeAttribute(attributes.entrySet(), Map.Entry::getKey, Map.Entry::getValue, remover);
	}


	public static <A> CompositeAttribute getCompositeAttribute(Iterable<A> attributes, Function<A, String> nameGetter,
			Function<A, String> valueGetter, Consumer<String> remover) {
		CompositeAttribute attribute = new CompositeAttribute();
		for (A attr : attributes) {
			String attrName = nameGetter.apply(attr);
			String attrValue = valueGetter.apply(attr);
			AttributeValueParser parser = new AttributeValueParser(attrName, attrValue);
			
			boolean remove = true;
			switch (attrName) {
			case AttributeRegistry.FONT_COLOR:
				attribute.createAndGetFontColor().setFontColor(parser.getDeviceRgb());
				break;
			case AttributeRegistry.FONT_OPACITY:
				attribute.createAndGetFontColor().setOpacity(parser.getFloat());
				break;
			case AttributeRegistry.BACKGROUND_COLOR:
				attribute.createAndGetBackgroundColor().setFontColor(parser.getDeviceRgb());
				break;
			case AttributeRegistry.BACKGROUND_OPACITY:
				attribute.createAndGetBackgroundColor().setOpacity(parser.getFloat());
				break;

			case AttributeRegistry.BORDER_TYPE:
				attribute.createAndGetBorder().setType(parser.getString());
				break;
			case AttributeRegistry.BORDER_WIDTH:
				attribute.createAndGetBorder().setWidth(parser.getLength());
				break;
			case AttributeRegistry.BORDER_COLOR:
				attribute.createAndGetBorder().setColor(parser.getDeviceRgb());
				break;
			case AttributeRegistry.BORDER_OPACITY:
				attribute.createAndGetBorder().setOpacity(parser.getFloat());
				break;

			case AttributeRegistry.BORDER_TYPE_TOP:
				attribute.createAndGetTopBorder().setType(parser.getString());
				break;
			case AttributeRegistry.BORDER_WIDTH_TOP:
				attribute.createAndGetTopBorder().setWidth(parser.getLength());
				break;
			case AttributeRegistry.BORDER_COLOR_TOP:
				attribute.createAndGetTopBorder().setColor(parser.getDeviceRgb());
				break;
			case AttributeRegistry.BORDER_OPACITY_TOP:
				attribute.createAndGetTopBorder().setOpacity(parser.getFloat());
				break;

			case AttributeRegistry.BORDER_TYPE_RIGHT:
				attribute.createAndGetRightBorder().setType(parser.getString());
				break;
			case AttributeRegistry.BORDER_WIDTH_RIGHT:
				attribute.createAndGetRightBorder().setWidth(parser.getLength());
				break;
			case AttributeRegistry.BORDER_COLOR_RIGHT:
				attribute.createAndGetRightBorder().setColor(parser.getDeviceRgb());
				break;
			case AttributeRegistry.BORDER_OPACITY_RIGHT:
				attribute.createAndGetRightBorder().setOpacity(parser.getFloat());
				break;

			case AttributeRegistry.BORDER_TYPE_BOTTOM:
				attribute.createAndGetBottomBorder().setType(parser.getString());
				break;
			case AttributeRegistry.BORDER_WIDTH_BOTTOM:
				attribute.createAndGetBottomBorder().setWidth(parser.getLength());
				break;
			case AttributeRegistry.BORDER_COLOR_BOTTOM:
				attribute.createAndGetBottomBorder().setColor(parser.getDeviceRgb());
				break;
			case AttributeRegistry.BORDER_OPACITY_BOTTOM:
				attribute.createAndGetBottomBorder().setOpacity(parser.getFloat());
				break;

			case AttributeRegistry.BORDER_TYPE_LEFT:
				attribute.createAndGetLeftBorder().setType(parser.getString());
				break;
			case AttributeRegistry.BORDER_WIDTH_LEFT:
				attribute.createAndGetLeftBorder().setWidth(parser.getLength());
				break;
			case AttributeRegistry.BORDER_COLOR_LEFT:
				attribute.createAndGetLeftBorder().setColor(parser.getDeviceRgb());
				break;
			case AttributeRegistry.BORDER_OPACITY_LEFT:
				attribute.createAndGetLeftBorder().setOpacity(parser.getFloat());
				break;
			default:
				remove = false;
			}
			if(remove && remover != null) {
				remover.accept(attrName);
			}
		}
		return attribute;
	}

	protected static final Map<String, PageSize> STANDARD_PAGE_SIZE = Map.ofEntries(Map.entry("a4", PageSize.A4),
			Map.entry("a4r", PageSize.A4.rotate()), Map.entry("a3", PageSize.A3),
			Map.entry("a3r", PageSize.A3.rotate()), Map.entry("b5", PageSize.B5),
			Map.entry("b5r", PageSize.B5.rotate()));

	public static PaperLayout getPaperLayout(List<Attribute> attributes) {
		PaperLayout paper = new PaperLayout();
		for (Attribute attr : attributes) {
			String attrName = attr.getName();
			String attrValue = attr.getValue();
			AttributeValueParser parser = new AttributeValueParser(attrName, attrValue);
			switch (attrName) {
			case AttributeRegistry.PAGE_SIZE:
				PageSize ps = STANDARD_PAGE_SIZE.get(parser.getString().toLowerCase());
				if (ps == null) {
					LOGGER.warn("Page size ({}) is not predefined. Default to A4.", attrValue);
					continue;
				} else {
					paper.setPs(ps);
				}
				break;
			case AttributeRegistry.MARGIN:
				parser.setLength(paper::setMargin);
				break;
			case AttributeRegistry.MARGIN_LEFT:
				parser.setLength(paper::setMarginLeft);
				break;
			case AttributeRegistry.MARGIN_RIGHT:
				parser.setLength(paper::setMarginRight);
				break;
			case AttributeRegistry.MARGIN_TOP:
				parser.setLength(paper::setMarginTop);
				break;
			case AttributeRegistry.MARGIN_BOTTOM:
				parser.setLength(paper::setMarginBottom);
				break;
			}
		}
		return paper;
	}
}
