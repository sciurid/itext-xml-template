package me.chenqiang.pdf.xml.context;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Style;

import me.chenqiang.pdf.font.FileCachedFontRegistry;
import me.chenqiang.pdf.font.FontFileRegistry;
import me.chenqiang.pdf.font.FontRegistry;
import me.chenqiang.pdf.font.FontResourceRegistry;
import me.chenqiang.pdf.font.MemoryCachedFontRegistry;
import me.chenqiang.pdf.spi.IntegratedPdfFontService;

public class ResourceRepository {
	static final Logger LOGGER = LoggerFactory.getLogger(ResourceRepository.class);
//	protected Map<String, byte []> fonts;
	protected Map<String, Style> styles;
	protected Map<String, byte []> images;
	protected Map<String, FontRegistry> fonts;
	
	public ResourceRepository() {
		this.fonts = IntegratedPdfFontService.loadAll();
		this.styles = new TreeMap<>();
		this.images = new TreeMap<>();
	}

	
	public void loadSingleFontData(byte [] data, String ... names) throws IOException {
		PdfFont font = PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, false);
		for(String name : names) {
			if(data.length > (1 << 6)) {
				this.fonts.put(name, new FileCachedFontRegistry(font));
			}
			else {
				this.fonts.put(name, new MemoryCachedFontRegistry(font));
			}
		}
	}
	
	public void loadTtcFontData(byte [] ttcData, int ttcIndex, String ... names) throws IOException {
		PdfFont font = PdfFontFactory.createTtcFont(ttcData, ttcIndex, PdfEncodings.IDENTITY_H, true, false);
		for(String name : names) {
			if(ttcData.length > (1 << 6)) {
				this.fonts.put(name, new FileCachedFontRegistry(font));
			}
			else {
				this.fonts.put(name, new MemoryCachedFontRegistry(font));
			}
		}
	}
	
	public void loadFontFile(String path, String ... names) throws IOException {
		for(String name : names) {
			this.fonts.put(name, FontFileRegistry.create(path));
		}
	}
	
	public void loadTtcFontFile(String path, int ttcIndex, String ... names) throws IOException {
		for(String name : names) {
			this.fonts.put(name, FontFileRegistry.createTtc(path, ttcIndex));
		}
	}
		
	public void loadFontResource(String resource, String ... names) throws IOException {
		for(String name : names) {
			this.fonts.put(name, FontResourceRegistry.create(this.getClass(), resource));
		}
	}
	
	public void loadTtcFontResource(String resource, int ttcIndex, String ... names) throws IOException {
		for(String name : names) {
			this.fonts.put(name, FontResourceRegistry.createTtc(this.getClass(), resource, ttcIndex));
		}
	}
	
	public FontRegistry getFont(String name) {
		return this.fonts.get(name);
	}
	
	public void registerStyle(String name, Style style) {
		this.styles.put(name, style);
	}
	
	public Style getStyle(String name) {
		return this.styles.get(name);
	}
	
	public void registerImage(String id, byte [] data) {
		this.images.put(id, data);
	}
	
	public byte [] getImage(String id) {
		return this.images.get(id);
	}
}
