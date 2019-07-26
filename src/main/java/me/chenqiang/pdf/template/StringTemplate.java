package me.chenqiang.pdf.template;

import com.itextpdf.layout.element.Paragraph;

public class StringTemplate implements ComponentInContainer<Paragraph>{
	private String content;
	
	public StringTemplate(String content) {
		super();
		this.content = content;
	}

	@Override
	public void process(Paragraph container) {
		container.add(content);
	}

}
