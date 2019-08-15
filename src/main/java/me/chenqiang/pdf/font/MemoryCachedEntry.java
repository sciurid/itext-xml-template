package me.chenqiang.pdf.font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.font.PdfFont;

import me.chenqiang.pdf.common.utils.SerializableCloning;

public class MemoryCachedEntry implements FontRegistryEntry {
	private static final Logger LOGGER = LoggerFactory.getLogger(MemoryCachedEntry.class);
	protected byte[] cache;

	public MemoryCachedEntry(PdfFont font) {
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