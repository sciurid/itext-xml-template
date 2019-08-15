package me.chenqiang.pdf.xml.context;

@FunctionalInterface interface BooleanFunction<T> {
	public void apply(T element, boolean value);
}