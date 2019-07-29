package me.chenqiang.pdf.spi;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

public class IntegratedPdfFontServiceImpl implements IntegratedPdfFontService{
	private static final Logger LOGGER = LoggerFactory.getLogger(IntegratedPdfFontServiceImpl.class);
	protected Map<String, PdfFont> fonts;

	public IntegratedPdfFontServiceImpl() {
		this.fonts = new TreeMap<>();
		
		byte [] data;
		
		try {
			data = IntegratedPdfFontServiceImpl.class.getResourceAsStream("simsun.ttc").readAllBytes();
			PdfFont simsun = PdfFontFactory.createTtcFont(data, 0, PdfEncodings.IDENTITY_H, true, true);
			this.fonts.put("宋体", simsun);
			
			data = IntegratedPdfFontServiceImpl.class.getResourceAsStream("simhei.ttf").readAllBytes();
			PdfFont simhei = PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, true);
			this.fonts.put("黑体", simhei);
			
			data = IntegratedPdfFontServiceImpl.class.getResourceAsStream("simfang.ttf").readAllBytes();
			PdfFont simfang = PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, true);
			this.fonts.put("仿宋", simfang);

			data = IntegratedPdfFontServiceImpl.class.getResourceAsStream("simkai.ttf").readAllBytes();
			PdfFont simkai = PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, true);
			this.fonts.put("楷体", simkai);
		} catch (IOException e) {
			LOGGER.debug("内置字体加载失败：", e);
		}
	}
	
	@Override
	public Map<String, PdfFont> getIntegratedFonts() {
		return Collections.unmodifiableMap(this.fonts);
	}
}
