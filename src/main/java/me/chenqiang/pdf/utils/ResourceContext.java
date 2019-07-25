package me.chenqiang.pdf.utils;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Style;

public class ResourceContext {
	protected Map<String, PdfFont> fonts;
	protected Map<String, Style> styles;
		
	public ResourceContext() {
		this.fonts = new TreeMap<>();
		this.styles = new TreeMap<>();
	}

	public void loadSingleFontData(byte [] data, String ... names) throws IOException {
		PdfFont font = PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, true);
		for(String name : names) {
			this.fonts.put(name, font);
		}
	}
	
	public void loadTtcFontData(byte [] ttcData, int ttcIndex, String ... names) throws IOException {
		PdfFont font = PdfFontFactory.createTtcFont(ttcData, ttcIndex, PdfEncodings.IDENTITY_H, true, true);
		for(String name : names) {
			this.fonts.put(name, font);
		}
	}
	
	public void loadFontFile(String path, String ... names) throws IOException {
		if(path.toLowerCase().endsWith(".ttc")) {
			this.loadTtcFontFile(path, 0, names);
		}
		else {
			PdfFont font = PdfFontFactory.createFont(path, PdfEncodings.IDENTITY_H, true, true);
			for(String name : names) {
				this.fonts.put(name, font);
			}
		}		
	}
	
	public void loadTtcFontFile(String path, int ttcIndex, String ... names) throws IOException {
		PdfFont font = PdfFontFactory.createTtcFont(path, ttcIndex, PdfEncodings.IDENTITY_H, true, true);
		for(String name : names) {
			this.fonts.put(name, font);
		}
	}
	
	public void loadFontResource(String resource, String ... names) throws IOException {
		if(resource.toLowerCase().endsWith(".ttc")) {
			this.loadTtcFontResource(resource, 0, names);
		}
		else {
			this.loadSingleFontData(ResourceContext.class.getResourceAsStream(resource).readAllBytes(), names);
		}
	}
	
	public void loadTtcFontResource(String resource, int ttcIndex, String ... names) throws IOException {
		this.loadTtcFontData(ResourceContext.class.getResourceAsStream(resource).readAllBytes(), ttcIndex, names);
	}
	
	public PdfFont getFont(String name) {
		return this.fonts.get(name);
	}
	
	public void registerStyle(String name, Style style) {
		this.styles.put(name, style);
	}
	
	public Style getStyle(String name) {
		return this.styles.get(name);
	}
}
