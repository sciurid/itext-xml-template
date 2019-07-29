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

public class HanSCPdfFontServiceImpl implements IntegratedPdfFontService{
	private static final Logger LOGGER = LoggerFactory.getLogger(HanSCPdfFontServiceImpl.class);
	protected Map<String, PdfFont> fonts;

	public HanSCPdfFontServiceImpl() {
		this.fonts = new TreeMap<>();
		
		byte [] data;
		
		try {
			data = HanSCPdfFontServiceImpl.class.getResourceAsStream("hansc/SourceHanSansSC-Medium.otf").readAllBytes();
			PdfFont sans = PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, true);
			this.fonts.put("思源黑体", sans);
			
			data = HanSCPdfFontServiceImpl.class.getResourceAsStream("hansc/SourceHanSerifSC-Bold.otf").readAllBytes();
			PdfFont serifB = PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, true);
			this.fonts.put("思源粗宋", serifB);
			
			data = HanSCPdfFontServiceImpl.class.getResourceAsStream("hansc/SourceHanSerifSC-Medium.otf").readAllBytes();
			PdfFont serif = PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, true);
			this.fonts.put("思源宋体", serif);
		} catch (IOException e) {
			LOGGER.debug("内置字体加载失败：", e);
		}
	}
	
	@Override
	public Map<String, PdfFont> getIntegratedFonts() {
		return Collections.unmodifiableMap(this.fonts);
	}
}
