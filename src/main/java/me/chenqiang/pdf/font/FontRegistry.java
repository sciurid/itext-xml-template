package me.chenqiang.pdf.font;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

public class FontRegistry implements Function<String, FontRegistryEntry>{
	protected Map<String, FontRegistryEntry> fonts = new TreeMap<>();
	
	public void initialize() {
		this.fonts.putAll(PdfFontService.loadAll());
	}
	
	public void loadData(byte [] data, String ... names) throws IOException {
		PdfFont font = PdfFontFactory.createFont(data, PdfEncodings.IDENTITY_H, true, false);
		for(String name : names) {
			if(data.length > (1 << 8)) {
				this.fonts.put(name, new FileCachedEntry(font));
			}
			else {
				this.fonts.put(name, new MemoryCachedEntry(font));
			}
		}
	}
	
	public void loadTtcData(byte [] ttcData, int ttcIndex, String ... names) throws IOException {
		PdfFont font = PdfFontFactory.createTtcFont(ttcData, ttcIndex, PdfEncodings.IDENTITY_H, true, false);
		for(String name : names) {
			if(ttcData.length > (1 << 6)) {
				this.fonts.put(name, new FileCachedEntry(font));
			}
			else {
				this.fonts.put(name, new MemoryCachedEntry(font));
			}
		}
	}
	
	public void loadFile(String path, String ... names) throws IOException {
		for(String name : names) {
			this.fonts.put(name, FileEntry.create(path));
		}
	}
	
	public void loadTtcFile(String path, int ttcIndex, String ... names) throws IOException {
		for(String name : names) {
			this.fonts.put(name, FileEntry.createTtc(path, ttcIndex));
		}
	}
		
	public void loadResource(String resource, String ... names) throws IOException {
		for(String name : names) {
			this.fonts.put(name, ResourceEntry.create(this.getClass(), resource));
		}
	}
	
	public void loadTtcResource(String resource, int ttcIndex, String ... names) throws IOException {
		for(String name : names) {
			this.fonts.put(name, ResourceEntry.createTtc(this.getClass(), resource, ttcIndex));
		}
	}

	@Override
	public FontRegistryEntry apply(String t) {
		return this.fonts.get(t);
	}	
}
