package me.chenqiang.pdf.composer;


import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

public class TextComposer extends BasicElementComposer<Text, TextComposer>
implements ParagraphComponent, CellComponent{
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
		return new Text(this.content.toString());
	}

	@Override
	public void process(Paragraph para) {
		para.add(this.<Void>produce(null));
	}		
}
