package me.chenqiang.pdf.common.attribute;

import com.itextpdf.kernel.colors.Color;

@FunctionalInterface interface ColorFunction<T> {
	public void apply(T element, Color color);
}