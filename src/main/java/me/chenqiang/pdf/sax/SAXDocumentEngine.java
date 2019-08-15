package me.chenqiang.pdf.sax;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.DocumentEngine;
import me.chenqiang.pdf.common.utils.ByteBufferInputStream;
import me.chenqiang.pdf.common.utils.ByteBufferOutputStream;
import me.chenqiang.pdf.sax.composer.DocumentComposer;

public final class SAXDocumentEngine extends DocumentEngine{
	private static final Logger LOGGER = LoggerFactory.getLogger(SAXDocumentEngine.class);
	
	protected Map<String, DocumentComposer> documentTemplates;
	protected Function<PdfWriter, PdfDocument> documentCreator;
	
	public SAXDocumentEngine() {
		this(true);
	}
	
	public SAXDocumentEngine(boolean pdfA) {
		this.documentTemplates = new TreeMap<>();
		if(pdfA) {
			this.documentCreator = DocumentEngine::createPdfADocument;
		}
		else {
			this.documentCreator = PdfDocument::new;
		}
	}
	
	public void load(InputStream xml) throws DocumentException {
		XmlTemplate tpl = new XmlTemplate(xml);
		tpl.getComposerMap().forEach((k, v) -> {
			if(this.documentTemplates.containsKey(k)) {
				LOGGER.warn("Document id conflict, ignored: {}", k);
			}
			else {
				this.documentTemplates.put(k, v);
			}
		});
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
		return composer;
	}
		
	public void produce(String docId, Map<String, Object> params, Collection<BiConsumer<InputStream, OutputStream>> modifiers, OutputStream os) throws IOException {
		ByteBufferInputStream bis;
		try (ByteBufferOutputStream buffer = new ByteBufferOutputStream()) { 
			produce(this.getDocument(docId), params, buffer);
			bis = buffer.transfer();
			if(modifiers != null) {
				for(BiConsumer<InputStream, OutputStream> modifier : modifiers) {
					buffer.reset();
					modifier.accept(bis, buffer);
					bis = buffer.transfer();
				}
			}
		}
		bis.transferTo(os);
	}
	
	public static void produce(DocumentComposer composer, Map<String, Object> params, OutputStream os) throws IOException {		
		PdfWriter writer = new PdfWriter(os, new WriterProperties().addXmpMetadata());
		PdfDocument pdf = createPdfADocument(writer);
		pdf.setFlushUnusedObjects(true);
		pdf.setTagged();
		
		DocumentContext context = new DocumentContext(params);
		composer.compose(pdf, writer, true, context);
		
		writer.close();
	}	
}
