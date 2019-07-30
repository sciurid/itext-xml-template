package me.chenqiang.pdf.composer;

import java.util.Map;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.component.DivComponent;
import me.chenqiang.pdf.component.ParagraphComponent;
import me.chenqiang.pdf.component.TableCellComponent;
import me.chenqiang.pdf.configurability.StringStub;
import me.chenqiang.pdf.configurability.Substitution;

public class StringComposer implements ParagraphComponent, TableCellComponent, DivComponent, StringStub {
	private String text;
	protected ThreadLocal<String> substituted = new ThreadLocal<>();
	

	public StringComposer(String content) {
		super();
		this.text = content;
	}

	@Override
	public void process(Paragraph para) {
		if (this.text != null && this.text.length() > 0) {
			para.add(this.getCurrentText());
		}
	}

	@Override
	public void process(Cell cell) {
		if (this.text != null && this.text.length() > 0) {
			cell.add(new Paragraph(this.getCurrentText()));
		}
	}
	
	@Override
	public void process(Div div) {
		if (this.text != null && this.text.length() > 0) {
			div.add(new Paragraph(this.getCurrentText()));
		}
	}
	
	@Override
	public String toString() {
		return this.text;
	}

	@Override
	public void substitute(Map<String, String> params) {
		this.substituted.set(Substitution.substitute(this.text, params));
	}

	@Override
	public void reset() {
		this.substituted.remove();
	}
	
	protected String getCurrentText() {
		String dataText = this.substituted.get();
		if(dataText == null) {
			dataText = this.text;
		}
		return dataText;
	}
}
