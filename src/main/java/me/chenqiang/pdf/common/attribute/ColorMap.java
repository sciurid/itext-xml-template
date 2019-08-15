package me.chenqiang.pdf.common.attribute;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceRgb;

public final class ColorMap {
	private static final Logger LOGGER = LoggerFactory.getLogger(ColorMap.class);
	private ColorMap() {
	}
	protected static final Pattern RGB_INT = Pattern.compile("(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})");
	protected static final Pattern RGB_HEX = Pattern.compile("#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})");
	protected static final Pattern CMYK_INT = Pattern.compile("([01]?\\d{2})\\s*,\\s*([01]?\\d{2})\\s*,\\s*([01]?\\d{2}),\\s*([01]?\\d{2})");
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
	public static DeviceRgb getDeviceRgb(String text) {
		if(COMMON_COLORS.containsKey(text)) {
			return COMMON_COLORS.get(text);
		}
		Matcher m;
		m = RGB_INT.matcher(text);
		if(m.matches()) {
			return new DeviceRgb(
					Integer.parseInt(m.group(1)),
					Integer.parseInt(m.group(2)),
					Integer.parseInt(m.group(3))
					);
		}
		m = RGB_HEX.matcher(text.toLowerCase());
		if(m.matches()) {
			return new DeviceRgb(
					Integer.parseInt(m.group(1), 16),
					Integer.parseInt(m.group(2), 16),
					Integer.parseInt(m.group(3), 16)
					);
		}
		return null;
	}
	
	public static DeviceCmyk getDeviceCmyk(String text) {
		if(COMMON_COLORS.containsKey(text)) {
			return DeviceCmyk.convertRgbToCmyk(COMMON_COLORS.get(text));
		}
		Matcher m = CMYK_INT.matcher(text);		
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
	
	public static Color getColor(String text) {
		Color color = ColorMap.getDeviceRgb(text);
		if(color == null) {
			color = ColorMap.getDeviceCmyk(text);
		}
		return color;
	}
}
