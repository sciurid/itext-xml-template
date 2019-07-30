package me.chenqiang.pdf.xml.context;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.geom.PageSize;

import me.chenqiang.pdf.attribute.PaperLayout;
import me.chenqiang.pdf.composer.PdfElementComposer;

public final class AttributeUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(AttributeUtils.class);
	public static final BiFunction<String, String, Consumer<Object>> DO_NOTHING = (name, value) -> {
		return item -> {};
	};	
	
	protected static final Set<String> WARNING_FREE = Set.of(
			AttributeRegistry.ID);
	
	public static <E> void setComposerAttributes(List<Attribute> xmlAttrs, 
			Map<String, BiFunction<String, String, ? extends Consumer<? super E>> > registryMap, 
			PdfElementComposer<E> composer) {		
		for(Attribute attr : xmlAttrs) {
			String name = attr.getName();
			String value = attr.getValue();
			if(registryMap.containsKey(name)) {
				Consumer<? super E> modifier = registryMap.get(name).apply(name, value);
				if(modifier != null) {
					composer.setAttribute((registryMap.get(name).apply(name, value)));
				}
			}
			else {
				if(!WARNING_FREE.contains(name)) {
					LOGGER.info("Unrecognized attribute '{}' for {}", attr.getName(), composer.getClass());
				}
			}
		}		
	}
	
	public static CompositeAttribute getCompositeAttribute(List<Attribute> attributes) {
		CompositeAttribute attribute = new CompositeAttribute();

		for (Attribute attr : attributes) {
			String attrName = attr.getName();
			String attrValue = attr.getValue();
			AttributeValueParser parser = new AttributeValueParser(attrName, attrValue);
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
				attribute.createAndGetBorder().setWidth(parser.getFloat());
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
				attribute.createAndGetTopBorder().setWidth(parser.getFloat());
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
				attribute.createAndGetRightBorder().setWidth(parser.getFloat());
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
				attribute.createAndGetBottomBorder().setWidth(parser.getFloat());
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
				attribute.createAndGetLeftBorder().setWidth(parser.getFloat());
				break;
			case AttributeRegistry.BORDER_COLOR_LEFT:
				attribute.createAndGetLeftBorder().setColor(parser.getDeviceRgb());
				break;
			case AttributeRegistry.BORDER_OPACITY_LEFT:
				attribute.createAndGetLeftBorder().setOpacity(parser.getFloat());
				break;
			}
		}
		return attribute;
	}
	
	protected static final Map<String, PageSize> STANDARD_PAGE_SIZE = Map.ofEntries(Map.entry("a4", PageSize.A4),
			Map.entry("a4r", PageSize.A4.rotate()), Map.entry("a3", PageSize.A3), Map.entry("a3r", PageSize.A3.rotate()),
			Map.entry("b5", PageSize.B5), Map.entry("b5r", PageSize.B5.rotate()));
	public static PaperLayout getPaperLayout(List<Attribute> attributes) {
		PaperLayout paper = new PaperLayout();
		for (Attribute attr : attributes) {
			String attrName = attr.getName();
			String attrValue = attr.getValue();
			AttributeValueParser parser = new AttributeValueParser(attrName, attrValue);
			switch (attrName) {
			case AttributeRegistry.PAGE_SIZE:
				PageSize ps = STANDARD_PAGE_SIZE.get(parser.getString().toLowerCase());
				if(ps == null) {
					LOGGER.warn("Page size ({}) is not predefined. Default to A4.", attrValue);
					continue;
				}
				else {
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
