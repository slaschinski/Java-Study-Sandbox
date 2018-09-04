package net.laschinski.sandbox.ui.conway;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import net.laschinski.sandbox.conway.Grid;

public class ConwayController implements Initializable {
	private static int xSize = 25;
	private static int ySize = 25;
	private static int cellSize = 20;
	private Grid conwayGrid;
	private GraphicsContext gc;
	private SimulationTask simulationTask;
	private int lastMousePosition[] = {-1, -1}; 
	
	@FXML
	private Button startStopButton;
	
	@FXML
	private Canvas canvas;
	
	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		try {
			conwayGrid = new Grid(xSize, ySize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
		drawGrid();
		drawCells();
	}
	
	@FXML
	private void toggleCell(MouseEvent event) {
		int x = (int)Math.floor(event.getX() / cellSize);
		int y = (int)Math.floor(event.getY() / cellSize);
		if (x >= xSize) {
			x = xSize - 1;
		} else if (x < 0) {
			x = 0;
		}
		if (y >= ySize) {
			y = ySize - 1;
		} else if (y < 0) {
			y = 0;
		}
		

		if (x != lastMousePosition[0] || y != lastMousePosition[1]) {
			lastMousePosition[0] = x;
			lastMousePosition[1] = y;
			if (event.getButton() == MouseButton.PRIMARY) {
				conwayGrid.setCell(x, y, true);
			} else if (event.getButton() ==  MouseButton.SECONDARY) {
				conwayGrid.setCell(x, y, false);
			}
			drawCells();
		}
	}

	@FXML
	private void startStopSimulation(ActionEvent event) {
		if (simulationTask == null || !simulationTask.running) {
			startStopButton.setText("Stop simulation");
			simulationTask = new SimulationTask(this, conwayGrid);
	
			ExecutorService executorService = Executors.newFixedThreadPool(1);
			executorService.execute(simulationTask);
			executorService.shutdown();
		} else {
			startStopButton.setText("Start simulation");
			simulationTask.running = false;
		}
	}
	
	public void drawCells() {
		boolean[][] cells = conwayGrid.getCells();
		
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				gc.clearRect(x * cellSize + 3, y * cellSize + 3, cellSize - 3, cellSize - 3);
				if (cells[x][y]) {
					gc.fillOval(x * cellSize + 3, y * cellSize + 3, cellSize - 4, cellSize -4);
				}
			}
		}
	}

	private void drawGrid() {
		for (int x = 0; x <= xSize; x++) {
	        gc.setLineWidth(1);
	        gc.strokeLine(x * cellSize + 1, 0, x * cellSize + 1, ySize * cellSize + 1);
		}
		for (int y = 0; y <= ySize; y++) {
	        gc.setLineWidth(1);
	        gc.strokeLine(0, y * cellSize + 1, xSize * cellSize + 1, y * cellSize + 1);
		}
	}
}