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
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import me.chenqiang.pdf.composer.DocumentComposer;

public abstract class PdfTest {
	protected PdfFont songti;
	protected PdfFont heiti;
	protected PdfFont fangsong;
	@Before
	public void registerFonts() throws IOException {
		ClassLoader loader = PdfTest.class.getClassLoader();
		URL root = PdfTest.class.getResource("/simsun.ttc");
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
		PdfWriter writer = new PdfWriter(fos);
//		PdfDocument pdf = new PdfADocument(writer, PdfAConformanceLevel.PDF_A_3A, 
//				new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", DocumentFactory.class.getResourceAsStream("/sRGB_CS_profile.icm")));
		PdfDocument pdf = new PdfDocument(writer);
		pdf.setTagged();
		template.compose(pdf, writer, true);
		writer.close();
		fos.close();
		Desktop.getDesktop().open(file);
	}
}
