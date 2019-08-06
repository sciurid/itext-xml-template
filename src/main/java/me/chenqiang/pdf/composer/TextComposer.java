package me.chenqiang.pdf.composer;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.component.DivComponent;
import me.chenqiang.pdf.component.ParagraphComponent;
import me.chenqiang.pdf.component.TableCellComponent;

public class TextComposer extends BasicElementComposer<Text, TextComposer>
		implements ParagraphComponent, TableCellComponent, DivComponent {
	protected final StringBuilder text;

	public TextComposer() {
		super(Text.class);
		this.text = new StringBuilder();
	}

	protected TextComposer(TextComposer original) {
		super(original);
		this.text = new StringBuilder(original.text);
	}

	public TextComposer(String str) {
		this();
		this.append(str);
	}

	public TextComposer append(String text) {
		this.text.append(text);
		return this;
	}

	@Override
	public void process(Cell cell, DocumentContext context) {
		Text evaluated = this.produce(context);
		if (evaluated != null) {
			cell.add(new Paragraph(evaluated));
		}
	}

	@Override
	protected Text create(DocumentContext context) {
		if (this.text.length() == 0) {
			return null;
		} 
		
		String evaluated = context == null ? this.text.toString() : context.eval(this.text.toString());
		return new Text(evaluated);
	}

	@Override
	public void process(Paragraph para, DocumentContext context) {
		Text element = this.produce(context);
		if (element != null) {
			para.add(element);
		}
	}

	@Override
	public void process(Div div, DocumentContext context) {
		Text element = this.produce(context);
		if (element != null) {
			div.add(new Paragraph(element));
		}
	}
}
