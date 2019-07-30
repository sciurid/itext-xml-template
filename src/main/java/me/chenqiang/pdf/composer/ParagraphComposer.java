package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.composer.DocumentComposer.DocumentComponent;
import me.chenqiang.pdf.composer.ParagraphComposer.ParagraphComponent;
import me.chenqiang.pdf.composer.TableCellComposer.TableCellComponent;
import me.chenqiang.pdf.configurability.StringStub;

public class ParagraphComposer extends BasicElementComposer<Paragraph, ParagraphComposer>
		implements DocumentComponent, TableCellComponent, Iterable<ParagraphComponent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphComposer.class);
	protected Supplier<Paragraph> creator;
	public static interface ParagraphComponent {
		public void process(Paragraph para);
	}

	protected List<ParagraphComponent> components;

	public ParagraphComposer(Supplier<Paragraph> creator) {
		super(Paragraph.class);
		this.creator = creator;
		this.components = new ArrayList<>();
	}
	
	public ParagraphComposer() {
		this(Paragraph::new);
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
		if (this.components.isEmpty()) {
			para.add("");
			LOGGER.warn("Empty paragraph found.");
		} else {
			LOGGER.debug("Paragraph with {} components.", this.components.size());
			this.components.forEach(component -> component.process(para));
		}
		return para;
	}

	@Override
	public void substitute(Map<String, String> params) {
		this.components.stream().filter(comp -> comp instanceof StringStub)
				.forEach(comp -> ((StringStub) comp).substitute(params));
	}

	@Override
	public Iterator<ParagraphComponent> iterator() {
		return this.components.iterator();
	}

}
