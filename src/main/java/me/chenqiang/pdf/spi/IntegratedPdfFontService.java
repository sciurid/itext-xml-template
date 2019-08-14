package me.chenqiang.pdf.spi;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

import me.chenqiang.pdf.font.FontRegistryEntry;

public interface IntegratedPdfFontService {
	public Map<String, FontRegistryEntry> getIntegratedFonts();
	public static Map<String, FontRegistryEntry> loadAll() {
		Map<String, FontRegistryEntry> fonts = new TreeMap<>();
		ServiceLoader<IntegratedPdfFontService> serviceLoader = ServiceLoader.load(IntegratedPdfFontService.class);
		for(IntegratedPdfFontService service : serviceLoader) {
			fonts.putAll(service.getIntegratedFonts());
		}
		return fonts;
	}
}
