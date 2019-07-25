package me.chenqiang.pdf.xml;

import java.util.StringTokenizer;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.ElementPropertyContainer;
import com.itextpdf.layout.property.TextAlignment;

import me.chenqiang.pdf.utils.ResourceContext;

public class StyleAttributeFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(StyleAttributeFactory.class);
	protected ResourceContext context;
			
	public StyleAttributeFactory(ResourceContext context) {
		super();
		this.context = context;
	}
	
	public static final String FONT_FAMILY = "font-family";
	public static final String FONT_SIZE = "font-size";
	public static final String FONT_VARIANT = "font-variant";
	public static final String TEXT_ALIGN = "text-align";
	public <T extends ElementPropertyContainer<T>> Consumer<T> getElementPropertyContainerAttribute(String attrName, String attrValue) {
		if(attrValue == null || attrValue.trim().isEmpty()) {
			LOGGER.error("Value of attribute {} is not specified.", attrName);
			return null;
		}
		switch(attrName) {
		// 字体
		case FONT_FAMILY:
			PdfFont font = context.getFont(attrValue);
			if(font == null) {
				LOGGER.error("Font-family name '{}' not registered.", attrValue);
				return null;
			}
			return element -> element.setFont(font);
		// 字号
		case FONT_SIZE:
			try {
				float fs = Float.parseFloat(attrValue);
				return element -> element.setFontSize(fs);
			}
			catch(NumberFormatException nfe) {
				LOGGER.error("Font-size not valid: {}", attrValue);
				return null;
			}
		//字体选项
		case FONT_VARIANT:
			return parseTextVariant(attrValue);			
		// 文字对齐
		case TEXT_ALIGN:
			TextAlignment alignment = parseTextAlign(attrValue);			
			return alignment == null ? null : element -> element.setTextAlignment(alignment);
		default:
			LOGGER.error("Not recognized: {}", attrName);
			return null;
		}
	}
	
	protected static TextAlignment parseTextAlign(String attrValue) {
		switch(attrValue) {
		case "center":
			return TextAlignment.CENTER;
		case "left":
			return TextAlignment.LEFT;
		case "right":
			return TextAlignment.RIGHT;
		case "justified":
			return TextAlignment.JUSTIFIED;
		case "justified-all":
			return TextAlignment.JUSTIFIED_ALL;
		default:
			LOGGER.error("Text-align not valid: {}", attrValue);
			return null;
		}
	}
	
	protected static <T extends ElementPropertyContainer<T>> Consumer<T> parseTextVariant(String attrValue) {
		StringTokenizer st = new StringTokenizer(attrValue, ", ");
		Consumer<T> consumer = null;
		while(st.hasMoreTokens()) {
			String var = st.nextToken();
			Consumer<T> next = null;
			switch(var) {
			case "bold":
				next = element -> element.setBold();
				break;
			case "italic":
				next = element -> element.setItalic();
				break;
			case "underline":
				next = element -> element.setUnderline();
				break;
			case "line-through":
			case "stroke":
				next = element -> element.setLineThrough();
				break;
			default:
				LOGGER.error("Text-variant not valid so ignored: {}", var);
			}
			consumer = consumer == null ? next : consumer.andThen(next);
		}
		return consumer;
	}
	
}
