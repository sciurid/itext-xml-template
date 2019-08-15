package me.chenqiang.pdf.font;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SCFontServiceImpl implements PdfFontService{
	protected Map<String, FontRegistryEntry> fonts;

	public SCFontServiceImpl() {
		this.fonts = new TreeMap<>();
		this.fonts.put("宋体", ResourceEntry.createTtc(SCFontServiceImpl.class, "simsun.ttc", 0));
		this.fonts.put("新宋体", ResourceEntry.createTtc(SCFontServiceImpl.class, "simsun.ttc", 1));
		this.fonts.put("黑体", ResourceEntry.create(SCFontServiceImpl.class, "simhei.ttf"));
		this.fonts.put("仿宋", ResourceEntry.create(SCFontServiceImpl.class, "simfang.ttf"));
		this.fonts.put("楷体", ResourceEntry.create(SCFontServiceImpl.class, "simkai.ttf"));
	}
	
	protected static InputStream getResource(String name) {
		return SCFontServiceImpl.class.getResourceAsStream(name);
	}
	
	@Override
	public Map<String, FontRegistryEntry> getIntegratedFonts() {
		return Collections.unmodifiableMap(this.fonts);
	}
}
