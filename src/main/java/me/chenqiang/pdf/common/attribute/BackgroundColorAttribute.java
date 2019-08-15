package me.chenqiang.pdf.common.attribute;

import java.util.function.Consumer;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.layout.ElementPropertyContainer;

public class BackgroundColorAttribute {
	protected Color backgroundColor = null;
	protected Float opacity = null;
	
	public void setFontColor(Color fontColor) {
		this.backgroundColor = fontColor;
	}
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public <T extends ElementPropertyContainer<T>> void apply(T element) {
		if(this.backgroundColor != null) {
			if(this.opacity != null) {
				element.setBackgroundColor(this.backgroundColor, this.opacity);
			}
			else {
				element.setBackgroundColor(this.backgroundColor);
			}
		}
	}
	
	public <T extends ElementPropertyContainer<T>> Consumer<T> createAttribute() {
		return element -> this.apply(element);
	}	
}
