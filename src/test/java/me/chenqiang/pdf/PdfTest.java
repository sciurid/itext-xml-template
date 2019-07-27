package me.chenqiang.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.DocumentFactory;

public abstract class PdfTest {
	private PdfFont songti;
	private PdfFont heiti;
	private PdfFont fangsong;
	@Before
	public void registerFonts() throws IOException {
		ClassLoader loader = ProducePdfDocument.class.getClassLoader();
		URL root = ProducePdfDocument.class.getResource("/simsun.ttc");
		System.out.println(root);
		this.songti = PdfFontFactory.createTtcFont(
				loader.getResourceAsStream("simsun.ttc").readAllBytes(), 0, 
				PdfEncodings.IDENTITY_H, true, true);
		this.heiti = PdfFontFactory.createFont(
				loader.getResourceAsStream("simhei.ttf").readAllBytes(), 
				PdfEncodings.IDENTITY_H, true, true);
		this.fangsong = PdfFontFactory.createFont(
				loader.getResourceAsStream("simfang.ttf").readAllBytes(), 
				PdfEncodings.IDENTITY_H, true, true);
	}
	
	protected void render(DocumentComposer template, String prefix) throws IOException {
		File file = File.createTempFile(prefix, ".pdf");
		FileOutputStream fos = new FileOutputStream(file);
		DocumentFactory.produce(template, fos);
		fos.close();
		Desktop.getDesktop().open(file);
	}
}
