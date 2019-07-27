package me.chenqiang.pdf.xml;

import java.util.Collections;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.ElementPropertyContainer;
import com.itextpdf.layout.element.BlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import me.chenqiang.pdf.utils.ResourceContext;

public final class AttributeRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(AttributeRegistry.class);
	protected ResourceContext context;

	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super ElementPropertyContainer<?>>>> mapElementPropertyContainer;
	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super BlockElement<?>>>> mapBlockElement;
	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super Paragraph>>> mapParagraph;
	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super Table>>> mapTable;
	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super Text>>> mapText;

	public AttributeRegistry(ResourceContext context) {
		super();
		this.context = context;
		this.mapElementPropertyContainer = new TreeMap<>();
		this.mapBlockElement = new TreeMap<>();
		this.mapParagraph = new TreeMap<>();
		this.mapText = new TreeMap<>();
		this.mapTable = new TreeMap<>();
		this.initElementPropertyContainerMap();
		this.initBlockElementMap();
		this.initParagraphMap();
		this.initTextMap();
		this.initTableMap();
	}

	public static final String FONT_FAMILY = "font-family";
	public static final String FONT_SIZE = "font-size";
	public static final String FONT_VARIANT = "font-variant";
	public static final String TEXT_ALIGN = "text-align";

	protected void initElementPropertyContainerMap() {
		this.mapElementPropertyContainer.put(FONT_FAMILY, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			if (!parser.isValid()) {
				return null;
			}
			PdfFont font = this.context.getFont(parser.getString());
			if (font == null) {
				LOGGER.error("Font-family name '{}' not registered.", value);
				return null;
			}
			return element -> element.setFont(font);
		});
		this.mapElementPropertyContainer.put(FONT_SIZE,
				AttributeRegistry.doFloatValue(ElementPropertyContainer::setFontSize));
		this.mapElementPropertyContainer.put(FONT_VARIANT, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			if (!parser.isValid()) {
				return null;
			}
			StringTokenizer st = parser.getTokens();
			Consumer<ElementPropertyContainer<?>> consumer = null;
			while (st.hasMoreTokens()) {
				String var = st.nextToken();
				Consumer<ElementPropertyContainer<?>> next = null;
				switch (var) {
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
				if(next != null) {
					consumer = consumer == null ? next : consumer.andThen(next);
				}				
			}
			return consumer;
		});
		this.mapElementPropertyContainer.put(TEXT_ALIGN, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			if (!parser.isValid()) {
				return null;
			}
			TextAlignment alignment = parser.getTextAlign();
			return alignment == null ? null : element -> element.setTextAlignment(alignment);
		});
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super ElementPropertyContainer<?>>>> getElementPropertyContainerMap() {
		return Collections.unmodifiableMap(this.mapElementPropertyContainer);
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

	public void initBlockElementMap() {
		this.mapBlockElement.putAll(this.mapElementPropertyContainer);

		this.mapBlockElement.put(KEEP_TOGETHER, doBooleanValue(BlockElement::setKeepTogether));
		this.mapBlockElement.put(KEEP_WITH_NEXT, doBooleanValue(BlockElement::setKeepWithNext));
		this.mapBlockElement.put(MARGINS, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			if (!parser.isValid()) {
				return null;
			}
			float[] fvals = parser.getFloats(4);
			return fvals.length == 0 ? null : block -> block.setMargins(fvals[0], fvals[1], fvals[2], fvals[3]);
		});
		this.mapBlockElement.put(MARGIN, doFloatValue(BlockElement::setMargin));
		this.mapBlockElement.put(MARGIN_TOP, doFloatValue(BlockElement::setMarginTop));
		this.mapBlockElement.put(MARGIN_BOTTOM, doFloatValue(BlockElement::setMarginBottom));
		this.mapBlockElement.put(MARGIN_LEFT, AttributeRegistry.doFloatValue(BlockElement::setMarginLeft));
		this.mapBlockElement.put(MARGIN_RIGIHT, doFloatValue(BlockElement::setMarginRight));
		this.mapBlockElement.put(PADDINGS, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			if (!parser.isValid()) {
				return null;
			}
			float[] fvals = parser.getFloats(4);
			return fvals.length == 0 ? null : block -> block.setPaddings(fvals[0], fvals[1], fvals[2], fvals[3]);
		});
		this.mapBlockElement.put(PADDING, doFloatValue(BlockElement::setPadding));
		this.mapBlockElement.put(PADDING_TOP, doFloatValue(BlockElement::setPaddingTop));
		this.mapBlockElement.put(PADDING_BOTTOM, doFloatValue(BlockElement::setPaddingBottom));
		this.mapBlockElement.put(PADDING_LEFT, doFloatValue(BlockElement::setPaddingLeft));
		this.mapBlockElement.put(PADDING_RIGIHT, doFloatValue(BlockElement::setPaddingRight));
		this.mapBlockElement.put(WIDTH, doUnitValue(BlockElement::setWidth));
		this.mapBlockElement.put(MIN_WIDTH, doUnitValue(BlockElement::setMinWidth));
		this.mapBlockElement.put(MAX_WIDTH, doUnitValue(BlockElement::setMaxWidth));
		this.mapBlockElement.put(HEIGHT, doUnitValue(BlockElement::setHeight));
		this.mapBlockElement.put(MIN_HEIGHT, doUnitValue(BlockElement::setMinHeight));
		this.mapBlockElement.put(MAX_HEIGHT, doUnitValue(BlockElement::setMaxHeight));
		this.mapBlockElement.put(SPACING_RATIO, doFloatValue(BlockElement::setSpacingRatio));
		this.mapBlockElement.put(VERTICAL_ALIGNMENT, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			if (!parser.isValid()) {
				return null;
			}
			VerticalAlignment va = parser.getVerticalAlignment();
			return va == null ? null : block -> block.setVerticalAlignment(va);
		});
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super BlockElement<?>>>> getBlockElementMap() {
		return Collections.unmodifiableMap(this.mapBlockElement);
	}

	public static final String FIRST_LINE_INDENT = "first-line-indent";
	public static final String FIXED_LEADING = "fixed-leading";
	public static final String MULTIPLIED_LEADING = "multiplied-leading";

	protected void initParagraphMap() {
		this.mapParagraph.putAll(this.mapBlockElement);

		this.mapParagraph.put(FIRST_LINE_INDENT, doFloatValue(Paragraph::setFirstLineIndent));
		this.mapParagraph.put(FIXED_LEADING, doFloatValue(Paragraph::setFixedLeading));
		this.mapParagraph.put(MULTIPLIED_LEADING, doFloatValue(Paragraph::setMultipliedLeading));
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super Paragraph>>> getParagraphMap() {
		return Collections.unmodifiableMap(this.mapParagraph);
	}

	public static final String WIDTHS = "widths";
	public static final String COLUMNS = "columns";

	protected void initTableMap() {
		this.mapTable.putAll(this.mapBlockElement);

	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super Table>>> getTableMap() {
		return Collections.unmodifiableMap(this.mapTable);
	}

	public static final String ROW_SPAN = "rowspan";
	public static final String COL_SPAN = "colspan";

	public static final String TEXT_RISE = "text-rise";
	public static final String TEXT_SKEW = "text-skew";
	public static final String TEXT_HORIZONTAL_SCALING = "text-horizontal-scaling";

	protected void initTextMap() {
		this.mapText.put(TEXT_RISE, doFloatValue(Text::setTextRise));
		this.mapText.put(TEXT_SKEW, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			if (!parser.isValid()) {
				return null;
			}
			float[] skew = parser.getFloats(2);
			return skew.length == 0 ? null : text -> text.setSkew(skew[0], skew[1]);
		});
		this.mapText.put(TEXT_HORIZONTAL_SCALING, doFloatValue(Text::setHorizontalScaling));
		this.mapText.putAll(this.mapElementPropertyContainer);
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super Text>>> getTextMap() {
		return Collections.unmodifiableMap(this.mapText);
	}

	@FunctionalInterface
	protected static interface FloatFunction<T> {
		public void apply(T element, float value);
	}

	protected static <T> BiFunction<String, String, Consumer<T>> doFloatValue(FloatFunction<T> function) {
		return (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			if (!parser.isValid()) {
				return null;
			}
			Float fval = parser.getFloat();
			return fval == null ? null : element -> function.apply(element, fval.floatValue());
		};
	}

	@FunctionalInterface
	protected static interface BooleanFunction<T> {
		public void apply(T element, boolean value);
	}

	protected static <T> BiFunction<String, String, Consumer<T>> doBooleanValue(BooleanFunction<T> function) {
		return (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			if (!parser.isValid()) {
				return null;
			}
			return element -> function.apply(element, parser.getBoolean());
		};
	}

	@FunctionalInterface
	protected static interface UnitValueFunction<T> {
		public void apply(T element, UnitValue value);
	}

	protected static <T> BiFunction<String, String, Consumer<T>> doUnitValue(UnitValueFunction<T> function) {
		return (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			if (!parser.isValid()) {
				return null;
			}
			UnitValue uval = parser.getUnitValue();
			return uval == null ? null : element -> function.apply(element, uval);
		};
	}
}
