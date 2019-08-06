package me.chenqiang.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.spi.IntegratedPdfFontService;
import me.chenqiang.pdf.utils.LengthUnit;
import me.chenqiang.pdf.xml.XmlTemplateLoader;

public final class DocumentEngine {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentEngine.class);
	
	protected Map<String, DocumentComposer> documentTemplates;
	protected Function<PdfWriter, PdfDocument> documentCreator;
	protected List<BiConsumer<InputStream, OutputStream>> documentModifiers;
	
	public DocumentEngine() {
		this(PdfDocument::new);
	}
	
	public DocumentEngine(Function<PdfWriter, PdfDocument> documentCreator) {
		this.documentTemplates = new TreeMap<>();
		this.documentCreator = documentCreator;
		this.documentModifiers = new ArrayList<>();
	}
	
	public void load(InputStream xml) throws DocumentException {
		XmlTemplateLoader loader = new XmlTemplateLoader();
		loader.load(xml);
		loader.getDocumentComposerMap().forEach((k, v) -> {
			if(this.documentTemplates.containsKey(k)) {
				LOGGER.warn("Document id conflict: {}", k);
			}
			else {
				this.documentTemplates.put(k, v);
			}
		});
	}
	
	public void add(BiConsumer<InputStream, OutputStream> modifier) {
		this.documentModifiers.add(modifier);
	}
	
	public DocumentComposer getDocument(String docId) {
		if(docId == null) {
			LOGGER.error("Document id '{}' is null.", docId);		
			throw new IllegalArgumentException(String.format("Document id '%s' is null.", docId));
		}
		DocumentComposer composer = this.documentTemplates.get(docId);
		if(composer == null) {
			LOGGER.error("Document width id '{}' does not exist.", docId);
			throw new IllegalArgumentException(String.format("Document with id '%s' does not exist.", docId));
		}
		composer.setAuthor("清华大学 Tsinghua University.");
		return composer;
	}
	
	public byte [] produce(String docId, Map<String, Object> params) throws IOException {		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		produce(this.getDocument(docId), params, bos);
		bos.close();
		return bos.toByteArray();
	}
	
	public void produce(String docId, Map<String, Object> params, OutputStream os) throws IOException {
		ByteArrayInputStream bis;
		try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) { 
			produce(this.getDocument(docId), params, buffer);
			bis = new ByteArrayInputStream(buffer.toByteArray());
		}
		for(BiConsumer<InputStream, OutputStream> modifier : this.documentModifiers) {
			try(ByteArrayOutputStream next = new ByteArrayOutputStream()) { 
				modifier.accept(bis, next);
				bis = new ByteArrayInputStream(next.toByteArray());
			}
		}
		bis.transferTo(os);
	}
	
	public static final BiConsumer<InputStream, OutputStream> PAGE_NUMBER = (is, os) -> {
		try {
			PdfDocument pdf = new PdfADocument(new PdfReader(is), new PdfWriter(os));
			int totalPages = pdf.getNumberOfPages();
			Document doc = new Document(pdf);
			for(int i = 1; i <= totalPages; i++) {
				Rectangle pageSize = pdf.getPage(i).getPageSize();
				Paragraph para = 
						new Paragraph(String.format("第 %s 页，共 %s 页", i, totalPages))
						.setFont(IntegratedPdfFontService.loadAll().get("新宋体").getFont());
				doc.showTextAligned(para, 
						pageSize.getWidth() / 2 , LengthUnit.mm2pt(15), i, 
						TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);			
			}
			doc.close();
		} catch (IOException e) {
			LOGGER.error("Error in adding page number.", e);
		}		
	};
	
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
	
	public static void produce(DocumentComposer composer, Map<String, Object> params, OutputStream os) throws IOException {		
		PdfWriter writer = new PdfWriter(os, new WriterProperties().addXmpMetadata());
		PdfDocument pdf = getPdfADocument(writer);
		pdf.setFlushUnusedObjects(true);
		pdf.setTagged();
		
		DocumentContext context = new DocumentContext(params);
		composer.compose(pdf, writer, true, context);
		
		writer.close();
	}

	public static PdfADocument getPdfADocument(PdfWriter writer) {
		InputStream icm = DocumentFactory.class.getResourceAsStream("/sRGB_CS_profile.icm");
		PdfADocument pdf = new PdfADocument(writer, PdfAConformanceLevel.PDF_A_3A, 
				new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icm));
		pdf.getCatalog().put(PdfName.Lang, new PdfString("ZH"));
		return pdf;
	}
}
