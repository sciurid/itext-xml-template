package me.chenqiang.pdf.template;


import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

public class TextTemplate extends StyledTemplate<Text, TextTemplate> 
implements ComponentTemplate<Paragraph>{
	private StringBuilder content;
	
	public TextTemplate() {
		super();
		this.content = new StringBuilder();
	}
	
	public TextTemplate(String str) {
		this();
		this.append(str);
	}
	
	public TextTemplate append(String text) {
		this.content.append(text);
		return this;
	}

	@Override
	public void process(Paragraph container) {
		Text text = new Text(this.content.toString());
		this.apply(text);
		container.add(text);
	}
}
