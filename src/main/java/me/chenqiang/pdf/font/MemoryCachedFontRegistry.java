package me.chenqiang.pdf.font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.font.PdfFont;

import me.chenqiang.pdf.utils.SerializableCloning;

public class MemoryCachedFontRegistry implements FontRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(MemoryCachedFontRegistry.class);
	protected byte[] cache;

	public MemoryCachedFontRegistry(PdfFont font) {
		this.cache = SerializableCloning.toBytes(font);
		LOGGER.debug("Memory cached font registry is created.");
	}

	@Override
	public PdfFont getFont() {
		if (this.cache == null) {
			throw new IllegalStateException();
		}
		return SerializableCloning.fromBytes(cache);
	}

}