package me.chenqiang.pdf.composer;


import java.util.Map;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import me.chenqiang.pdf.utils.Substitution;

public class TextComposer extends BasicElementComposer<Text, TextComposer>
implements ParagraphComponent, TableCellComponent, ParameterPlaceholder<String>{
	private StringBuilder content;
	
	public TextComposer() {
		super();
		this.content = new StringBuilder();
	}
	
	public TextComposer(String str) {
		this();
		this.append(str);
	}
	
	public TextComposer append(String text) {
		this.content.append(text);
		return this;
	}
	
	@Override
	public void process(Cell cell) {
		cell.add(new Paragraph(this.create()));		
	}

	@Override
	protected Text create() {
		if(this.content.length() == 0) {
			return null;
		}
		else {
			return new Text(this.content.toString());
		}
	}

	@Override
	public void process(Paragraph para) {
		Text text = this.<Void>produce(null);
		if(text != null) {
			para.add(text);
		}
	}

	@Override
	public void substitute(Map<String, String> params) {
		this.content = new StringBuilder();
		this.content.append(Substitution.substitute(this.content.toString(), params));
	}

	@Override
	public void setParameter(String parameter) {
		this.content = new StringBuilder(parameter);
	}	
}
