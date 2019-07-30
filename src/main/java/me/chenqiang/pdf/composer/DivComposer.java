package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.component.DivComponent;
import me.chenqiang.pdf.component.DocumentComponent;
import me.chenqiang.pdf.component.ParagraphComponent;
import me.chenqiang.pdf.component.TableCellComponent;
import me.chenqiang.pdf.configurability.Substitution;

public class DivComposer extends BasicElementComposer<Div, DivComposer>
		implements DocumentComponent, TableCellComponent, ParagraphComponent, Iterable<DivComponent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DivComposer.class);
	protected List<DivComponent> components;

	public DivComposer() {
		super(Div.class);
		this.components = new ArrayList<>();
	}
	
	public DivComposer append(DivComponent component) {
		if (component != null) {
			this.components.add(component);
		}
		return this;
	}

	public DivComposer insertAt(DivComponent component, int index) {
		if (component != null) {
			this.components.add(index, component);
		}
		return this;
	}

	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {
		Div div = this.<Void>produce(null);
		doc.add(div);
	}

	@Override
	public void process(Cell cell) {
		Div div = this.<Void>produce(null);
		cell.add(div);
	}
	
	@Override
	public void process(Paragraph para) {
		Div div = this.<Void>produce(null);
		para.add(div);
	}

	@Override
	protected Div create() {
		Div div = new Div();
		if (this.components.isEmpty()) {
			LOGGER.warn("Empty div found.");
		} else {
			LOGGER.debug("Div with {} components.", this.components.size());
			this.components.forEach(component -> component.process(div));
		}
		return div;
	}

	@Override
	public void substitute(Map<String, String> params) {
		Substitution.substitute(components, params);
	}
	
	@Override
	public void reset() {
		Substitution.reset(this.components);
	}

	@Override
	public Iterator<DivComponent> iterator() {
		return this.components.iterator();
	}
	
}
