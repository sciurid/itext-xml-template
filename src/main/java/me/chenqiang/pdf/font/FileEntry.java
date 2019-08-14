package me.chenqiang.pdf.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

public class FileEntry implements FontRegistryEntry {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileEntry.class);
	protected File file;
	protected int ttcIndex;
	
	private FileEntry(File file, int ttcIndex) {
		this.file = file;
		this.ttcIndex = ttcIndex;
	}
	
	private FileEntry(File file) {
		this(file, -1);
	}
			
	@Override
	public PdfFont getFont() {
		if(file == null) {
			throw new IllegalStateException();
		}
		try(FileInputStream fis = new FileInputStream(this.file)) {				
			if(this.ttcIndex >= 0) {
				return PdfFontFactory.createTtcFont(fis.readAllBytes(), this.ttcIndex, PdfEncodings.IDENTITY_H, true, false);					
			}
			else {
				return PdfFontFactory.createFont(fis.readAllBytes(), PdfEncodings.IDENTITY_H, true, false);
			}
		} catch (IOException e) {
			LOGGER.error("Font Creation Error", e);
			return null;
		} 
	}		
	
	public static FileEntry create(File file) {
		FileEntry reg = new FileEntry(file);
		if(reg.getFont() != null) {
			return reg;
		}
		else {
			return null;
		}
	}
	public static FileEntry createTtc(File file, int ttcIndex) {
		FileEntry reg = new FileEntry(file, ttcIndex);
		if(reg.getFont() != null) {
			return reg;
		}
		else {
			return null;
		}
	}
	
	public static FileEntry create(String path) {
		return create(new File(path));
	}
	
	public static FileEntry createTtc(String path, int ttcIndex) {
		return createTtc(new File(path), ttcIndex);
	}
}