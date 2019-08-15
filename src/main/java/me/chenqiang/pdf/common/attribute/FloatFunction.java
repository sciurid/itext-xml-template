package me.chenqiang.pdf.common.attribute;

@FunctionalInterface interface FloatFunction<T> {
	public void apply(T element, float value);
}