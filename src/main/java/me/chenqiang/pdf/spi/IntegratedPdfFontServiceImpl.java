package me.chenqiang.pdf.spi;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import me.chenqiang.pdf.font.FontRegistryEntry;
import me.chenqiang.pdf.font.ResourceEntry;

public class IntegratedPdfFontServiceImpl implements IntegratedPdfFontService{
	protected Map<String, FontRegistryEntry> fonts;

	public IntegratedPdfFontServiceImpl() {
		this.fonts = new TreeMap<>();
		this.fonts.put("宋体", ResourceEntry.createTtc(IntegratedPdfFontServiceImpl.class, "simsun.ttc", 0));
		this.fonts.put("新宋体", ResourceEntry.createTtc(IntegratedPdfFontServiceImpl.class, "simsun.ttc", 1));
		this.fonts.put("黑体", ResourceEntry.create(IntegratedPdfFontServiceImpl.class, "simhei.ttf"));
		this.fonts.put("仿宋", ResourceEntry.create(IntegratedPdfFontServiceImpl.class, "simfang.ttf"));
		this.fonts.put("楷体", ResourceEntry.create(IntegratedPdfFontServiceImpl.class, "simkai.ttf"));
	}
	
	protected static InputStream getResource(String name) {
		return IntegratedPdfFontServiceImpl.class.getResourceAsStream(name);
	}
	
	@Override
	public Map<String, FontRegistryEntry> getIntegratedFonts() {
		return Collections.unmodifiableMap(this.fonts);
	}
}
