package me.chenqiang.pdf.composer;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;

public class StringComposer implements ParagraphComponent, CellComponent{
	private String content;
	
	public StringComposer(String content) {
		super();
		this.content = content;
	}

	@Override
	public void process(Paragraph para) {
		para.add(this.content);
	}

	@Override
	public void process(Cell cell) {
		cell.add(new Paragraph(this.content));
	}

	@Override
	public String toString() {
		return this.content;
	}
	
}
