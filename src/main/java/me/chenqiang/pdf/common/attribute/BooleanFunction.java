package me.chenqiang.pdf.common.attribute;

@FunctionalInterface interface BooleanFunction<T> {
	public void apply(T element, boolean value);
}