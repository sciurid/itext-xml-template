package me.chenqiang.pdf.common.attribute;

@FunctionalInterface interface BiFloatFunction<T> {
	public void apply(T element, float val1, float val2);
}