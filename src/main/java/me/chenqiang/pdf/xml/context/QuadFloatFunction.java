package me.chenqiang.pdf.xml.context;

@FunctionalInterface interface QuadFloatFunction<T> {
	public void apply(T element, float val1, float val2, float val3, float val4);
}