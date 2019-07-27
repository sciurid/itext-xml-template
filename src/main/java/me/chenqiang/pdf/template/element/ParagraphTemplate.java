package me.chenqiang.pdf.template.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.template.BasicElementTemplate;
import me.chenqiang.pdf.template.table.TableComponent;

public class ParagraphTemplate extends BasicElementTemplate<Paragraph, ParagraphTemplate>
implements DocumentComponentTemplate, CellComponent, TableComponent{
	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphTemplate.class);
	
	protected List<ParagraphComponent> components;
	public ParagraphTemplate() {
		this.components = new ArrayList<>();
	}
	
	public ParagraphTemplate(ParagraphComponent ... comps) {
		this();
		for(ParagraphComponent comp : comps) {
			this.components.add(comp);
		}
	}
	
	public ParagraphTemplate(Collection<? extends ParagraphComponent> comps) {
		this();
		this.components.addAll(comps);
	}
	
	
	public ParagraphTemplate append(ParagraphComponent component) {
		this.components.add(component);
		return this;
	}

	public ParagraphTemplate insertAt(ParagraphComponent component, int index) {
		this.components.add(index, component);
		return this;
	}

	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {
		Paragraph para = this.<Void>produce(null);
		doc.add(para);
	}
	
	@Override
	public void process(Cell cell) {
		Paragraph para = this.<Void>produce(null);
		cell.add(para);
	}

	@Override
	protected Paragraph create() {
		Paragraph para = new Paragraph();
		if(this.components.isEmpty()) {
			para.add("");
			LOGGER.warn("Empty paragraph found.");
		}
		else {
			LOGGER.debug("Paragraph with {} components.", this.components.size());
			this.components.forEach(component -> component.process(para));
		}
		return para;
	}	
}
