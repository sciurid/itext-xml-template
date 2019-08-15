package me.chenqiang.pdf.common.attribute;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import me.chenqiang.pdf.common.utils.LengthUnit;

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
			return null;
		}
	}

	public float[] getFloatArray() {
		return this.getFloats(0);
	}

	@FunctionalInterface
	public static interface FloatConsumer {
		public void accept(float fval);
	}

	public void setFloat(FloatConsumer consumer) {
		Float fval = this.getFloat();
		if(fval != null) {
			consumer.accept(fval);
		}
	}
	public void setLength(FloatConsumer consumer) {
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
	
	public HorizontalAlignment getHorizontalAlignment() {
		switch (this.attrValue) {
		case "center":
			return HorizontalAlignment.CENTER;
		case "left":
			return HorizontalAlignment.LEFT;
		case "right":
			return HorizontalAlignment.RIGHT;
		default:
			LOGGER.error("Vertical-alignment not valid: '{}'", this.originalValue);
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
			.compile("\\s*([\\+\\-]?\\d+(?:\\.\\d+)?)\\s*(pt|mm|cm|in|inch|%)?\\s*");

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

	public void setUnitValue(Consumer<? super UnitValue> function) {
		UnitValue unitValue = this.getUnitValue();
		if(unitValue != null) {
			function.accept(unitValue);
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
	
	public DeviceRgb getDeviceRgb() {
		DeviceRgb color = ColorMap.getDeviceRgb(this.attrValue);
		if(color == null) {
			LOGGER.error("Attribute '{}' value is not valid rgb color: '{}'.", this.attrName, this.originalValue);
		}
		return color;
	}
	
	public DeviceCmyk getDeviceCmyk() {
		DeviceCmyk color = ColorMap.getDeviceCmyk(this.attrValue);
		if(color == null) {
			LOGGER.error("Attribute '{}' value is not valid cmyk color: '{}'.", this.attrName, this.originalValue);
		}
		return color;
	}
	
	public void setDeviceRgb(Consumer<? super DeviceRgb> function) {
		DeviceRgb color = this.getDeviceRgb();
		if(color != null) {
			function.accept(color);
		}
	}
}
