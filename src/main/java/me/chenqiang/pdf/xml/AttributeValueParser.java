package me.chenqiang.pdf.xml;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import me.chenqiang.pdf.utils.LengthUnit;

public class AttributeValueParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(AttributeValueParser.class);
	private final String attrName;
	private final String originalValue;
	private final String attrValue;

	public AttributeValueParser(String attrName, String originalValue) {
		this.attrName = attrName;
		this.originalValue = originalValue;
		if (attrName == null) {
			throw new IllegalArgumentException("Name of attribute is not specified.");
		} else if (originalValue == null) {
			throw new IllegalArgumentException(String.format("Value of attribute %s is not specified.", attrName));
		} else if (originalValue.isBlank()) {
			throw new IllegalArgumentException(String.format("Value of attribute %s is blank.", attrName));
		} else {
			this.attrValue = originalValue.trim();
		}
	}

	public String getString() {
		return this.attrValue;
	}

	public boolean getBoolean() {
		if (!"true".equalsIgnoreCase(this.attrValue) && !"false".equalsIgnoreCase(this.attrValue)) {
			LOGGER.warn("Attribute '{}' value is not 'true' or 'false': '{}'.", this.attrName, this.originalValue);
		}
		return Boolean.parseBoolean(this.attrValue);
	}

	public Float getFloat() {
		try {
			return Float.parseFloat(this.attrValue);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' value is not float: '{}'.", this.attrName, this.originalValue);
			return null;
		}
	}

	public static final Pattern DELIMETER = Pattern.compile("[,\\s]+");

	public Iterator<String> iterator() {
		if (this.attrValue == null) {
			return null;
		}
		return Arrays.asList(DELIMETER.split(this.attrValue)).iterator();
	}

	public float[] getFloats(int expected) {
		try {
			String[] items = DELIMETER.split(this.attrValue);
			float[] res;
			if (expected > 0) {
				if (expected != items.length) {
					LOGGER.error("Items in attribute '{}' value '{}' exceeds expected number {}.", this.attrName,
							this.originalValue, expected);
					return null;
				}
				res = new float[expected];
			} else {
				res = new float[items.length];
			}
			int index = 0;
			for (String item : items) {
				res[index++] = Float.parseFloat(item);
			}
			return res;
		} catch (NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' value is not an array of floats: '{}'.", this.attrName, this.originalValue);
			return new float[0];
		}
	}

	public float[] getFloatArray() {
		return this.getFloats(0);
	}

	@FunctionalInterface
	public static interface FloatConsumer {
		public void accept(float fval);
	}

	public void setLengthInPoints(FloatConsumer consumer) {
		Float fval = this.getLength();
		if (fval != null) {
			consumer.accept(fval.floatValue());
		}
	}

	public Integer getInteger() {
		try {
			return Integer.parseInt(this.attrValue);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' value is not integer: '{}'.", this.attrName, this.originalValue);
			return null;
		}
	}

	public TextAlignment getTextAlign() {
		switch (this.attrValue) {
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
			LOGGER.error("Text-align not valid: '{}'", this.originalValue);
			return null;
		}
	}

	public VerticalAlignment getVerticalAlignment() {
		switch (this.attrValue) {
		case "top":
			return VerticalAlignment.TOP;
		case "middle":
			return VerticalAlignment.MIDDLE;
		case "bottom":
			return VerticalAlignment.BOTTOM;
		default:
			LOGGER.error("Vertical-alignment not valid: '{}'", this.originalValue);
			return null;
		}
	}

	protected static final Pattern LENGTH_OR_PERCENT = Pattern
			.compile("\\s*[\\+\\-]?(\\d+(?:\\.\\d+)?)\\s*(pt|mm|cm|in|inch|%)?\\s*");

	protected static class LengthParser {
		final String original;
		final boolean percentage;
		final float numeric;

		LengthParser(String value) {
			this.original = value;
			Matcher m = LENGTH_OR_PERCENT.matcher(value);
			if (m.matches()) {
				float fval = Float.parseFloat(m.group(1));
				String unit = m.groupCount() < 2 ? "pt" : m.group(2);
				if (unit == null) {
					unit = "pt";
				}
				switch (unit) {
				case "pt":
					this.numeric = fval;
					this.percentage = false;
					break;
				case "mm":
					this.numeric = LengthUnit.mm2pt(fval);
					this.percentage = false;
					break;
				case "cm":
					this.numeric = LengthUnit.cm2pt(fval);
					this.percentage = false;
					break;
				case "in":
				case "inch":
					this.numeric = LengthUnit.inch2pt(fval);
					this.percentage = false;
					break;
				case "%":
					this.numeric = fval;
					this.percentage = true;
					break;
				default:
					throw new NumberFormatException(value);
				}
			} else {
				throw new NumberFormatException(value);
			}
		}

		float getLength() {
			if (this.percentage) {
				throw new NumberFormatException(this.original);
			}
			return this.numeric;
		}

		UnitValue getUnitValue() {
			return this.percentage ? UnitValue.createPercentValue(this.numeric)
					: UnitValue.createPointValue(this.numeric);
		}
	}

	public UnitValue getUnitValue() {
		try {
			return new LengthParser(this.attrValue).getUnitValue();
		} catch (NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' value is not a length or percentage: '{}'.", this.attrName,
					this.originalValue);
			return null;
		}
	}

	public UnitValue[] getUnitValueArray() {
		String[] list = DELIMETER.split(this.attrValue);
		UnitValue[] res = new UnitValue[list.length];
		int index = 0;
		try {
			for (String item : list) {
				res[index++] = new LengthParser(item).getUnitValue();
			}
			return res;
		} catch (NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' is not an array of unit values: '{}'.", this.attrName, this.originalValue);
			return new UnitValue[0];
		}
	}

	public Float getLength() {
		try {
			return new LengthParser(this.attrValue).getLength();
		} catch (NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' is not accepted as points/length: '{}'.", this.attrName, this.originalValue);
			return null;
		}
	}

	public float[] getLengthArray(int expected) {
		try {
			String[] items = DELIMETER.split(this.attrValue);
			float[] res;
			if (expected > 0) {
				if (expected != items.length) {
					LOGGER.error("Items in attribute '{}' value '{}' exceeds expected number {}.", this.attrName,
							this.originalValue, expected);
					return null;
				}
				res = new float[expected];
			} else {
				res = new float[items.length];
			}
			int index = 0;
			for (String item : items) {
				res[index++] = new LengthParser(item).getLength();
			}
			return res;
		} catch (NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' value is not an array of floats: '{}'.", this.attrName, this.originalValue);
			return new float[0];
		}
	}

	public float[] getLengths() {
		return this.getLengthArray(0);
	}
	
	protected static final Pattern RGB_INT = Pattern.compile("(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})");
	protected static final Pattern RGB_HEX = Pattern.compile("#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})");
	protected static final Map<String, DeviceRgb> COMMON_COLORS = Map.ofEntries(
			Map.entry("white", (DeviceRgb)DeviceRgb.WHITE),
			Map.entry("black", (DeviceRgb)DeviceRgb.BLACK),
			Map.entry("red", (DeviceRgb)DeviceRgb.RED),
			Map.entry("green", (DeviceRgb)DeviceRgb.GREEN),
			Map.entry("blue", (DeviceRgb)DeviceRgb.BLUE),
			Map.entry("crimson", new DeviceRgb(220, 20, 60)),
			Map.entry("salmon", new DeviceRgb(250, 128, 114)),
			Map.entry("pink", new DeviceRgb(255, 192, 203)),
			Map.entry("coral", new DeviceRgb(255, 127, 80)),
			Map.entry("tomato", new DeviceRgb(255, 99, 71)),
			Map.entry("orange", new DeviceRgb(255, 165, 0)),
			Map.entry("gold", new DeviceRgb(255, 215, 0)),
			Map.entry("yellow", new DeviceRgb(255, 255, 0)),
			Map.entry("khaki", new DeviceRgb(240, 230, 140)),
			Map.entry("plum", new DeviceRgb(221, 160, 221)),
			Map.entry("violet", new DeviceRgb(238, 130, 238)),
			Map.entry("orchid", new DeviceRgb(218, 112, 214)),
			Map.entry("megenta", new DeviceRgb(255, 0, 255)),
			Map.entry("purple", new DeviceRgb(128, 0, 128)),
			Map.entry("indigo", new DeviceRgb(75, 0, 130)),
			Map.entry("lime", new DeviceRgb(50, 205, 50)),
			Map.entry("darkgreen", new DeviceRgb(0, 128, 0)),
			Map.entry("olive", new DeviceRgb(128, 128, 0)),
			Map.entry("teal", new DeviceRgb(0, 128, 128)),
			Map.entry("cyan", new DeviceRgb(0, 255, 255)),
			Map.entry("skyblue", new DeviceRgb(135, 206, 235)),
			Map.entry("navy", new DeviceRgb(0, 0, 128)),
			Map.entry("bisque", new DeviceRgb(255, 228, 196)),
			Map.entry("brown", new DeviceRgb(165, 42, 42)),
			Map.entry("maroon", new DeviceRgb(128, 0, 0)),
			Map.entry("snow", new DeviceRgb(255, 250, 250)),
			Map.entry("azure", new DeviceRgb(240, 255, 255)),
			Map.entry("ivory", new DeviceRgb(255, 255, 240)),
			Map.entry("lightgray", new DeviceRgb(211, 211, 211)),
			Map.entry("silver", new DeviceRgb(192, 192, 192)),
			Map.entry("darkgray", new DeviceRgb(169, 169, 169)),
			Map.entry("gray", new DeviceRgb(128, 128, 128))
			);
	static {
		LOGGER.info("Supported rgb color names: {}", COMMON_COLORS.keySet());
	}
	public DeviceRgb getDeviceRgb() {
		if(COMMON_COLORS.containsKey(this.attrValue)) {
			return COMMON_COLORS.get(this.attrValue);
		}
		Matcher m;
		m = RGB_INT.matcher(this.attrValue);
		if(m.matches()) {
			return new DeviceRgb(
					Integer.parseInt(m.group(1)),
					Integer.parseInt(m.group(2)),
					Integer.parseInt(m.group(3))
					);
		}
		m = RGB_HEX.matcher(this.attrValue.toLowerCase());
		if(m.matches()) {
			return new DeviceRgb(
					Integer.parseInt(m.group(1), 16),
					Integer.parseInt(m.group(2), 16),
					Integer.parseInt(m.group(3), 16)
					);
		}
		return null;
	}
	
	protected static final Pattern CMYK_INT = Pattern.compile("([01]?\\d{2})\\s*,\\s*([01]?\\d{2})\\s*,\\s*([01]?\\d{2}),\\s*([01]?\\d{2})");
	public DeviceCmyk getDeviceCmyk() {
		Matcher m = CMYK_INT.matcher(this.attrValue);
		if(m.matches()) {
			return new DeviceCmyk(
					Integer.parseInt(m.group(1)),
					Integer.parseInt(m.group(2)),
					Integer.parseInt(m.group(3)),
					Integer.parseInt(m.group(4))
					);
		}
		return null;
	}
}
