package me.chenqiang.pdf.xml.context;

import com.itextpdf.layout.property.UnitValue;

@FunctionalInterface interface UnitValueFunction<T> {
	public void apply(T element, UnitValue value);
}