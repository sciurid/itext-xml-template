package me.chenqiang.pdf.attribute;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.ElementPropertyContainer;

public class BackgroundColorAttribute {
	protected Color fontColor = DeviceRgb.BLACK;
	protected float opacity = 1.0f;
	
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public <T extends ElementPropertyContainer<T>> void apply(T element) {
		element.setBackgroundColor(this.fontColor, this.opacity);
	}
	
	public static interface Acceptor {
		public void accept(BackgroundColorAttribute fontColorAttr);
	}
}
