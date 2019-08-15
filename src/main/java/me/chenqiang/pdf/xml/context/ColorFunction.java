package me.chenqiang.pdf.xml.context;

import com.itextpdf.kernel.colors.Color;

@FunctionalInterface interface ColorFunction<T> {
	public void apply(T element, Color color);
}