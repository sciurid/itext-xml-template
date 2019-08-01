package me.chenqiang.pdf.spi;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import me.chenqiang.pdf.font.FontRegistry;
import me.chenqiang.pdf.font.FontResourceRegistry;

public class HanSCPdfFontServiceImpl implements IntegratedPdfFontService{
	protected Map<String, FontRegistry> fonts;

	public HanSCPdfFontServiceImpl() {
		this.fonts = new TreeMap<>();
		this.fonts.put("思源黑体", getResource("hansc/SourceHanSansSC-Medium.otf"));
		this.fonts.put("思源粗宋", getResource("hansc/SourceHanSerifSC-Bold.otf"));
		this.fonts.put("思源宋体", getResource("hansc/SourceHanSerifSC-Medium.otf"));
	}
	
	protected static FontRegistry getResource(String name) {
		return FontResourceRegistry.create(HanSCPdfFontServiceImpl.class, name);
	}
	
	@Override
	public Map<String, FontRegistry> getIntegratedFonts() {
		return Collections.unmodifiableMap(this.fonts);
	}
}
