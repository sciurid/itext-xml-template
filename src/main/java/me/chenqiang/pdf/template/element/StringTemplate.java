package me.chenqiang.pdf.template.element;

import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.template.table.TableComponent;

public class StringTemplate implements ParagraphComponent, TableComponent{
	private String content;
	
	public StringTemplate(String content) {
		super();
		this.content = content;
	}

	@Override
	public void process(Paragraph para) {
		para.add(this.content);
	}

	@Override
	public String toString() {
		return this.content;
	}
	
}
