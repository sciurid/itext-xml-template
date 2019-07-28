package me.chenqiang.pdf.composer;

import java.util.Map;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.utils.Substitution;

public class StringComposer implements ParagraphComponent, TableCellComponent, StringStub {
	private String content;

	public StringComposer(String content) {
		super();
		this.content = content;
	}

	@Override
	public void process(Paragraph para) {
		if (this.content != null) {
			para.add(this.content);
		}
	}

	@Override
	public void process(Cell cell) {
		if (this.content != null && this.content.length() > 0) {
			cell.add(new Paragraph(this.content));
		}
	}

	@Override
	public String toString() {
		return this.content;
	}

	@Override
	public void substitute(Map<String, String> params) {
		this.content = Substitution.substitute(this.content, params);
	}
}
