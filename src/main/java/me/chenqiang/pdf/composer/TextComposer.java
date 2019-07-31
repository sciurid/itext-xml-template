package me.chenqiang.pdf.composer;

import java.util.Map;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import me.chenqiang.pdf.component.DivComponent;
import me.chenqiang.pdf.component.ParagraphComponent;
import me.chenqiang.pdf.component.StringParameterPlaceholder;
import me.chenqiang.pdf.component.StringStub;
import me.chenqiang.pdf.component.TableCellComponent;
import me.chenqiang.pdf.utils.Substitution;

public class TextComposer extends BasicElementComposer<Text, TextComposer>
		implements ParagraphComponent, TableCellComponent, DivComponent, StringParameterPlaceholder, StringStub {
	protected StringBuilder text;

	public TextComposer() {
		super(Text.class);
		this.text = new StringBuilder();
	}

	protected TextComposer(TextComposer original) {
		super(original);
		this.text = new StringBuilder(original.text);
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
		if (str != null) {
			cell.add(new Paragraph(str));
		}
	}

	@Override
	protected Text create() {
		if (this.text.length() == 0) {
			return null;
		} else {
			return new Text(this.getCurrentText());
		}
	}

	@Override
	public void process(Paragraph para) {
		Text text = this.<Void>produce(null);
		if (text != null) {
			para.add(text);
		}
	}

	@Override
	public void process(Div div) {
		Text text = this.<Void>produce(null);
		if (text != null) {
			div.add(new Paragraph(text));
		}
	}

	@Override
	public void substitute(Map<String, String> params) {
		this.text = new StringBuilder(Substitution.substitute(this.text.toString(), params));
	}

	protected String getCurrentText() {
		return this.text.toString();
	}

	@Override
	public void setParameter(String parameter) {
		this.text.delete(0, this.text.length()).append(parameter);
	}

	@Override
	public TextComposer copy() {
		return new TextComposer(this);
	}
}
