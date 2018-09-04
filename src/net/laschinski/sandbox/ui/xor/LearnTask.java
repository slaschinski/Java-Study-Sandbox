package net.laschinski.sandbox.ui.xor;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import net.laschinski.sandbox.ann.ANN;
import net.laschinski.sandbox.ann.ANNBuilder;

class LearnTask extends Task<Integer> {
	Series<Number, Number> series;

	LearnTask(Series<Number, Number> series) {
		this.series = series;
	}

	@Override
	protected Integer call() throws Exception {
		ANN ann = new ANNBuilder(2, 0.8d).alphaDecay(0.999d).lambda(0.0003d).buildAnn();
		ann.AddLayer(16, "LeakyReLU");
		ann.AddLayer(1, "Sigmoid");

		updateMessage("    Processing... ");

		ArrayList<Double> result;

		for (int i = 0; i < 100; i++) {
			double sumSquareError = 0;

			result = Train(ann, 1, 1, 0);
			sumSquareError += Math.pow((double) result.get(0) - 0, 2);
			if (i % 10 == 0) {
				System.out.println(" 1 1 " + result.get(0));
			}

			result = Train(ann, 1, 0, 1);
			sumSquareError += Math.pow((double) result.get(0) - 1, 2);
			if (i % 10 == 0) {
				System.out.println(" 1 0 " + result.get(0));
			}

			result = Train(ann, 0, 1, 1);
			sumSquareError += Math.pow((double) result.get(0) - 1, 2);
			if (i % 10 == 0) {
				System.out.println(" 0 1 " + result.get(0));
			}

			result = Train(ann, 0, 0, 0);
			sumSquareError += Math.pow((double) result.get(0) - 0, 2);
			if (i % 10 == 0) {
				System.out.println(" 0 0 " + result.get(0));
				System.out.println("SSE: " + sumSquareError);
			}

			final int iteration = i;
			final double SSE = sumSquareError;

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					series.getData().add(new XYChart.Data<Number, Number>(iteration, SSE));
				}
			});
		}

		updateMessage("    Done.  ");

		return 0;
	}

	private ArrayList<Double> Train(ANN ann, double i1, double i2, double o) {
		ArrayList<Double> inputs = new ArrayList<>();
		ArrayList<Double> outputs = new ArrayList<>();
		inputs.add(i1);
		inputs.add(i2);
		outputs.add(o);
		return (ann.Train(inputs, outputs));
	}
}