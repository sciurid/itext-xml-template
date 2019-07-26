package me.chenqiang.pdf.xml;

import java.util.StringTokenizer;

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
		if(attrName == null) {
			LOGGER.error("Name of attribute is not specified.");
			this.attrValue = null;
		}
		else if(originalValue == null) {
			LOGGER.error("Value of attribute {} is not specified.", attrName);
			this.attrValue = null;
		}
		else {
			String trimmed = originalValue.trim();
			if(trimmed.isEmpty()) {
				LOGGER.error("Value of attribute {} is empty.", attrName);
				this.attrValue = null;
			}
			else {
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
		if(!"true".equalsIgnoreCase(this.attrValue) && !"false".equalsIgnoreCase(this.attrValue)) {
			LOGGER.error("Attribute '{}' value is not 'true' or 'false': '{}'.", this.attrName, this.originalValue);
		}
		return Boolean.parseBoolean(this.attrValue);
	}
	
	public Float getFloat() {
		try {
			return Float.parseFloat(this.attrValue);
		}
		catch(NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' value is not float: '{}'.", this.attrName, this.originalValue);
			return null;
		}
	}
	
	public float[] getFloats(int expected) {
		StringTokenizer tokens = this.getTokens();
		if(expected > 0 && tokens.countTokens() != expected) {
			LOGGER.error("Attribute '{}' value is not an array of floats with expected length({}): '{}'.", this.attrName, expected, this.originalValue);
		}
		int index = 0;
		float [] res = new float[tokens.countTokens()];
		try {
			while(tokens.hasMoreTokens()) {
				float val = Float.parseFloat(tokens.nextToken());
				res[index++] = val;
			}
			return res;
		}
		catch(NumberFormatException nfe) {
			LOGGER.error("Attribute '{}' value is not an array of floats: '{}'.", this.attrName, this.originalValue);
			return new float[0];
		}		
	}
	
	public float[] getFloats() {
		return this.getFloats(0);
	}
	
	public TextAlignment getTextAlign() {
		switch(this.attrValue) {
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
		switch(this.attrValue) {
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
		return new StringTokenizer(this.attrValue, ", ");
	}
	
	public UnitValue getUnitValue() {
		float fval;
		if(this.attrValue.endsWith("%")) {
			String f = this.attrValue.substring(0, this.attrValue.length() - 1).trim();			
			try {
				fval = Float.parseFloat(f);
				return UnitValue.createPercentValue(fval);
			}
			catch(NumberFormatException nfe) {
				LOGGER.error("Attribute '{}' value is not a float percentage: '{}'.", this.attrName, this.originalValue);
				return null;
			}
		}
		else {
			fval = this.getFloat();
			return UnitValue.createPointValue(fval);
		}
	}
}
