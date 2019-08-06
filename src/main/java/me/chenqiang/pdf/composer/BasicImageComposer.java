package me.chenqiang.pdf.composer;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.component.DivComponent;
import me.chenqiang.pdf.component.DocumentComponent;
import me.chenqiang.pdf.component.ParagraphComponent;
import me.chenqiang.pdf.component.TableCellComponent;

public abstract class BasicImageComposer<S extends BasicImageComposer<S>>
		extends BasicElementComposer<Image, S>
		implements DocumentComponent, ParagraphComponent, TableCellComponent, DivComponent {
	
	protected BasicImageComposer() {
		super(Image.class);
	}
	
	protected BasicImageComposer(BasicImageComposer<S> origin) {
		super(origin);
	}

	@Override
	public void process(Cell cell, DocumentContext context) {
		Image image = this.produce(context);
		if(image != null) {
			cell.add(image);
		}
	}

	@Override
	public void process(Paragraph para, DocumentContext context) {
		Image image = this.produce(context);
		if(image != null) {
			para.add(image);
		}
	}

	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer, DocumentContext context) {
		Image image = this.produce(context);
		if(image != null) {
			doc.add(image);
		}
	}
	
	@Override
	public void process(Div div, DocumentContext context) {
		Image image = this.produce(context);
		if(image != null) {
			div.add(image);
		}
	}
}
