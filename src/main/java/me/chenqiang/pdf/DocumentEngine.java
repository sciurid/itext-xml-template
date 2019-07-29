package me.chenqiang.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

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
	private DocumentEngine() {}
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentEngine.class);
	public static void produce(InputStream xml, String documentId, 
			Map<String, String> textParams, Map<String, byte []> dataParams,
			OutputStream os) throws DocumentException, IOException {
		XmlTemplateLoader loader = new XmlTemplateLoader();
		loader.load(xml);
		DocumentComposer composer = loader.getDocumentComposer(documentId);
		if(composer == null) {
			LOGGER.error("Document id '{}' does not exist.", documentId);
			return;
		}
		composer.parameterize(textParams, dataParams);
		
		PdfWriter writer = new PdfWriter(os);
		PdfDocument pdf = new PdfDocument(writer);
		pdf.setTagged();
		composer.compose(pdf, writer, true);
		writer.close();
	}
	
	protected PdfADocument getPdfADocument(PdfWriter writer) {
		InputStream icm = DocumentFactory.class.getResourceAsStream("/sRGB_CS_profile.icm");
		return new PdfADocument(writer, PdfAConformanceLevel.PDF_A_3A, 
				new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icm));
	}
}
