package me.chenqiang.pdf.xml.context;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Style;

import me.chenqiang.pdf.spi.IntegratedPdfFontService;

public class ResourceRepository {
	protected Map<String, PdfFont> fonts;
	protected Map<String, Style> styles;
	protected Map<String, ImageData> images;

	public ResourceRepository() {
		this.fonts = new TreeMap<>();
		this.loadIntegratedPdfFonts();
		this.styles = new TreeMap<>();
		this.images = new TreeMap<>();
	}

	protected void loadIntegratedPdfFonts() {
		ServiceLoader<IntegratedPdfFontService> serviceLoader = ServiceLoader.load(IntegratedPdfFontService.class);
		serviceLoader.forEach(service -> this.fonts.putAll(service.getIntegratedFonts()));
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
			this.loadSingleFontData(ResourceRepository.class.getResourceAsStream(resource).readAllBytes(), names);
		}
	}
	
	public void loadTtcFontResource(String resource, int ttcIndex, String ... names) throws IOException {
		this.loadTtcFontData(ResourceRepository.class.getResourceAsStream(resource).readAllBytes(), ttcIndex, names);
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
	
	public void registerImage(String id, ImageData data) {
		this.images.put(id, data);
	}
	
	public ImageData getImage(String id) {
		return this.images.get(id);
	}
}
