package me.chenqiang.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.dom4j.DocumentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.pdfa.PdfADocument;

import me.chenqiang.pdf.common.utils.LengthUnit;
import me.chenqiang.pdf.font.PdfFontService;

public abstract class DocumentEngine {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentEngine.class);
	
	public static BiConsumer<InputStream, OutputStream> createPageNumberPattern(String pattern, Function<Integer, String> formatter, PdfFont font) {
		
		String ptn = pattern == null ? "%s/%s" : pattern;
		Function<Integer, String> fmt = formatter == null ? i -> Integer.toString(i): formatter;
		PdfFont numfnt;
		if(font == null) {
			PdfFont def;
			try {
				def = PdfFontFactory.createFont(StandardFonts.HELVETICA, PdfEncodings.UTF8, true, true);
			} catch (IOException e) {
				def = null;
			}
			numfnt = def;
		}
		else {
			numfnt = font;
		}
		return (is, os) -> {
			try {
				PdfDocument pdf = new PdfADocument(new PdfReader(is), new PdfWriter(os));
				int totalPages = pdf.getNumberOfPages();
				Document doc = new Document(pdf);
				for(int pageNum = 1; pageNum <= totalPages; pageNum++) {
					Rectangle pageSize = pdf.getPage(pageNum).getPageSize();
					Paragraph para = 
							new Paragraph(String.format(ptn, fmt.apply(pageNum), fmt.apply(totalPages)))
							.setFont(numfnt);
					doc.showTextAligned(para, 
							pageSize.getWidth() / 2 , LengthUnit.mm2pt(15), pageNum, 
							TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);			
				}
				doc.close();
			} catch (IOException e) {
				LOGGER.error("Error in adding page number.", e);
			}		
		};
	}
	public static final BiConsumer<InputStream, OutputStream> PAGE_NUMBER = createPageNumberPattern(null, null, null);
	public static final BiConsumer<InputStream, OutputStream> CHINESE_PAGE_NUMBER = createPageNumberPattern("第%s页，共%s页", null, PdfFontService.loadAll().get("新宋体").getFont());
	
	public static final BiConsumer<InputStream, OutputStream> PRINTING_ONLY = (is, os) -> {	
		String random = new Random(System.currentTimeMillis()).ints(20, 0, 94)
				.collect(StringBuilder::new, (sb, i) -> sb.append((char)(i + 33)), StringBuilder::append)
				.toString();
		LOGGER.warn("Random password used for pdf owner: {}", random);
		getEncryption(null, random).accept(is, os);		
	};	
	
	public static BiConsumer<InputStream, OutputStream> getEncryption(String userPassword, String ownerPassword) {
		return (is, os) -> {
			try {

				PdfWriter writer = new PdfWriter(os, new WriterProperties()
						.setStandardEncryption(userPassword == null ? null : userPassword.getBytes(), 
								ownerPassword == null ? null : ownerPassword.getBytes(), 
								EncryptionConstants.ALLOW_PRINTING,
								EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA));
				PdfDocument pdf = new PdfDocument(new PdfReader(is), writer);
				pdf.close();
			} catch (IOException e) {
				LOGGER.error("Error in setting printing-only.", e);
			}
		};
	}	
	
	public static PdfADocument createPdfADocument(PdfWriter writer) {
		InputStream icm = DocumentFactory.class.getResourceAsStream("/sRGB_CS_profile.icm");
		PdfADocument pdf = new PdfADocument(writer, PdfAConformanceLevel.PDF_A_3A, 
				new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icm));
		pdf.getCatalog().put(PdfName.Lang, new PdfString("ZH"));
		return pdf;
	}
}
