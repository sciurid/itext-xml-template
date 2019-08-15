package me.chenqiang.pdf.common.attribute;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.ElementPropertyContainer;
import com.itextpdf.layout.element.BlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import me.chenqiang.pdf.font.FontRegistryEntry;

public final class AttributeRegistry implements AttributeNames{
	private static final Logger LOGGER = LoggerFactory.getLogger(AttributeRegistry.class);

	@SuppressWarnings("rawtypes")
	protected static final StyleAttributeRegistryEntry<ElementPropertyContainer> EPC_SA;
	static {
		EPC_SA = new StyleAttributeRegistryEntry<>(ElementPropertyContainer.class);
		EPC_SA.registerFloat(FONT_SIZE, ElementPropertyContainer::setFontSize);
		EPC_SA.registerColor(BACKGROUND_COLOR, ElementPropertyContainer::setBackgroundColor);
		EPC_SA.register(TEXT_ALIGN, (name, value) -> {
			TextAlignment alignment = new AttributeValueParser(name, value).getTextAlign();
			return alignment == null ? null : element -> element.setTextAlignment(alignment);
		});
		EPC_SA.register(HORIZONTAL_ALIGNMENT, (name, value) -> {
			HorizontalAlignment alignment = new AttributeValueParser(name, value).getHorizontalAlignment();
			return alignment == null ? null : element -> element.setHorizontalAlignment(alignment);
		});
		EPC_SA.register(FONT_VARIANT, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			Iterator<String> iter = parser.iterator();
			@SuppressWarnings("rawtypes")
			Consumer<ElementPropertyContainer> consumer = null;
			while (iter.hasNext()) {
				String var = iter.next().trim();
				@SuppressWarnings("rawtypes")
				Consumer<ElementPropertyContainer> next = null;
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
	}
	
	@SuppressWarnings("rawtypes")
	protected StyleAttributeRegistryEntry<ElementPropertyContainer> createLocalEpcsa() {
		StyleAttributeRegistryEntry<ElementPropertyContainer> epcsa = new StyleAttributeRegistryEntry<>(EPC_SA);
		epcsa.register(FONT_FAMILY, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			FontRegistryEntry font = this.fontMapper.apply(parser.getString());
			if (font == null) {
				LOGGER.error("Font-family name '{}' not registered.", value);
				return null;
			}
			return element -> element.setFont(font.getFont());
		});
		return epcsa;
	}
	

	@SuppressWarnings("rawtypes")
	protected static final StyleAttributeRegistryEntry<BlockElement> BLOCK_SA;
	static {
		BLOCK_SA = new StyleAttributeRegistryEntry<>(BlockElement.class);
		BLOCK_SA.registerBoolean(KEEP_TOGETHER, BlockElement::setKeepTogether);
		BLOCK_SA.registerBoolean(KEEP_WITH_NEXT, BlockElement::setKeepWithNext);
		BLOCK_SA.registerQuadFloat(MARGINS, BlockElement::setMargins);
		BLOCK_SA.registerFloat(MARGIN, BlockElement::setMargin);
		BLOCK_SA.registerFloat(MARGIN_TOP, BlockElement::setMarginTop);
		BLOCK_SA.registerFloat(MARGIN_BOTTOM, BlockElement::setMarginBottom);
		BLOCK_SA.registerFloat(MARGIN_LEFT, BlockElement::setMarginLeft);
		BLOCK_SA.registerFloat(MARGIN_RIGHT, BlockElement::setMarginRight);
		BLOCK_SA.registerQuadFloat(PADDINGS, BlockElement::setPaddings);
		BLOCK_SA.registerFloat(PADDING, BlockElement::setPadding);
		BLOCK_SA.registerFloat(PADDING_TOP, BlockElement::setPaddingTop);
		BLOCK_SA.registerFloat(PADDING_BOTTOM, BlockElement::setPaddingBottom);
		BLOCK_SA.registerFloat(PADDING_LEFT, BlockElement::setPaddingLeft);
		BLOCK_SA.registerFloat(PADDING_RIGIHT, BlockElement::setPaddingRight);
		BLOCK_SA.registerUnitValue(WIDTH, BlockElement::setWidth);
		BLOCK_SA.registerUnitValue(MIN_WIDTH, BlockElement::setMinWidth);
		BLOCK_SA.registerUnitValue(MAX_WIDTH, BlockElement::setMaxWidth);
		BLOCK_SA.registerUnitValue(HEIGHT, BlockElement::setHeight);
		BLOCK_SA.registerUnitValue(MIN_HEIGHT, BlockElement::setMinHeight);
		BLOCK_SA.registerUnitValue(MAX_HEIGHT, BlockElement::setMaxHeight);
		BLOCK_SA.registerFloat(ROTATION, BlockElement::setRotationAngle);
		BLOCK_SA.registerFloat(SPACING_RATIO, BlockElement::setSpacingRatio);
		BLOCK_SA.register(VERTICAL_ALIGNMENT, (name, value) -> {
			AttributeValueParser parser = new AttributeValueParser(name, value);
			VerticalAlignment va = parser.getVerticalAlignment();
			return va == null ? null : block -> block.setVerticalAlignment(va);
		});
	}

	
	protected static final StyleAttributeRegistryEntry<Image> IMAGE_SA;
	static {
		IMAGE_SA = new StyleAttributeRegistryEntry<>(Image.class);
		IMAGE_SA.registerUnitValue(WIDTH, Image::setWidth);
		IMAGE_SA.registerUnitValue(MIN_WIDTH, Image::setMinWidth);
		IMAGE_SA.registerUnitValue(MAX_WIDTH, Image::setMaxWidth);
		IMAGE_SA.registerUnitValue(HEIGHT, Image::setHeight);
		IMAGE_SA.registerUnitValue(MIN_HEIGHT, Image::setMinHeight);
		IMAGE_SA.registerUnitValue(MAX_HEIGHT, Image::setMaxHeight);

		IMAGE_SA.registerQuadFloat(MARGINS, Image::setMargins);
		IMAGE_SA.registerFloat(MARGIN_TOP, Image::setMarginTop);
		IMAGE_SA.registerFloat(MARGIN_BOTTOM, Image::setMarginBottom);
		IMAGE_SA.registerFloat(MARGIN_LEFT, Image::setMarginLeft);
		IMAGE_SA.registerFloat(MARGIN_RIGHT, Image::setMarginRight);
		IMAGE_SA.registerQuadFloat(PADDINGS, Image::setPaddings);
		IMAGE_SA.registerFloat(PADDING, Image::setPadding);
		IMAGE_SA.registerFloat(PADDING_TOP, Image::setPaddingTop);
		IMAGE_SA.registerFloat(PADDING_BOTTOM, Image::setPaddingBottom);
		IMAGE_SA.registerFloat(PADDING_LEFT, Image::setPaddingLeft);
		IMAGE_SA.registerFloat(PADDING_RIGIHT, Image::setPaddingRight);

		IMAGE_SA.registerBiFloat(SCALE, Image::scale);
		IMAGE_SA.registerBiFloat(SCALE_ABSOLUTE, Image::scaleAbsolute);
		IMAGE_SA.registerBiFloat(SCALE_TO_FIT, Image::scaleToFit);
		IMAGE_SA.registerBoolean(AUTO_SCALE, Image::setAutoScale);
		IMAGE_SA.registerBoolean(AUTO_SCALE_HEIGHT, Image::setAutoScaleHeight);
		IMAGE_SA.registerBoolean(AUTO_SCALE_WIDTH, Image::setAutoScaleWidth);
	}
	
	
	protected static final StyleAttributeRegistryEntry<Paragraph> PARA_SA;
	static {
		PARA_SA = new StyleAttributeRegistryEntry<>(Paragraph.class);
		PARA_SA.registerFloat(FIRST_LINE_INDENT, (Paragraph::setFirstLineIndent));
		PARA_SA.registerFloat(FIXED_LEADING, (Paragraph::setFixedLeading));
		PARA_SA.registerFloat(MULTIPLIED_LEADING, (Paragraph::setMultipliedLeading));
	}
	
	protected static final StyleAttributeRegistryEntry<Text> TEXT_SA;
	static {
		TEXT_SA = new StyleAttributeRegistryEntry<>(Text.class);
		TEXT_SA.registerFloat(TEXT_RISE, (Text::setTextRise));
		TEXT_SA.registerBiFloat(TEXT_SKEW, (Text::setSkew));
		TEXT_SA.registerFloat(TEXT_HORIZONTAL_SCALING, (Text::setHorizontalScaling));		
	}
	
	protected Function<String, FontRegistryEntry> fontMapper;
	protected List<StyleAttributeRegistryEntry<?>> entries;

	public AttributeRegistry(Function<String, FontRegistryEntry> fontMapper) {
		this.fontMapper = fontMapper;
		this.entries = List.of(this.createLocalEpcsa(), BLOCK_SA, PARA_SA, IMAGE_SA, TEXT_SA);
	}
	
	public <T> Consumer<T> get(Class<T> target, String name, String value) {
		for(StyleAttributeRegistryEntry<?> entry : entries) {
			if(entry.isAcceptable(target)) {
				@SuppressWarnings("unchecked")
				Consumer<T> consumer = (Consumer<T>)entry.getAttribute(name, value);
				if(consumer != null) {
					return consumer;
				}
			}
		}
		LOGGER.warn("Attribute '[{}={}]' is not assignable to {}.", name, value, target);
		return null;
	}
}
