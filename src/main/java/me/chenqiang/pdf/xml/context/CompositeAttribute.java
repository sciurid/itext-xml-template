package me.chenqiang.pdf.xml.context;

import me.chenqiang.pdf.attribute.BackgroundColorAttribute;
import me.chenqiang.pdf.attribute.FontColorAttribute;

public class CompositeAttribute {
	protected FontColorAttribute fontColor = null;
	protected BackgroundColorAttribute backgroundColor = null;
	
	public FontColorAttribute getFontColor() {
		if(this.fontColor == null) {
			this.fontColor = new FontColorAttribute();
		}
		return this.fontColor;
	}
	
	public BackgroundColorAttribute getBackgroundColor() {
		if(this.backgroundColor == null) {
			this.backgroundColor = new BackgroundColorAttribute();
		}
		return this.backgroundColor;
	}
	
	public CompositeAttribute applyFontColor(FontColorAttribute.Acceptor acceptor) {
		if(this.fontColor != null) {
			acceptor.accept(this.fontColor);
		}
		return this;
	}
	
	public CompositeAttribute applyBackgroundColor(BackgroundColorAttribute.Acceptor acceptor) {
		if(this.backgroundColor != null) {
			acceptor.accept(this.backgroundColor);
		}
		return this;
	}
}
