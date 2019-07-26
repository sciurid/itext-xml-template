package me.chenqiang.pdf.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class ParagraphTemplate extends ContainerTemplate<Paragraph, ParagraphTemplate>
implements DocumentComponentTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphTemplate.class);
	
	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {		
		Paragraph para = new Paragraph();
		if(this.components.isEmpty()) {
			para.add("");
			LOGGER.warn("Empty paragraph found.");
		}
		else {
			LOGGER.debug("Paragraph with {} components.", this.components.size());
			this.components.forEach(component -> component.process(para));
			this.apply(para);
		}
		doc.add(para);			
	}
	
	public void insertAt(ComponentInContainer<Paragraph> component, int index) {
		this.components.add(index, component);
	}

}
