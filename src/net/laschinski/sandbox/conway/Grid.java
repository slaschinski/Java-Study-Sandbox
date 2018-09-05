package net.laschinski.sandbox.conway;

import java.util.Random;

public class Grid {

	private boolean[][] cells;
	private CellSimulation cellAlgo;
	private CellSimulation cellANN;

	public Grid() throws Exception {
		initialize(10, 10);
	}

	public Grid(int x, int y) throws Exception {
		initialize(x, y);
	}
	
	private void initialize(int x, int y) throws Exception  {
		if (x < 2 || y < 2) {
			throw new Exception();
		}
		cells = new boolean[x][y];

		cellAlgo = new CellAlgorithm();
		cellANN = new CellANN();
	}

	public boolean[][] getCells() {
		return cells;
	}

	public void clear() {
		cells = new boolean[cells.length][cells[0].length];
	}

	public void fillRandom() {
		Random random = new Random();
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				cells[x][y] = random.nextBoolean();
			}
		}
	}

	public void setCell(int x, int y, boolean state) {
		cells[x][y] = state;
	}

	public void toggleCell(int x, int y) {
		if (cells[x][y]) {
			cells[x][y] = false;
		} else {
			cells[x][y] = true;
		}
	}
	
	public void calculateNextStateWithAlgorithm() {
		calculateNextState(cellAlgo);
	}
	
	public void calculateNextStateWithANN() {
		calculateNextState(cellANN);
	}

	private void calculateNextState(CellSimulation cellSim) {
		boolean[][] cellsNextState = new boolean[cells.length][cells[0].length];

		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				int left = x - 1;
				int right = x + 1;
				int top = y - 1;
				int bottom = y + 1;

				if (left < 0) {
					left = cells.length - 1;
				}
				if (right >= cells.length) {
					right = 0;
				}
				if (top < 0) {
					top = cells[x].length - 1;
				}
				if (bottom >= cells[x].length) {
					bottom = 0;
				}
				
				cellsNextState[x][y] = cellSim.calculateNewCellStatus(cells[left][top], cells[left][y],
						cells[left][bottom], cells[x][top], cells[x][y], cells[x][bottom], cells[right][top],
						cells[right][y], cells[right][bottom]);
			}
		}
		for (int i = 0; i < cells.length; i++) {
			System.arraycopy(cellsNextState[i], 0, cells[i], 0, cells[i].length);
		}
	}
	
	public void trainANN() {
		CellANN ann = (CellANN)cellANN;
		ann.fullyTrainAnn();
	}
}
