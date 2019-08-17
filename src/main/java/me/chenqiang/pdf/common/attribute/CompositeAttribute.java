package me.chenqiang.pdf.common.attribute;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.itextpdf.layout.ElementPropertyContainer;
import com.itextpdf.layout.borders.Border;

import me.chenqiang.pdf.sax.composer.component.PdfElementComposer;

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
	
	public <E extends ElementPropertyContainer<E>, S extends PdfElementComposer<E, S>> void setComposerAttribute(PdfElementComposer<E, S> composer) {
		if(this.fontColor != null) {
			composer.setAttribute(this.fontColor.createAttribute());
		}
		if(this.backgroundColor != null) {
			composer.setAttribute(this.backgroundColor.createAttribute());
		}
		if(this.common != null) {
			composer.setAttribute(this.common.<E>createAttribute(ElementPropertyContainer::setBorder));
		}
		if(this.top != null) {
			composer.setAttribute(this.top.<E>createAttribute(ElementPropertyContainer::setBorderTop));
		}
		if(this.right != null) {
			composer.setAttribute(this.right.<E>createAttribute(ElementPropertyContainer::setBorderRight));
		}
		if(this.bottom != null) {
			composer.setAttribute(this.bottom.<E>createAttribute(ElementPropertyContainer::setBorderBottom));
		}
		if(this.left != null) {
			composer.setAttribute(this.left.<E>createAttribute(ElementPropertyContainer::setBorderLeft));
		}
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
