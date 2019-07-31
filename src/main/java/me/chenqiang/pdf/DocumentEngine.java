package me.chenqiang.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfa.PdfADocument;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.xml.XmlTemplateLoader;

public final class DocumentEngine {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentEngine.class);
	
	protected Map<String, DocumentComposer> documentTemplates;
	protected Function<PdfWriter, PdfDocument> documentCreator;
	
	public DocumentEngine() {
		this(PdfDocument::new);
	}
	
	public DocumentEngine(Function<PdfWriter, PdfDocument> documentCreator) {
		this.documentTemplates = new TreeMap<>();
		this.documentCreator = documentCreator;
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
	
	public DocumentComposer getDocument(String docId) {
		DocumentComposer composer = this.documentTemplates.get(docId);
		if(composer != null) {
			throw new IllegalArgumentException(String.format("Document with id '%s' does not exist.", docId));
		}
		return composer;
	}
	
	public byte [] produce(String docId, Map<String, String> subMap,
			Map<String, String> textParams, Map<String, byte []> dataParams) throws IOException {
		if(docId == null) {
			LOGGER.error("Document id '{}' does not exist.", docId);		
			return null;
		}
		DocumentComposer composer = this.documentTemplates.get(docId);
		if(composer == null) {
			LOGGER.error("Document width id '{}' does not exist.", docId);
			return null;
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		produce(composer, subMap, textParams, dataParams, bos);
		bos.close();
		return bos.toByteArray();
	}
	
	public void produce(String docId, Map<String, String> subMap,
			Map<String, String> textParams, Map<String, byte []> dataParams, OutputStream os) throws IOException {
		if(docId == null) {
			LOGGER.error("Document id '{}' does not exist.", docId);	
		}
		DocumentComposer composer = this.documentTemplates.get(docId);
		if(composer == null) {
			LOGGER.error("Document width id '{}' does not exist.", docId);
		}
		
		produce(composer, subMap, textParams, dataParams, os);
	}
	
	public static void produce(DocumentComposer composer, Map<String, String> subMap,
			Map<String, String> textParams, Map<String, byte []> dataParams,
			OutputStream os) throws IOException {
		DocumentComposer sub = Replacement.replace(composer, subMap, textParams, dataParams);
		
		PdfWriter writer = new PdfWriter(os);
		PdfDocument pdf = new PdfDocument(writer);
		pdf.setTagged();
		sub.compose(pdf, writer, true);
		writer.close();
	}

	public static PdfADocument getPdfADocument(PdfWriter writer) {
		InputStream icm = DocumentFactory.class.getResourceAsStream("/sRGB_CS_profile.icm");
		return new PdfADocument(writer, PdfAConformanceLevel.PDF_A_3A, 
				new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icm));
	}
}
