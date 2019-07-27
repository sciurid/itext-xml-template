package me.chenqiang.pdf.template.element;


import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import me.chenqiang.pdf.template.BasicElementTemplate;

public class TextTemplate extends BasicElementTemplate<Text, TextTemplate>
implements ParagraphComponent{
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
	protected Text create() {
		return new Text(this.content.toString());
	}

	@Override
	public void process(Paragraph para) {
		para.add(this.<Void>produce(null));
	}		
}
