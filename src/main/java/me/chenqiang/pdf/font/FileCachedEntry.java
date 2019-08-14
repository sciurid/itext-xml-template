package me.chenqiang.pdf.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.font.PdfFont;

import me.chenqiang.pdf.utils.SerializableCloning;

public class FileCachedEntry implements FontRegistryEntry {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileCachedEntry.class);
	protected File file;
	
	public FileCachedEntry(PdfFont font) {
		try {
			this.file = File.createTempFile("font-", ".cache");
			try(FileOutputStream fos = new FileOutputStream(file)) {
				SerializableCloning.toOutputStream(font, fos);
			}
			LOGGER.debug("File cached font registry is created.");
			
		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
	@Override
	public PdfFont getFont() {
		if(file == null) {
			throw new IllegalStateException();
		}
		try(FileInputStream fis = new FileInputStream(this.file)) {
			return SerializableCloning.fromInputStream(fis);
		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		} 
	}	
}