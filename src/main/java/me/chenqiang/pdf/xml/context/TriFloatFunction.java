package me.chenqiang.pdf.xml.context;

@FunctionalInterface interface TriFloatFunction<T> {
	public void apply(T element, float val1, float val2, float val3);
}