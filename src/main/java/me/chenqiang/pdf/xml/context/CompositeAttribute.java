package me.chenqiang.pdf.xml.context;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.itextpdf.layout.ElementPropertyContainer;
import com.itextpdf.layout.borders.Border;

import me.chenqiang.pdf.attribute.BackgroundColorAttribute;
import me.chenqiang.pdf.attribute.BorderAttribute;
import me.chenqiang.pdf.attribute.FontColorAttribute;

public class CompositeAttribute {
	protected FontColorAttribute fontColor = null;
	protected BackgroundColorAttribute backgroundColor = null;
	protected BorderAttribute common = null;
	protected BorderAttribute top = null;
	protected BorderAttribute right = null;
	protected BorderAttribute bottom = null;
	protected BorderAttribute left = null;

	public FontColorAttribute createAndGetFontColor() {
		if (this.fontColor == null) {
			this.fontColor = new FontColorAttribute();
		}
		return this.fontColor;
	}

	public BackgroundColorAttribute createAndGetBackgroundColor() {
		if (this.backgroundColor == null) {
			this.backgroundColor = new BackgroundColorAttribute();
		}
		return this.backgroundColor;
	}

	public BorderAttribute createAndGetBorder() {
		if (this.common == null) {
			this.common = new BorderAttribute();
		}
		return this.common;
	}

	public BorderAttribute createAndGetTopBorder() {
		if (this.top == null) {
			this.top = new BorderAttribute();
		}
		return this.top;
	}

	public BorderAttribute createAndGetRightBorder() {
		if (this.right == null) {
			this.right = new BorderAttribute();
		}
		return this.right;
	}

	public BorderAttribute createAndGetBottomBorder() {
		if (this.bottom == null) {
			this.bottom = new BorderAttribute();
		}
		return this.bottom;
	}

	public BorderAttribute createAndGetLeftBorder() {
		if (this.left == null) {
			this.left = new BorderAttribute();
		}
		return this.left;
	}

	public CompositeAttribute applyFontColor(FontColorAttribute.Acceptor acceptor) {
		if (this.fontColor != null) {
			acceptor.accept(this.fontColor);
		}
		return this;
	}

	public CompositeAttribute applyBackgroundColor(BackgroundColorAttribute.Acceptor acceptor) {
		if (this.backgroundColor != null) {
			acceptor.accept(this.backgroundColor);
		}
		return this;
	}

	public CompositeAttribute applyBorder(BorderAttribute.Acceptor acceptor) {
		acceptor.accept(common, top, right, bottom, left);
		return this;
	}
	
	public static <T extends ElementPropertyContainer<T>> 
	Consumer<T> createBorderAttribute(BorderAttribute borderAttr, BiConsumer<T , Border> setter) {
		if(borderAttr != null) {
			return borderAttr.createAttribute(setter);
		}
		else {
			return null;
		}
	}
}
