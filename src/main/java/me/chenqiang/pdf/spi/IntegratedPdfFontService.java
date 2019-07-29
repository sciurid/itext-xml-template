package me.chenqiang.pdf.spi;

import java.util.Map;

import com.itextpdf.kernel.font.PdfFont;

public interface IntegratedPdfFontService {
	public Map<String, PdfFont> getIntegratedFonts();
}
