package me.chenqiang.pdf.spi;

import java.util.Map;

import me.chenqiang.pdf.font.FontRegistry;

public interface IntegratedPdfFontService {
	public Map<String, FontRegistry> getIntegratedFonts();
}
