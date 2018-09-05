package net.laschinski.sandbox.ui.conway;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import net.laschinski.sandbox.conway.CellANN;

public class TrainTask  extends Task<Integer> {
	Series<Number, Number> series;
	CellANN cellANN;

	TrainTask(Series<Number, Number> series, CellANN cellANN) {
		this.series = series;
		this.cellANN = cellANN;
	}

	@Override
	protected Integer call() throws Exception {
		for (int i = 0; i < 100; i++) {
			final double sse = cellANN.trainByAlgorithm(500);
			final int iteration = i;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					series.getData().add(new XYChart.Data<Number, Number>(iteration, sse));
				}
			});
		}
		// does not quite work - probably too few examples and/or errors in data
		/*for (int i = 0; i < 10000; i++) {
			final double sse = cellANN.trainEpoch();
			if (i % 100 == 0) {
				System.out.println("SSE: " + sse);
			}
		}*/
		return 0;
	}
}
