package net.laschinski.ann_sandbox.utils;

public class Random {
	public static double Range(double min, double max) {
		double random = min + Math.random() * (max - min);
		return random;
	}
	
}
