package me.chenqiang.pdf.sax.composer;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.sax.composer.component.DivComponent;
import me.chenqiang.pdf.sax.composer.component.ParagraphComponent;
import me.chenqiang.pdf.sax.composer.component.TableCellComponent;

public class StringComposer implements ParagraphComponent, TableCellComponent, DivComponent {
	protected final String text;	

	public StringComposer(String content) {
		this.text = content;
	}
	
	protected StringComposer(StringComposer composer) {
		this.text = composer.text;
	}

	protected String getEvaluatedString(DocumentContext context) {
		if (this.text != null && this.text.length() > 0) {
			return context == null ? this.text : context.eval(this.text);
		}
		else {
			return null;
		}
	}
	
	@Override
	public void process(Paragraph para, DocumentContext context) {
		String evaluated = this.getEvaluatedString(context);
		if (evaluated != null) {
			para.add(evaluated);
		}
	}

	@Override
	public void process(Cell cell, DocumentContext context) {
		String evaluated = this.getEvaluatedString(context);
		if (evaluated != null) {
			cell.add(new Paragraph(evaluated));
		}
	}
	
	@Override
	public void process(Div div, DocumentContext context) {
		String evaluated = this.getEvaluatedString(context);
		if (evaluated != null) {
			div.add(new Paragraph(evaluated));
		}
	}
	
	@Override
	public String toString() {
		return this.text;
	}
}
