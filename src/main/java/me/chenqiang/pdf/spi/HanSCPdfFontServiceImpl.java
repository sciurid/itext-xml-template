package me.chenqiang.pdf.spi;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import me.chenqiang.pdf.font.FontRegistryEntry;
import me.chenqiang.pdf.font.ResourceEntry;

public class HanSCPdfFontServiceImpl implements IntegratedPdfFontService{
	protected Map<String, FontRegistryEntry> fonts;

	public HanSCPdfFontServiceImpl() {
		this.fonts = new TreeMap<>();
		this.fonts.put("思源黑体", getResource("hansc/SourceHanSansSC-Medium.otf"));
		this.fonts.put("思源粗宋", getResource("hansc/SourceHanSerifSC-Bold.otf"));
		this.fonts.put("思源宋体", getResource("hansc/SourceHanSerifSC-Medium.otf"));
	}
	
	protected static FontRegistryEntry getResource(String name) {
		return ResourceEntry.create(HanSCPdfFontServiceImpl.class, name);
	}
	
	@Override
	public Map<String, FontRegistryEntry> getIntegratedFonts() {
		return Collections.unmodifiableMap(this.fonts);
	}
}
