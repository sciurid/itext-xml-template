package me.chenqiang.pdf.composer;


import java.util.Map;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import me.chenqiang.pdf.component.DivComponent;
import me.chenqiang.pdf.component.ParagraphComponent;
import me.chenqiang.pdf.component.TableCellComponent;
import me.chenqiang.pdf.configurability.StringParameterPlaceholder;
import me.chenqiang.pdf.configurability.Substitution;

public class TextComposer extends BasicElementComposer<Text, TextComposer>
implements ParagraphComponent, TableCellComponent, DivComponent, StringParameterPlaceholder {
	protected StringBuffer text;
	protected ThreadLocal<String> substituted = new ThreadLocal<>();
	
	public TextComposer() {
		super(Text.class);
		this.text = new StringBuffer();
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
	public void process(Cell cell) {
		Text str = this.create();
		if(str != null) {
			cell.add(new Paragraph(str));
		}
	}

	@Override
	protected Text create() {
		if(this.text.length() == 0) {
			return null;
		}
		else {
			return new Text(this.getCurrentText());
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
	public void process(Div div) {
		Text text = this.<Void>produce(null);
		if(text != null) {
			div.add(new Paragraph(text));
		}
	}

	@Override
	public void substitute(Map<String, String> params) {
		if(Substitution.isSubstitutable(this.text.toString())) {
			this.substituted.set(Substitution.substitute(this.text.toString(), params));
		}
	}

	@Override
	public void reset() {
		this.substituted.remove();
	}
	
	protected String getCurrentText() {
		String dataText = this.substituted.get();
		if(dataText == null) {
			dataText = this.text.toString();
		}	
		return dataText;
	}

	@Override
	public void setParameter(String parameter) {
		this.text.delete(0, this.text.length()).append(parameter);
	}	
}
