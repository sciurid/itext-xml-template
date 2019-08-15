package me.chenqiang.pdf.xml.context;

@FunctionalInterface interface BiFloatFunction<T> {
	public void apply(T element, float val1, float val2);
}