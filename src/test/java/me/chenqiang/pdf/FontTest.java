package me.chenqiang.pdf;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.FontProgramDescriptor;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;

public class FontTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(FontTest.class);
//	@Test
	public void listSystemFonts() {
		FontProvider fp = new FontProvider();
		fp.addSystemFonts();
		FontSet fs = fp.getFontSet();
		LOGGER.info("FontSet size: {}", fs.size());
		
		fs.getFonts().forEach(fi -> {
			FontProgramDescriptor descriptor = fi.getDescriptor();
			LOGGER.info("Name: {}, Alias: {}, FamilyName: {}, FullName: {}", 
					fi.getFontName(), fi.getAlias(), descriptor.getFamilyNameLowerCase(), descriptor.getFontName());
			
			});
		
	}
}
