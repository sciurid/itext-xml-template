package me.chenqiang.pdf.common.attribute;

import java.util.function.Consumer;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.layout.ElementPropertyContainer;

public class FontColorAttribute {
	protected Color fontColor = null;
	protected Float opacity = null;
	
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public <T extends ElementPropertyContainer<T>> void apply(T element) {
		if(this.fontColor != null) {
			if(this.opacity != null) {
				element.setFontColor(this.fontColor, this.opacity);
			}
			else {
				element.setFontColor(this.fontColor);
			}
		}
		
	}
	
	public <T extends ElementPropertyContainer<T>> Consumer<T> createAttribute() {
		return element -> this.apply(element);
	}
}
