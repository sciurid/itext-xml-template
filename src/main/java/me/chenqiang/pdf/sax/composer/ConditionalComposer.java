package me.chenqiang.pdf.sax.composer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.sax.composer.component.DivComponent;
import me.chenqiang.pdf.sax.composer.component.DocumentComponent;
import me.chenqiang.pdf.sax.composer.component.ParagraphComponent;
import me.chenqiang.pdf.sax.composer.component.PdfElementComposer;
import me.chenqiang.pdf.sax.composer.component.TableCellComponent;
import me.chenqiang.pdf.sax.composer.component.TableComponent;

public abstract class ConditionalComposer 
implements DocumentComponent, ParagraphComponent, TableComponent, DivComponent, TableCellComponent {
	@SuppressWarnings("rawtypes")
	protected List<PdfElementComposer> components = new ArrayList<>();
	
	public void append(@SuppressWarnings("rawtypes") PdfElementComposer comp) {
		this.components.add(comp);
	}	
	
	protected abstract void doConditional(
			DocumentContext context, @SuppressWarnings("rawtypes") Consumer<PdfElementComposer> processor);
	
	@Override
	public void process(Table tbl, DocumentContext context) {
		this.doConditional(context, comp -> {
			if(comp instanceof TableComponent) {
				((TableComponent)comp).process(tbl, context);
			}
		});
	}

	@Override
	public void process(Paragraph para, DocumentContext context) {
		this.doConditional(context, comp -> {
			if(comp instanceof ParagraphComponent) {
				((ParagraphComponent)comp).process(para, context);
			}
		});
	}

	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer, DocumentContext context) {
		this.doConditional(context, comp -> {
			if(comp instanceof DocumentComponent) {
				((DocumentComponent)comp).process(doc, pdf, writer, context);
			}
		});
	}

	@Override
	public void process(Div div, DocumentContext context) {
		this.doConditional(context, comp -> {
			if(comp instanceof DivComponent) {
				((DivComponent)comp).process(div, context);
			}
		});
	}

	@Override
	public void process(Cell cell, DocumentContext context) {
		this.doConditional(context, comp -> {
			if(comp instanceof TableCellComponent) {
				((TableCellComponent)comp).process(cell, context);
			}
		});
	}
		
}
