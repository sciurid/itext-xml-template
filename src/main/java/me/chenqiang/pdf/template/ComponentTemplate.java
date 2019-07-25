package me.chenqiang.pdf.template;

import com.itextpdf.layout.element.AbstractElement;

public interface ComponentTemplate<T extends AbstractElement<T>> {
	public void process(T container);
//	public void setParamValue(String value);
//	public void setDisplay(boolean display);
}
