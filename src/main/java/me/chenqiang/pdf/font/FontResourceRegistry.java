package me.chenqiang.pdf.font;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

public class FontResourceRegistry implements FontRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(FontResourceRegistry.class);
	protected Class<?> clazz;
	protected String resource;
	protected int ttcIndex;
					
	public FontResourceRegistry(Class<?> clazz, String resource, int ttcIndex) {
		this.clazz = clazz;
		this.resource = resource;
		this.ttcIndex = ttcIndex;
	}
	
	public FontResourceRegistry(Class<?> clazz, String resource) {
		this(clazz, resource, -1);
	}

	@Override
	public PdfFont getFont() {
		if(resource == null) {
			throw new IllegalStateException();
		}
		try(InputStream fis = clazz.getResourceAsStream(resource)) {
			byte [] data = fis.readAllBytes();
			if(this.ttcIndex < 0) {
				return PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, false);									
			}
			else {
				return PdfFontFactory.createTtcFont(data, this.ttcIndex, PdfEncodings.IDENTITY_H, true, false);
			}
		} catch (IOException e) {
			LOGGER.error("Font Creation Error", e);
			return null;
		} 
	}
	
	public static FontResourceRegistry create(Class<?> clazz, String resourcePath) {
		FontResourceRegistry reg = new FontResourceRegistry(clazz, resourcePath);
		if(reg.getFont() != null) {
			return reg;
		}
		else {
			return null;
		}
	}
	
	public static FontResourceRegistry createTtc(Class<?> clazz, String resourcePath, int ttcIndex) {
		FontResourceRegistry reg = new FontResourceRegistry(clazz, resourcePath, ttcIndex);
		if(reg.getFont() != null) {
			return reg;
		}
		else {
			return null;
		}
	}
}