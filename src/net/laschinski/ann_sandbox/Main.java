package net.laschinski.ann_sandbox;

import java.util.ArrayList;

import net.laschinski.ann_sandbox.ann.Ann;
import net.laschinski.ann_sandbox.ann.AnnBuilder;

public class Main {
	static Ann ann;
    static double sumSquareError = 0;
    static float delta = 0;
    static int i = 0;
    
	public static void main(String[] args) {
		ann = new AnnBuilder(2, 0.08d)
				.alphaDecay(0.999d)
				.lambda(0.0003d)
				.buildAnn();
        ann.AddLayer(32, "LeakyReLU");
        ann.AddLayer(1, "Sigmoid");

        ArrayList<Double> result;
        
        for (int i = 0; i < 20000; i++) {
            sumSquareError = 0;
            
            result = Train(1, 1, 0);
            sumSquareError += Math.pow((double)result.get(0) - 0, 2);
            if (i % 1000 == 0) {
            	System.out.println(" 1 1 " + result.get(0));
            }
            
            result = Train(1, 0, 1);
            sumSquareError += Math.pow((double)result.get(0) - 1, 2);
            if (i % 1000 == 0) {
            	System.out.println(" 1 0 " + result.get(0));
            }
            
            result = Train(0, 1, 1);
            sumSquareError += Math.pow((double)result.get(0) - 1, 2);
            if (i % 1000 == 0) {
            	System.out.println(" 0 1 " + result.get(0));
            }
            
            result = Train(0, 0, 0);
            sumSquareError += Math.pow((double)result.get(0) - 0, 2);
            if (i % 1000 == 0) {
                System.out.println(" 0 0 " + result.get(0));
            	System.out.println("SSE: " + sumSquareError);
            }
        }
	}

	static ArrayList<Double> Train(double i1, double i2, double o)
    {
        ArrayList<Double> inputs = new ArrayList<>();
        ArrayList<Double> outputs = new ArrayList<>();
        inputs.add(i1);
        inputs.add(i2);
        outputs.add(o);
        return (ann.Train(inputs, outputs));
    }
}
