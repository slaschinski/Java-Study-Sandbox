package net.laschinski.sandbox.ui.conway;

import javafx.concurrent.Task;
import net.laschinski.sandbox.conway.Grid;

class SimulationTask extends Task<Integer> {
	public boolean running = true;
	private ConwayController controller;
	private Grid conwayGrid;
	private boolean useANN;
	
	SimulationTask(ConwayController controller, Grid conwayGrid, boolean useANN) {
		this.controller = controller;
		this.conwayGrid = conwayGrid;
		this.useANN = useANN;
	}

	@Override
	protected Integer call() throws Exception {
		while (running) {
			if (useANN) {
				conwayGrid.calculateNextStateWithANN();
			} else {
				conwayGrid.calculateNextStateWithAlgorithm();
			}
			controller.drawCells();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

}
