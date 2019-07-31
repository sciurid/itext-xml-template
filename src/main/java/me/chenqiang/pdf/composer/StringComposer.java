package me.chenqiang.pdf.composer;

import java.util.Map;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.component.Copyable;
import me.chenqiang.pdf.component.DivComponent;
import me.chenqiang.pdf.component.ParagraphComponent;
import me.chenqiang.pdf.component.StringStub;
import me.chenqiang.pdf.component.TableCellComponent;
import me.chenqiang.pdf.utils.Substitution;

public class StringComposer implements ParagraphComponent, TableCellComponent, DivComponent, StringStub, Copyable<StringComposer> {
	private String text;	

	public StringComposer(String content) {
		this.text = content;
	}
	
	protected StringComposer(StringComposer composer) {
		this.text = composer.text;
	}

	@Override
	public void process(Paragraph para) {
		if (this.text != null && this.text.length() > 0) {
			para.add(this.text);
		}
	}

	@Override
	public void process(Cell cell) {
		if (this.text != null && this.text.length() > 0) {
			cell.add(new Paragraph(this.text));
		}
	}
	
	@Override
	public void process(Div div) {
		if (this.text != null && this.text.length() > 0) {
			div.add(new Paragraph(this.text));
		}
	}
	
	@Override
	public String toString() {
		return this.text;
	}

	@Override
	public void substitute(Map<String, String> params) {
		this.text = Substitution.substitute(this.text, params);
	}

	@Override
	public StringComposer copy() {
		return new StringComposer(this);
	}
}
