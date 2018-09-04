package net.laschinski.sandbox.ui.xor;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class XORController implements Initializable {

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	}

	@FXML
	private XYChart<Number, Number> chart;

	@FXML
	private void buttonPressed(ActionEvent event) {

		XYChart.Series<Number, Number> series = new Series<Number, Number>();
		chart.getData().add(series);

		LearnTask task = new LearnTask(series);

		task.setOnRunning((succeesesEvent) -> {
		});

		task.setOnSucceeded((succeededEvent) -> {
		});

		ExecutorService executorService = Executors.newFixedThreadPool(1);
		executorService.execute(task);
		executorService.shutdown();
	}
}
