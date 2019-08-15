package me.chenqiang.pdf.common.attribute;

@FunctionalInterface interface TriFloatFunction<T> {
	public void apply(T element, float val1, float val2, float val3);
}