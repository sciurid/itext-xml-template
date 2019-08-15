package me.chenqiang.pdf.font;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class HanSCFontServiceImpl implements PdfFontService{
	protected Map<String, FontRegistryEntry> fonts;

	public HanSCFontServiceImpl() {
		this.fonts = new TreeMap<>();
		this.fonts.put("思源黑体", getResource("hansc/SourceHanSansSC-Medium.otf"));
		this.fonts.put("思源粗宋", getResource("hansc/SourceHanSerifSC-Bold.otf"));
		this.fonts.put("思源宋体", getResource("hansc/SourceHanSerifSC-Medium.otf"));
	}
	
	protected static FontRegistryEntry getResource(String name) {
		return ResourceEntry.create(HanSCFontServiceImpl.class, name);
	}
	
	@Override
	public Map<String, FontRegistryEntry> getIntegratedFonts() {
		return Collections.unmodifiableMap(this.fonts);
	}
}
