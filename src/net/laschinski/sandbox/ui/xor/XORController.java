package net.laschinski.sandbox.ui.xor;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import net.laschinski.sandbox.ann.ANN;
import net.laschinski.sandbox.ann.ANNBuilder;

class TestTask extends Task<Integer> {
    Series<Number, Number> series;
    
    TestTask(Series<Number, Number> series) {
    	this.series = series;
    }
    
    @Override
    protected Integer call() throws Exception {
		ANN ann = new ANNBuilder(2, 0.8d)
				.alphaDecay(0.999d)
				.lambda(0.0003d)
				.buildAnn();
        ann.AddLayer(16, "LeakyReLU");
        ann.AddLayer(1, "Sigmoid");
	   
		updateMessage("    Processing... ");
		
		ArrayList<Double> result;
        
        for (int i = 0; i < 100; i++) {
        	double sumSquareError = 0;
            
            result = Train(ann, 1, 1, 0);
            sumSquareError += Math.pow((double)result.get(0) - 0, 2);
            if (i % 10 == 0) {
            	System.out.println(" 1 1 " + result.get(0));
            }
            
            result = Train(ann, 1, 0, 1);
            sumSquareError += Math.pow((double)result.get(0) - 1, 2);
            if (i % 10 == 0) {
            	System.out.println(" 1 0 " + result.get(0));
            }
            
            result = Train(ann, 0, 1, 1);
            sumSquareError += Math.pow((double)result.get(0) - 1, 2);
            if (i % 10 == 0) {
            	System.out.println(" 0 1 " + result.get(0));
            }
            
            result = Train(ann, 0, 0, 0);
            sumSquareError += Math.pow((double)result.get(0) - 0, 2);
            if (i % 10 == 0) {
                System.out.println(" 0 0 " + result.get(0));
            	System.out.println("SSE: " + sumSquareError);
            }
            
            final int iteration = i;
            final double SSE = sumSquareError;
            
            Platform.runLater(new Runnable() {
            	@Override public void run() {
            		series.getData().add(new XYChart.Data<Number, Number>(iteration, SSE));
            	}
            });
        }
        
		updateMessage("    Done.  ");
		
		return 0;
    }
    
	private ArrayList<Double> Train(ANN ann, double i1, double i2, double o)
    {
        ArrayList<Double> inputs = new ArrayList<>();
        ArrayList<Double> outputs = new ArrayList<>();
        inputs.add(i1);
        inputs.add(i2);
        outputs.add(o);
        return (ann.Train(inputs, outputs));
    }
}

public class XORController implements Initializable {
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }
    
    @FXML
    private XYChart<Number, Number> chart;
    
	@FXML
	private void buttonPressed(ActionEvent event) {

        XYChart.Series<Number, Number> series = new Series<Number, Number>();
        chart.getData().add(series);
        series.setName("Testseries");
        
        TestTask task = new TestTask(series);

        task.setOnRunning((succeesesEvent) -> {
        });

        task.setOnSucceeded((succeededEvent) -> {
        });

        ExecutorService executorService
           = Executors.newFixedThreadPool(1);
        executorService.execute(task);
        executorService.shutdown();
	}
}
