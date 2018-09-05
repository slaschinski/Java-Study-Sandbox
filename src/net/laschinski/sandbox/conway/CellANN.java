package net.laschinski.sandbox.conway;

import java.util.ArrayList;

import net.laschinski.sandbox.ann.ANN;
import net.laschinski.sandbox.ann.ANNBuilder;

public class CellANN implements CellSimulation {
	ANN ann;
	double[][] trainFigures = new double[30][9];
	double[] trainAnswers = new double[30];
	
	public CellANN() {
		ann = new ANNBuilder(9, 0.1d).buildAnn();
		ann.AddLayer(16, "LeakyReLU");
		ann.AddLayer(1, "Sigmoid");
		fillTrainFigures();
	}

	@Override
	public boolean calculateNewCellStatus(boolean topLeft, boolean left, boolean leftBottom, boolean top,
			boolean center, boolean bottom, boolean topRight, boolean right, boolean rightBottom) {
		ArrayList<Double> inputValues = new ArrayList<>();
		inputValues.add(topLeft ? 1.0d : 0.0d);
		inputValues.add(left ? 1.0d : 0.0d);
		inputValues.add(leftBottom ? 1.0d : 0.0d);
		inputValues.add(top ? 1.0d : 0.0d);
		inputValues.add(center ? 1.0d : 0.0d);
		inputValues.add(bottom ? 1.0d : 0.0d);
		inputValues.add(topRight ? 1.0d : 0.0d);
		inputValues.add(right ? 1.0d : 0.0d);
		inputValues.add(rightBottom ? 1.0d : 0.0d);
		
		double returnValue = ann.Predict(inputValues).get(0);
		
		if (returnValue > 0.5d) {
			return true;
		}
		return false;
	}
	
	public void fullyTrainAnn() {
		double sse;
		for (int i = 0; i < 1000; i++) {
			sse = trainByAlgorithm();
			if (i % 10 == 0) {
				System.out.println("SSE: " + sse);
			}
		}
		// does not quite work - probably too few examples and/or errors in data
		/*for (int i = 0; i < 10000; i++) {
			sse = trainEpoch();
			if (i % 100 == 0) {
				System.out.println("SSE: " + sse);
			}
		}*/
	}

	public double trainEpoch() {
		ArrayList<Double> inputValues = new ArrayList<>();
		ArrayList<Double> desiredOutputValues = new ArrayList<>();
		ArrayList<Double> outputValues;
		double sumSquareError = 0.0d;
		
		for (int i = 0; i < trainFigures.length; i++) {
			inputValues.clear();
			desiredOutputValues.clear();
			for (double input : trainFigures[i]) {
				inputValues.add(input);
			}
			desiredOutputValues.add(trainAnswers[i]);
			outputValues = ann.Train(inputValues, desiredOutputValues);
			sumSquareError += Math.pow((double) outputValues.get(0) - desiredOutputValues.get(0), 2);
		}
		
		return sumSquareError;
	}
	
	public double trainByAlgorithm() {
		CellAlgorithm cellAlgo = new CellAlgorithm();
		double sumSquareError = 0.0d;
		
		for (int i = 0; i < 500; i++) {
			boolean[] inputs = new boolean[9];
			ArrayList<Double> inputValues = new ArrayList<>();
			ArrayList<Double> desiredOutputValues = new ArrayList<>();
			ArrayList<Double> outputValues;
			
			for (int j = 0; j < 9; j++) {
				double rand = Math.random();
				int value = Math.round((float)rand);
				inputs[j] = value == 1;
				inputValues.add((double)value);
			}
			boolean output = cellAlgo.calculateNewCellStatus(inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5], inputs[6], inputs[7], inputs[8]);
			desiredOutputValues.add(output ? 1.0d : 0.0d);
			
			outputValues = ann.Train(inputValues, desiredOutputValues);
			sumSquareError += Math.pow((double) outputValues.get(0) - desiredOutputValues.get(0), 2);
		}
		
		return sumSquareError;
		
	}
	
	private void fillTrainFigures() {
		trainFigures[0] = new double[]
				{	0, 0, 0,
					0, 0, 0,
					0, 0, 0
				};
		trainAnswers[0] = 0;
		
		trainFigures[1] = new double[]
				{	0, 0, 0,
					0, 1, 0,
					0, 0, 0
				};
		trainAnswers[1] = 0;
		
		trainFigures[2] = new double[]
				{	1, 0, 0,
					0, 1, 0,
					0, 0, 0
				};
		trainAnswers[2] = 0;
		
		trainFigures[3] = new double[]
				{	0, 1, 0,
					0, 1, 0,
					0, 0, 0
				};
		trainAnswers[3] = 0;
		
		trainFigures[4] = new double[]
				{	0, 0, 1,
					0, 1, 0,
					0, 0, 0
				};
		trainAnswers[4] = 0;
		
		trainFigures[5] = new double[]
				{	0, 0, 0,
					1, 1, 0,
					0, 0, 0
				};
		trainAnswers[5] = 0;
		
		trainFigures[6] = new double[]
				{	0, 0, 0,
					0, 1, 1,
					0, 0, 0
				};
		trainAnswers[6] = 0;
		
		trainFigures[7] = new double[]
				{	0, 0, 0,
					0, 0, 0,
					0, 1, 0
				};
		trainAnswers[7] = 0;
		
		trainFigures[8] = new double[]
				{	0, 0, 0,
					0, 0, 0,
					0, 0, 1
				};
		trainAnswers[8] = 0;
		
		trainFigures[9] = new double[]
				{	1, 0, 0,
					0, 1, 0,
					0, 0, 1
				};
		trainAnswers[9] = 1;
		
		trainFigures[10] = new double[]
				{	0, 0, 1,
					0, 1, 0,
					1, 1, 0
				};
		trainAnswers[10] = 1;
		
		trainFigures[11] = new double[]
				{	1, 0, 1,
					0, 1, 1,
					0, 0, 0
				};
		trainAnswers[11] = 1;
		
		trainFigures[12] = new double[]
				{	0, 0, 1,
					0, 1, 0,
					0, 1, 1
				};
		trainAnswers[12] = 1;
		
		trainFigures[13] = new double[]
				{	1, 0, 1,
					0, 0, 0,
					0, 0, 1
				};
		trainAnswers[13] = 1;
		
		trainFigures[14] = new double[]
				{	0, 0, 0,
					0, 0, 1,
					0, 1, 1
				};
		trainAnswers[14] = 1;
		
		trainFigures[15] = new double[]
				{	1, 0, 0,
					1, 0, 0,
					0, 0, 1
				};
		trainAnswers[15] = 1;
		
		trainFigures[16] = new double[]
				{	0, 0, 0,
					1, 0, 0,
					1, 0, 1
				};
		trainAnswers[16] = 1;
		
		trainFigures[17] = new double[]
				{	0, 0, 1,
					1, 0, 0,
					1, 0, 1
				};
		trainAnswers[17] = 0;
		
		trainFigures[18] = new double[]
				{	0, 0, 0,
					1, 0, 0,
					1, 1, 1
				};
		trainAnswers[18] = 0;
		
		trainFigures[19] = new double[]
				{	0, 1, 0,
					1, 1, 0,
					1, 0, 1
				};
		trainAnswers[19] = 0;
		
		trainFigures[20] = new double[]
				{	1, 0, 1,
					1, 1, 0,
					0, 0, 1
				};
		trainAnswers[20] = 0;
		
		trainFigures[21] = new double[]
				{	1, 0, 0,
					0, 0, 1,
					1, 1, 1
				};
		trainAnswers[21] = 0;
		
		trainFigures[22] = new double[]
				{	1, 1, 1,
					1, 0, 1,
					1, 1, 1
				};
		trainAnswers[22] = 0;
		
		trainFigures[23] = new double[]
				{	1, 0, 1,
					1, 0, 1,
					0, 0, 0
				};
		trainAnswers[23] = 0;
		
		trainFigures[24] = new double[]
				{	1, 1, 1,
					1, 0, 0,
					1, 0, 1
				};
		trainAnswers[24] = 0;
		
		trainFigures[25] = new double[]
				{	1, 1, 0,
					1, 0, 1,
					1, 1, 0
				};
		trainAnswers[25] = 0;
		
		trainFigures[26] = new double[]
				{	1, 0, 0,
					0, 1, 1,
					1, 1, 1
				};
		trainAnswers[26] = 0;
		
		trainFigures[27] = new double[]
				{	1, 1, 0,
					1, 1, 1,
					0, 1, 1
				};
		trainAnswers[27] = 0;
		
		trainFigures[28] = new double[]
				{	1, 0, 1,
					0, 1, 0,
					1, 1, 0
				};
		trainAnswers[28] = 0;
		
		trainFigures[29] = new double[]
				{	0, 1, 1,
					1, 1, 1,
					1, 0, 0
				};
		trainAnswers[29] = 0;
	}
}
