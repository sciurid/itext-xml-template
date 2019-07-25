package me.chenqiang.pdf;

import java.util.function.Function;

import org.junit.Test;

public class FunctionBindingTest {
	
	int delta = 0;
	
	public static int add(int x, int y) {
		return x + y;
	}
	
	public static void doIt(Function<Integer, Integer> func, int input) {
		System.out.println(func.apply(input));
	}
	
	@Test
	public void doBindingTest() {
		Function<Integer, Integer> addDelta = x -> add(this.delta, x);
		
		doIt(addDelta, 10);
		this.delta = 2;
		doIt(addDelta, 10);
	}

}
