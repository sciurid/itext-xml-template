package me.chenqiang.pdf.font;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

public interface PdfFontService {
	public Map<String, FontRegistryEntry> getIntegratedFonts();
	public static Map<String, FontRegistryEntry> loadAll() {
		Map<String, FontRegistryEntry> fonts = new TreeMap<>();
		ServiceLoader<PdfFontService> serviceLoader = ServiceLoader.load(PdfFontService.class);
		for(PdfFontService service : serviceLoader) {
			fonts.putAll(service.getIntegratedFonts());
		}
		return fonts;
	}
}
