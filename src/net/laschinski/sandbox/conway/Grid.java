package net.laschinski.sandbox.conway;

import java.util.Random;

public class Grid {

	private boolean[][] cells;

	public Grid() {
		cells = new boolean[10][10];
	}

	public Grid(int x, int y) throws Exception {
		if (x < 2 || y < 2) {
			throw new Exception();
		}
		cells = new boolean[x][y];
	}
	
	public boolean[][] getCells() {
		return cells;
	}
	
	public void fillRandom() {
		Random random = new Random();
	    for(int x = 0; x < cells.length; x++) {
	    	for(int y = 0; y < cells[x].length; y++) {
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
	
	public void calculateNextState() {
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

				int countCells = 0;
				if (cells[left][top]) {
					countCells++;
				}
				if (cells[left][y]) {
					countCells++;
				}
				if (cells[left][bottom]) {
					countCells++;
				}
				if (cells[x][top]) {
					countCells++;
				}
				if (cells[x][bottom]) {
					countCells++;
				}
				if (cells[right][top]) {
					countCells++;
				}
				if (cells[right][y]) {
					countCells++;
				}
				if (cells[right][bottom]) {
					countCells++;
				}
				
				if (countCells < 2 || countCells > 3) {
					cellsNextState[x][y] = false;
				} else if (countCells == 2) {
					cellsNextState[x][y] = cells[x][y];
				} else { // countCells == 3
					cellsNextState[x][y] = true;
				}
			}
		}
		for (int i = 0; i < cells.length; i++) {
			System.arraycopy(cellsNextState[i], 0, cells[i], 0, cells[i].length);
		}
	}
}
