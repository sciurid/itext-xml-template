package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.component.DivComponent;
import me.chenqiang.pdf.component.DocumentComponent;
import me.chenqiang.pdf.component.ParagraphComponent;
import me.chenqiang.pdf.component.TableCellComponent;

public final class ParagraphComposer extends BasicElementComposer<Paragraph, ParagraphComposer>
		implements DocumentComponent, TableCellComponent, DivComponent, Iterable<ParagraphComponent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphComposer.class);
	protected List<ParagraphComponent> components;

	public ParagraphComposer() {
		super(Paragraph.class);
		this.components = new ArrayList<>();
	}

	public ParagraphComposer append(ParagraphComponent component) {
		if (component != null) {
			this.components.add(component);
		}
		return this;
	}

	public ParagraphComposer insertAt(ParagraphComponent component, int index) {
		if (component != null) {
			this.components.add(index, component);
		}
		return this;
	}

	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer, DocumentContext context) {
		Paragraph para = this.produce(context);
		doc.add(para);
	}

	@Override
	public void process(Cell cell, DocumentContext context) {
		Paragraph para = this.produce(context);
		cell.add(para);
	}

	@Override
	public void process(Div div, DocumentContext context) {
		Paragraph para = this.produce(null);
		div.add(para);
	}

	@Override
	protected Paragraph create(DocumentContext context) {
		Paragraph para = new Paragraph();
		if (this.components.isEmpty()) {
			para.add("");
			LOGGER.warn("Empty paragraph found.");
		} else {
			LOGGER.debug("Paragraph with {} components.", this.components.size());
			this.components.forEach(component -> component.process(para, context));
		}
		return para;
	}

	@Override
	public Iterator<ParagraphComponent> iterator() {
		return this.components.iterator();
	}
}
