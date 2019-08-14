package me.chenqiang.pdf.xml.context;

@FunctionalInterface interface FloatFunction<T> {
	public void apply(T element, float value);
}