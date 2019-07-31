package me.chenqiang.pdf.xml.context;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Style;

import me.chenqiang.pdf.spi.IntegratedPdfFontService;
import me.chenqiang.pdf.utils.SerializableCloning;

public class ResourceRepository {
	protected Map<String, byte []> fonts;
	protected Map<String, Style> styles;
	protected Map<String, byte []> images;

	public ResourceRepository() {
		this.fonts = new TreeMap<>();
		this.loadIntegratedPdfFonts();
		this.styles = new TreeMap<>();
		this.images = new TreeMap<>();
	}

	protected void loadIntegratedPdfFonts() {
		ServiceLoader<IntegratedPdfFontService> serviceLoader = ServiceLoader.load(IntegratedPdfFontService.class);
		for(IntegratedPdfFontService service : serviceLoader) {
			service.getIntegratedFonts().forEach((k, v) -> {
				this.fonts.put(k, SerializableCloning.toBytes(v));
			});
		}
	}
	
	public void loadSingleFontData(byte [] data, String ... names) throws IOException {
		PdfFont font = PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, true);
		for(String name : names) {
			this.fonts.put(name, SerializableCloning.toBytes(font));
		}
	}
	
	public void loadTtcFontData(byte [] ttcData, int ttcIndex, String ... names) throws IOException {
		PdfFont font = PdfFontFactory.createTtcFont(ttcData, ttcIndex, PdfEncodings.IDENTITY_H, true, true);
		for(String name : names) {
			this.fonts.put(name, SerializableCloning.toBytes(font));
		}
	}
	
	public void loadFontFile(String path, String ... names) throws IOException {
		if(path.toLowerCase().endsWith(".ttc")) {
			this.loadTtcFontFile(path, 0, names);
		}
		else {
			PdfFont font = PdfFontFactory.createFont(path, PdfEncodings.IDENTITY_H, true, true);
			for(String name : names) {
				this.fonts.put(name, SerializableCloning.toBytes(font));
			}
		}		
	}
	
	public void loadTtcFontFile(String path, int ttcIndex, String ... names) throws IOException {
		PdfFont font = PdfFontFactory.createTtcFont(path, ttcIndex, PdfEncodings.IDENTITY_H, true, true);
		for(String name : names) {
			this.fonts.put(name, SerializableCloning.toBytes(font));
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
	
	public byte [] getFont(String name) {
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
