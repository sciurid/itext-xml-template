package me.chenqiang.pdf;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

public class ProducePdfDocument {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
	
	private PdfFont songti;
	private PdfFont heiti;
	private PdfFont fangsong;
	@Before
	public void registerFonts() throws IOException {
		ClassLoader loader = ProducePdfDocument.class.getClassLoader();		
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

	@Test
	public void produce() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();		
		PdfWriter writer = new PdfWriter(buffer);
		PdfDocument pdf = new PdfDocument(writer);
		
		Document doc = new Document(pdf);
		float margin = 3.0f * 72 / 2.54f;
		doc.setMargins(margin, margin, margin, margin);
		
		doc.add(
				new Paragraph("在 职 证 明")
				.setFont(this.heiti)
				.setFontSize(24)
				.setMultipliedLeading(1.5f)
				.setMarginTop(20).setMarginBottom(30)
				.setTextAlignment(TextAlignment.CENTER)
				);
		
		String name = "刘乃嘉";
		LocalDate date = LocalDate.of(2005, 9, 1);
		String unit = "信息化技术中心";
		BigDecimal income = BigDecimal.valueOf(100, 2);		
		String pattern = "兹证明%s同志于%s起在我校%s工作，上一年度（%d年）收入共计%s元。";
		
		String content = String.format(pattern, name, FORMATTER.format(date), unit,
				LocalDate.now().getYear() - 1, income.toPlainString());
		
		doc.add(new Paragraph(content)
				.setFont(this.songti)
				.setMultipliedLeading(1.5f)
				.setFontSize(14)
				.setFirstLineIndent(28));
		
		doc.add(new Paragraph("清华大学\n人事处\n" + FORMATTER.format(LocalDate.now()))
				.setFont(this.songti)
				.setMultipliedLeading(1.5f)
				.setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER)
				.setPaddingLeft(10.0f * 72 / 2.54f));
		
		doc.close();	
		pdf.close();
		writer.close();
		
		FileOutputStream fos = new FileOutputStream("D:\\Temp\\test.pdf");
		fos.write(buffer.toByteArray());
		fos.close();
	}
}
