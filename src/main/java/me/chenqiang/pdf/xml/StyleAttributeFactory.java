package me.chenqiang.pdf.xml;

import java.util.StringTokenizer;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.ElementPropertyContainer;
import com.itextpdf.layout.element.BlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

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
		AttributeValueParser parser = new AttributeValueParser(attrName, attrValue);
		if(!parser.isValid()) {
			return null;
		}
			
		Float fval;
		switch(attrName) {
		// 字体
		case FONT_FAMILY:
			PdfFont font = context.getFont(parser.getString());
			if(font == null) {
				LOGGER.error("Font-family name '{}' not registered.", attrValue);
				return null;
			}
			return element -> element.setFont(font);
		// 字号
		case FONT_SIZE:
			fval = parser.getFloat();
			return fval == null ? null : element -> element.setFontSize(fval.floatValue());
		//字体选项
		case FONT_VARIANT:
			return parseTextVariant(parser);			
		// 文字对齐			
		case TEXT_ALIGN:
			TextAlignment alignment = parser.getTextAlign();			
			return alignment == null ? null : element -> element.setTextAlignment(alignment);
		default:
			return null;
		}
	}
	
	public static final String KEEP_TOGETHER = "keep-together";
	public static final String KEEP_WITH_NEXT = "keep-with-next";
	public static final String MARGIN = "margin";
	public static final String MARGINS = "margins";
	public static final String MARGIN_TOP = "margin-top";
	public static final String MARGIN_BOTTOM = "margin-bottom";
	public static final String MARGIN_LEFT = "margin-left";
	public static final String MARGIN_RIGIHT = "margin-right";
	public static final String PADDING = "padding";
	public static final String PADDINGS = "paddings";
	public static final String PADDING_TOP = "padding-top";
	public static final String PADDING_BOTTOM = "padding-bottom";
	public static final String PADDING_LEFT = "padding-left";
	public static final String PADDING_RIGIHT = "padding-right";
	public static final String WIDTH = "width";
	public static final String MIN_WIDTH = "min-width";
	public static final String MAX_WIDTH = "max-width";
	public static final String HEIGHT = "height";
	public static final String MIN_HEIGHT = "min-height";
	public static final String MAX_HEIGHT = "max-height";
	public static final String ROTATION_ANGLE = "rotation-angle";	
	public static final String SPACING_RATIO = "spacing-ratio";	
	public static final String VERTICAL_ALIGNMENT = "vertical-alignment";	
	public <T extends BlockElement<T>> Consumer<T> getBlockElementAttribute(String attrName, String attrValue) {
		AttributeValueParser parser = new AttributeValueParser(attrName, attrValue);
		if(!parser.isValid()) {
			return null;
		}
			
		Float fval;
		UnitValue uval;
		switch(attrName) {
		case KEEP_TOGETHER:
			return block -> block.setKeepTogether(parser.getBoolean());
		case KEEP_WITH_NEXT:
			return block -> block.setKeepWithNext(parser.getBoolean());
		case MARGIN:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setMargin(fval.floatValue());
		case MARGIN_TOP:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setMarginTop(fval.floatValue());
		case MARGIN_BOTTOM:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setMarginBottom(fval.floatValue());
		case MARGIN_LEFT:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setMarginLeft(fval.floatValue());
		case MARGIN_RIGIHT:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setMarginRight(fval.floatValue());
		case PADDING:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setPadding(fval.floatValue());
		case PADDING_TOP:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setPaddingTop(fval.floatValue());
		case PADDING_BOTTOM:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setPaddingBottom(fval.floatValue());
		case PADDING_LEFT:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setPaddingLeft(fval.floatValue());
		case PADDING_RIGIHT:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setPaddingRight(fval.floatValue());
		case WIDTH:
			uval = parser.getUnitValue();
			return uval == null ? null : block -> block.setWidth(uval);
		case HEIGHT:
			uval = parser.getUnitValue();
			return uval == null ? null : block -> block.setHeight(uval);
		case MIN_WIDTH:
			uval = parser.getUnitValue();
			return uval == null ? null : block -> block.setMinWidth(uval);
		case MIN_HEIGHT:
			uval = parser.getUnitValue();
			return uval == null ? null : block -> block.setMinHeight(uval);
		case MAX_WIDTH:
			uval = parser.getUnitValue();
			return uval == null ? null : block -> block.setMaxWidth(uval);
		case MAX_HEIGHT:
			uval = parser.getUnitValue();
			return uval == null ? null : block -> block.setMaxHeight(uval);
		case ROTATION_ANGLE:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setRotationAngle(fval.floatValue());
		case SPACING_RATIO:
			fval = parser.getFloat();
			return fval == null ? null : block -> block.setSpacingRatio(fval.floatValue());
		case VERTICAL_ALIGNMENT:
			VerticalAlignment va = parser.getVerticalAlignment();
			return va == null ? null : block -> block.setVerticalAlignment(va);
		default:
			return null;
		}
	}
	
	public static final String FIRST_LINE_INDENT = "first-line-indent";
	public static final String FIXED_LEADING = "fixed-leading";
	public static final String MULTIPLIED_LEADING = "multiplied-leading";
	public Consumer<Paragraph> getParagraphAttribute(String attrName, String attrValue) {
		AttributeValueParser parser = new AttributeValueParser(attrName, attrValue);
		if(!parser.isValid()) {
			return null;
		}
			
		Float fval;
		switch(attrName) {
		// 首行缩进
		case FIRST_LINE_INDENT:
			fval = parser.getFloat();
			return fval == null ? null : para -> para.setFirstLineIndent(fval.floatValue());
		// 固定行距
		case FIXED_LEADING:
			fval = parser.getFloat();
			return fval == null ? null : para -> para.setFixedLeading(fval.floatValue());
		// 倍数行距
		case MULTIPLIED_LEADING:
			fval = parser.getFloat();
			return fval == null ? null : para -> para.setMultipliedLeading(fval.floatValue());
		default:
			return null;
		}
	}
	
	public static final String TEXT_RISE = "text-rise";
	public static final String TEXT_SKEW = "text-skew";
	public static final String TEXT_HORIZONTAL_SCALING = "text-horizontal-scaling";
	public Consumer<Text> getTextAttribute(String attrName, String attrValue) {
		AttributeValueParser parser = new AttributeValueParser(attrName, attrValue);
		if(!parser.isValid()) {
			return null;
		}
			
		Float fval;
		switch(attrName) {
		// 文字抬升
		case TEXT_RISE:
			fval = parser.getFloat();
			return fval == null ? null : text -> text.setTextRise(fval.floatValue());
		// 文字倾斜
		case TEXT_SKEW:
			float [] skew = parser.getFloats(2);
			return skew.length == 0 ? null : text -> text.setSkew(skew[0], skew[1]);
		// 横向比例
		case TEXT_HORIZONTAL_SCALING:
			fval = parser.getFloat();
			return fval == null ? null : text -> text.setHorizontalScaling(fval.floatValue());		
		default:
			LOGGER.error("Not recognized attribute: {}", attrName);
			return null;
		}
	}
	
	
	protected static <T extends ElementPropertyContainer<T>> Consumer<T> parseTextVariant(AttributeValueParser parser) {
		StringTokenizer st = parser.getTokens();
		Consumer<T> consumer = null;
		while(st.hasMoreTokens()) {
			String var = st.nextToken();
			Consumer<T> next = null;
			switch(var) {
			case "bold":
				next = ElementPropertyContainer::setBold;
				break;
			case "italic":
				next = ElementPropertyContainer::setItalic;
				break;
			case "underline":
				next = ElementPropertyContainer::setUnderline;
				break;
			case "line-through":
			case "stroke":
				next = ElementPropertyContainer::setLineThrough;
				break;
			default:
				LOGGER.error("Text-variant not valid so ignored: {}", var);
			}
			consumer = consumer == null ? next : consumer.andThen(next);
		}
		return consumer;
	}
	
}
