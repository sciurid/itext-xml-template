package me.chenqiang.pdf.common.utils;

public final class LengthUnit {
	private LengthUnit()  {}
	
	public static float mm2pt(float mm) {
		return mm * 72 / 25.4f;
	}
	
	public static float cm2pt(float mm) {
		return mm * 72 / 2.54f;
	}
	
	public static float inch2pt(float inch) {
		return inch * 72;
	}
}
