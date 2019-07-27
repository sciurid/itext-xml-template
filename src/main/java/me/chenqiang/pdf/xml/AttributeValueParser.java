package me.chenqiang.pdf.xml;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

public class AttributeValueParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(AttributeValueParser.class);
	private final String attrName;
	private final String originalValue;
	private final String attrValue;

	public AttributeValueParser(String attrName, String originalValue) {
		this.attrName = attrName;
		this.originalValue = originalValue;
		if (attrName == null) {
			LOGGER.error("Name of attribute is not specified.");
			this.attrValue = null;
		} else if (originalValue == null) {
			LOGGER.error("Value of attribute {} is not specified.", attrName);
			this.attrValue = null;
		} else {
			String trimmed = originalValue.trim();
			if (trimmed.isEmpty()) {
				LOGGER.error("Value of attribute {} is empty.", attrName);
				this.attrValue = null;
			} else {
				this.attrValue = trimmed;
			}
		}
	}

	public boolean isValid() {
		return this.attrValue != null;
	}

	public String getString() {
		return this.attrValue;
	}

	public boolean getBoolean() {
		if (!"true".equalsIgnoreCase(this.attrValue) && !"false".equalsIgnoreCase(this.attrValue)) {
			LOGGER.error("Attribute '{}' value is not 'true' or 'false': '{}'.", this.attrName, this.originalValue);
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

	public float[] getFloats(int expected) {
		StringTokenizer tokens = this.getTokens();
		if (expected > 0 && tokens.countTokens() != expected) {
			LOGGER.error("Attribute '{}' value is not an array of floats with expected length({}): '{}'.",
					this.attrName, expected, this.originalValue);
		}
		int index = 0;
		float[] res = new float[tokens.countTokens()];
		try {
			while (tokens.hasMoreTokens()) {
				float val = Float.parseFloat(tokens.nextToken());
				res[index++] = val;
			}
			return res;
		} catch (NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' value is not an array of floats: '{}'.", this.attrName, this.originalValue);
			return new float[0];
		}
	}

	public float[] getFloats() {
		return this.getFloats(0);
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

	public StringTokenizer getTokens() {
		return new StringTokenizer(this.attrValue, ",");
	}

	protected static final Pattern LENGTH_OR_PERCENT = Pattern.compile("\\s*(\\d+(?:\\.\\d+)?)\\s*(pt|mm|cm|in|inch|%)?\\s*");

	protected static UnitValue parseUnitValueString(String value) {
		Matcher m = LENGTH_OR_PERCENT.matcher(value);
		if (m.matches()) {
			float numeric = Float.parseFloat(m.group(1));
			String unit = m.groupCount() < 2 ? "pt" : m.group(2);
			if(unit == null) {
				unit = "pt";
			}
			switch (unit) {
			case "pt":
				return UnitValue.createPointValue(numeric);
			case "mm":
				return UnitValue.createPointValue(numeric * 72 / 25.4f);
			case "cm":
				return UnitValue.createPointValue(numeric * 72 / 2.54f);
			case "in":
			case "inch":
				return UnitValue.createPointValue(numeric * 72);
			case "%":
				return UnitValue.createPercentValue(numeric);
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	public UnitValue getUnitValue() {
		try {
			return parseUnitValueString(this.attrValue);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' value is not a length or percentage: '{}'.", this.attrName,
					this.originalValue);
			return null;
		}
	}

	public UnitValue[] getUnitValues() {
		StringTokenizer tokens = this.getTokens();
		int index = 0;
		UnitValue[] res = new UnitValue[tokens.countTokens()];
		try {
			while (tokens.hasMoreTokens()) {
				UnitValue val = parseUnitValueString(tokens.nextToken());
				res[index++] = val;
			}
			return res;
		} catch (NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' is not an array of unit values: '{}'.", this.attrName, this.originalValue);
			return new UnitValue[0];
		}
	}

	protected static final Pattern LENGTH = Pattern.compile("\\s*(\\d+(?:\\.\\d+)?)\\s*(pt|mm|cm|in|inch)?\\s*");

	protected static Float getPoints(String value) {
		Matcher m = LENGTH.matcher(value);
		if (m.matches()) {
			float numeric = Float.parseFloat(m.group(1));
			String unit = m.groupCount() < 3 ? "pt" : m.group(2);
			switch (unit) {
			case "pt":
				return numeric;
			case "mm":
				return numeric * 72 / 25.4f;
			case "cm":
				return numeric * 72 / 2.54f;
			case "in":
			case "inch":
				return numeric * 72;
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	protected Float getPoints() {
		Float res = getPoints(this.attrValue);
		if (res == null) {
			LOGGER.error("Attribute '{}' is not accepted as points/length: '{}'.", this.attrName, this.originalValue);
		}
		return res;
	}
}
