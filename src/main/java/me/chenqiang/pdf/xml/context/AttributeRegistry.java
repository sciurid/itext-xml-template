package me.chenqiang.pdf.xml.context;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.ElementPropertyContainer;
import com.itextpdf.layout.element.BlockElement;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

public final class AttributeRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(AttributeRegistry.class);
	protected ResourceRepository context;

	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super ElementPropertyContainer<?>>>> mapElementPropertyContainer;
	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super BlockElement<?>>>> mapBlockElement;
	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super BlockElement<?>>>> mapCell;
	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super Paragraph>>> mapParagraph;
	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super Table>>> mapTable;
	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super Text>>> mapText;
	protected final Map<String, BiFunction<String, String, ? extends Consumer<? super Image>>> mapImage;

	public AttributeRegistry(ResourceRepository context) {
		super();
		this.context = context;
		this.mapElementPropertyContainer = new TreeMap<>();
		this.initElementPropertyContainerMap();
		this.mapBlockElement = new TreeMap<>();
		this.initBlockElementMap();
		this.mapCell = new TreeMap<>();
		this.initCellMap();
		this.mapParagraph = new TreeMap<>();
		this.initParagraphMap();
		this.mapTable = new TreeMap<>();
		this.initTableMap();
		this.mapText = new TreeMap<>();
		this.initTextMap();
		this.mapImage = new TreeMap<>();
		this.initImageMap();
	}

	public static final String ID = "id";
	public static final String STYLE = "style";

	public static final String FONT_FAMILY = "font-family";
	public static final String FONT_SIZE = "font-size";
	public static final String FONT_VARIANT = "font-variant";
	public static final String TEXT_ALIGN = "text-align";
	public static final String HORIZONTAL_ALIGNMENT = "horizontal-alignment";

	protected void initElementPropertyContainerMap() {
		this.mapElementPropertyContainer.put(FONT_FAMILY, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			PdfFont font = this.context.getFont(parser.getString());
			if (font == null) {
				LOGGER.error("Font-family name '{}' not registered.", value);
				return null;
			}
			return element -> element.setFont(font);
		});
		this.mapElementPropertyContainer.put(FONT_SIZE,
				AttributeRegistry.doFloat(ElementPropertyContainer::setFontSize));
		this.mapElementPropertyContainer.put(FONT_VARIANT, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			Iterator<String> iter = parser.iterator();
			Consumer<ElementPropertyContainer<?>> consumer = null;
			while (iter.hasNext()) {
				String var = iter.next().trim();
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
				if (next != null) {
					consumer = consumer == null ? next : consumer.andThen(next);
				}
			}
			return consumer;
		});
		this.mapElementPropertyContainer.put(TEXT_ALIGN, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			TextAlignment alignment = parser.getTextAlign();
			return alignment == null ? null : element -> element.setTextAlignment(alignment);
		});
		this.mapElementPropertyContainer.put(BACKGROUND_COLOR,
				doRgbColor(ElementPropertyContainer::setBackgroundColor));
		this.mapElementPropertyContainer.put(HORIZONTAL_ALIGNMENT, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			HorizontalAlignment alignment = parser.getHorizontalAlignment();
			return alignment == null ? null : element -> element.setHorizontalAlignment(alignment);
		});
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super ElementPropertyContainer<?>>>> getElementPropertyContainerMap() {
		return Collections.unmodifiableMap(this.mapElementPropertyContainer);
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super Document>>> getDocumentMap() {
		return Collections.unmodifiableMap(this.mapElementPropertyContainer);
	}

	public static final String KEEP_TOGETHER = "keep-together";
	public static final String KEEP_WITH_NEXT = "keep-with-next";
	public static final String MARGIN = "margin";
	public static final String MARGINS = "margins";
	public static final String MARGIN_TOP = "margin-top";
	public static final String MARGIN_BOTTOM = "margin-bottom";
	public static final String MARGIN_LEFT = "margin-left";
	public static final String MARGIN_RIGHT = "margin-right";
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
	public static final String ROTATION = "rotation";
	public static final String SPACING_RATIO = "spacing-ratio";
	public static final String VERTICAL_ALIGNMENT = "vertical-alignment";

	public void initBlockElementMap() {
		this.mapBlockElement.putAll(this.mapElementPropertyContainer);

		this.mapBlockElement.put(KEEP_TOGETHER, doBoolean(BlockElement::setKeepTogether));
		this.mapBlockElement.put(KEEP_WITH_NEXT, doBoolean(BlockElement::setKeepWithNext));
		this.mapBlockElement.put(MARGINS, doQuadFloat(BlockElement::setMargins));
		this.mapBlockElement.put(MARGIN, doFloat(BlockElement::setMargin));
		this.mapBlockElement.put(MARGIN_TOP, doFloat(BlockElement::setMarginTop));
		this.mapBlockElement.put(MARGIN_BOTTOM, doFloat(BlockElement::setMarginBottom));
		this.mapBlockElement.put(MARGIN_LEFT, AttributeRegistry.doFloat(BlockElement::setMarginLeft));
		this.mapBlockElement.put(MARGIN_RIGHT, doFloat(BlockElement::setMarginRight));
		this.mapBlockElement.put(PADDINGS, doQuadFloat(BlockElement::setPaddings));
		this.mapBlockElement.put(PADDING, doFloat(BlockElement::setPadding));
		this.mapBlockElement.put(PADDING_TOP, doFloat(BlockElement::setPaddingTop));
		this.mapBlockElement.put(PADDING_BOTTOM, doFloat(BlockElement::setPaddingBottom));
		this.mapBlockElement.put(PADDING_LEFT, doFloat(BlockElement::setPaddingLeft));
		this.mapBlockElement.put(PADDING_RIGIHT, doFloat(BlockElement::setPaddingRight));
		this.mapBlockElement.put(WIDTH, doUnitValue(BlockElement::setWidth));
		this.mapBlockElement.put(MIN_WIDTH, doUnitValue(BlockElement::setMinWidth));
		this.mapBlockElement.put(MAX_WIDTH, doUnitValue(BlockElement::setMaxWidth));
		this.mapBlockElement.put(HEIGHT, doUnitValue(BlockElement::setHeight));
		this.mapBlockElement.put(MIN_HEIGHT, doUnitValue(BlockElement::setMinHeight));
		this.mapBlockElement.put(MAX_HEIGHT, doUnitValue(BlockElement::setMaxHeight));
		this.mapBlockElement.put(ROTATION, doFloat(BlockElement::setRotationAngle));
		this.mapBlockElement.put(SPACING_RATIO, doFloat(BlockElement::setSpacingRatio));
		this.mapBlockElement.put(VERTICAL_ALIGNMENT, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			VerticalAlignment va = parser.getVerticalAlignment();
			return va == null ? null : block -> block.setVerticalAlignment(va);
		});
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super BlockElement<?>>>> getBlockElementMap() {
		return Collections.unmodifiableMap(this.mapBlockElement);
	}
	
	public Map<String, BiFunction<String, String, ? extends Consumer<? super Div>>> getDivMap() {
		return Collections.unmodifiableMap(this.mapBlockElement);
	}

	public static final String SCALE = "scale";
	public static final String SCALE_ABSOLUTE = "scale-absolute";
	public static final String SCALE_TO_FIT = "scale-to-fit";
	public static final String AUTO_SCALE = "auto-scale";
	public static final String AUTO_SCALE_HEIGHT = "auto-scale-height";
	public static final String AUTO_SCALE_WIDTH = "auto-scale-width";
	public static final String REF = "ref";
	public static final String FILE = "file";
	public static final String RESOURCE = "resource";
	public static final String FORMAT = "format";
	public static final String OPACITY = "opacity";
	public static final String OFFSET_X = "offset-x";
	public static final String OFFSET_Y = "offset-y";
	

	protected void initImageMap() {
		this.mapImage.putAll(this.mapElementPropertyContainer);
		this.mapImage.put(WIDTH, doUnitValue(Image::setWidth));
		this.mapImage.put(MIN_WIDTH, doUnitValue(Image::setMinWidth));
		this.mapImage.put(MAX_WIDTH, doUnitValue(Image::setMaxWidth));
		this.mapImage.put(HEIGHT, doUnitValue(Image::setHeight));
		this.mapImage.put(MIN_HEIGHT, doUnitValue(Image::setMinHeight));
		this.mapImage.put(MAX_HEIGHT, doUnitValue(Image::setMaxHeight));

		this.mapImage.put(MARGINS, doQuadFloat(Image::setMargins));
		this.mapImage.put(MARGIN_TOP, doFloat(Image::setMarginTop));
		this.mapImage.put(MARGIN_BOTTOM, doFloat(Image::setMarginBottom));
		this.mapImage.put(MARGIN_LEFT, AttributeRegistry.doFloat(Image::setMarginLeft));
		this.mapImage.put(MARGIN_RIGHT, doFloat(Image::setMarginRight));
		this.mapImage.put(PADDINGS, doQuadFloat(Image::setPaddings));
		this.mapImage.put(PADDING, doFloat(Image::setPadding));
		this.mapImage.put(PADDING_TOP, doFloat(Image::setPaddingTop));
		this.mapImage.put(PADDING_BOTTOM, doFloat(Image::setPaddingBottom));
		this.mapImage.put(PADDING_LEFT, doFloat(Image::setPaddingLeft));
		this.mapImage.put(PADDING_RIGIHT, doFloat(Image::setPaddingRight));

		this.mapImage.put(SCALE, doBiFloat(Image::scale));
		this.mapImage.put(SCALE_ABSOLUTE, doBiFloat(Image::scaleAbsolute));
		this.mapImage.put(SCALE_TO_FIT, doBiFloat(Image::scaleToFit));
		this.mapImage.put(AUTO_SCALE, doBoolean(Image::setAutoScale));
		this.mapImage.put(AUTO_SCALE_HEIGHT, doBoolean(Image::setAutoScaleHeight));
		this.mapImage.put(AUTO_SCALE_WIDTH, doBoolean(Image::setAutoScaleWidth));

		this.mapImage.put(REF, AttributeUtils.DO_NOTHING);
		this.mapImage.put(FILE, AttributeUtils.DO_NOTHING);
		this.mapImage.put(RESOURCE, AttributeUtils.DO_NOTHING);
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super Image>>> getImageMap() {
		return Collections.unmodifiableMap(this.mapImage);
	}

	public static final String FIRST_LINE_INDENT = "first-line-indent";
	public static final String FIXED_LEADING = "fixed-leading";
	public static final String MULTIPLIED_LEADING = "multiplied-leading";

	protected void initParagraphMap() {
		this.mapParagraph.putAll(this.mapBlockElement);

		this.mapParagraph.put(FIRST_LINE_INDENT, doFloat(Paragraph::setFirstLineIndent));
		this.mapParagraph.put(FIXED_LEADING, doFloat(Paragraph::setFixedLeading));
		this.mapParagraph.put(MULTIPLIED_LEADING, doFloat(Paragraph::setMultipliedLeading));
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super Paragraph>>> getParagraphMap() {
		return Collections.unmodifiableMap(this.mapParagraph);
	}

	public static final String WIDTHS = "widths";
	public static final String COLUMNS = "columns";

	protected void initTableMap() {
		this.mapTable.putAll(this.mapBlockElement);
		this.mapCell.put(WIDTHS, AttributeUtils.DO_NOTHING);
		this.mapCell.put(COLUMNS, AttributeUtils.DO_NOTHING);
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super Table>>> getTableMap() {
		return Collections.unmodifiableMap(this.mapTable);
	}

	public static final String ROW_SPAN = "rowspan";
	public static final String COL_SPAN = "colspan";

	protected void initCellMap() {
		this.mapCell.putAll(this.mapBlockElement);
		this.mapCell.put(ROW_SPAN, AttributeUtils.DO_NOTHING);
		this.mapCell.put(COL_SPAN, AttributeUtils.DO_NOTHING);
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super Cell>>> getCellMap() {
		return Collections.unmodifiableMap(this.mapCell);
	}

	public static final String TEXT_RISE = "text-rise";
	public static final String TEXT_SKEW = "text-skew";
	public static final String TEXT_HORIZONTAL_SCALING = "text-horizontal-scaling";

	protected void initTextMap() {
		this.mapText.put(TEXT_RISE, doFloat(Text::setTextRise));
		this.mapText.put(TEXT_SKEW, doBiFloat(Text::setSkew));
		this.mapText.put(TEXT_HORIZONTAL_SCALING, doFloat(Text::setHorizontalScaling));
		this.mapText.putAll(this.mapElementPropertyContainer);
	}

	public Map<String, BiFunction<String, String, ? extends Consumer<? super Text>>> getTextMap() {
		return Collections.unmodifiableMap(this.mapText);
	}

	public static final String FONT_COLOR = "font-color";
	public static final String FONT_OPACITY = "font-opacity";
	public static final String BACKGROUND_COLOR = "background-color";
	public static final String BACKGROUND_OPACITY = "background-opacity";

	public static final String BORDER_TYPE = "border-type";
	public static final String BORDER_WIDTH = "border-width";
	public static final String BORDER_COLOR = "border-color";
	public static final String BORDER_OPACITY = "border-opacity";

	public static final String BORDER_TYPE_TOP = "border-type-top";
	public static final String BORDER_WIDTH_TOP = "border-width-top";
	public static final String BORDER_COLOR_TOP = "border-color-top";
	public static final String BORDER_OPACITY_TOP = "border-opacity-top";

	public static final String BORDER_TYPE_RIGHT = "border-type-right";
	public static final String BORDER_WIDTH_RIGHT = "border-width-right";
	public static final String BORDER_COLOR_RIGHT = "border-color-right";
	public static final String BORDER_OPACITY_RIGHT = "border-opacity-right";

	public static final String BORDER_TYPE_BOTTOM = "border-type-bottom";
	public static final String BORDER_WIDTH_BOTTOM = "border-width-bottom";
	public static final String BORDER_COLOR_BOTTOM = "border-color-bottom";
	public static final String BORDER_OPACITY_BOTTOM = "border-opacity-bottom";

	public static final String BORDER_TYPE_LEFT = "border-type-left";
	public static final String BORDER_WIDTH_LEFT = "border-width-left";
	public static final String BORDER_COLOR_LEFT = "border-color-left";
	public static final String BORDER_OPACITY_LEFT = "border-opacity-left";

	public static final String PAGE_SIZE = "page-size";

	@FunctionalInterface
	protected static interface FloatFunction<T> {
		public void apply(T element, float value);
	}

	protected static <T> BiFunction<String, String, Consumer<T>> doFloat(FloatFunction<T> function) {
		return (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			Float fval = parser.getLength();
			return fval == null ? null : element -> function.apply(element, fval.floatValue());
		};
	}

	@FunctionalInterface
	protected static interface BooleanFunction<T> {
		public void apply(T element, boolean value);
	}

	protected static <T> BiFunction<String, String, Consumer<T>> doBoolean(BooleanFunction<T> function) {
		return (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
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
			UnitValue uval = parser.getUnitValue();
			return uval == null ? null : element -> function.apply(element, uval);
		};
	}

	@FunctionalInterface
	protected static interface BiFloatFunction<T> {
		public void apply(T element, float val1, float val2);
	}

	protected static <T> BiFunction<String, String, Consumer<T>> doBiFloat(BiFloatFunction<T> function) {
		return (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			float[] fvals = parser.getLengthArray(2);
			return fvals.length == 0 ? null : element -> function.apply(element, fvals[0], fvals[1]);
		};
	}

	@FunctionalInterface
	protected static interface TriFloatFunction<T> {
		public void apply(T element, float val1, float val2, float val3);
	}

	protected static <T> BiFunction<String, String, Consumer<T>> doTriFloat(TriFloatFunction<T> function) {
		return (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			float[] fvals = parser.getLengthArray(3);
			return fvals.length == 0 ? null : element -> function.apply(element, fvals[0], fvals[1], fvals[2]);
		};
	}

	@FunctionalInterface
	protected static interface QuadFloatFunction<T> {
		public void apply(T element, float val1, float val2, float val3, float val4);
	}

	protected static <T> BiFunction<String, String, Consumer<T>> doQuadFloat(QuadFloatFunction<T> function) {
		return (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			float[] fvals = parser.getLengthArray(4);
			return fvals.length == 0 ? null
					: element -> function.apply(element, fvals[0], fvals[1], fvals[2], fvals[4]);
		};
	}

	@FunctionalInterface
	protected static interface ColorFunction<T> {
		public void apply(T element, Color color);
	}

	protected static <T> BiFunction<String, String, Consumer<T>> doRgbColor(ColorFunction<T> function) {
		return (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			Color color = parser.getDeviceRgb();
			return color == null ? null : element -> function.apply(element, color);
		};
	}

	protected static <T> BiFunction<String, String, Consumer<T>> doCmykColor(ColorFunction<T> function) {
		return (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			Color color = parser.getDeviceCmyk();
			return color == null ? null : element -> function.apply(element, color);
		};
	}
}
