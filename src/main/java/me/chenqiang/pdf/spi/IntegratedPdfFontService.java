package me.chenqiang.pdf.spi;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

import me.chenqiang.pdf.font.FontRegistry;

public interface IntegratedPdfFontService {
	public Map<String, FontRegistry> getIntegratedFonts();
	public static Map<String, FontRegistry> loadAll() {
		Map<String, FontRegistry> fonts = new TreeMap<>();
		ServiceLoader<IntegratedPdfFontService> serviceLoader = ServiceLoader.load(IntegratedPdfFontService.class);
		for(IntegratedPdfFontService service : serviceLoader) {
			fonts.putAll(service.getIntegratedFonts());
		}
		return fonts;
	}
}
