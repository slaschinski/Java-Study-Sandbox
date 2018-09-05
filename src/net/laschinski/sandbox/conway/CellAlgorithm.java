package net.laschinski.sandbox.conway;

public class CellAlgorithm implements CellSimulation {

	@Override
	public boolean calculateNewCellStatus(boolean topLeft, boolean left, boolean leftBottom, boolean top,
			boolean center, boolean bottom, boolean topRight, boolean right, boolean rightBottom) {

		int countCells = 0;
		if (topLeft) {
			countCells++;
		}
		if (left) {
			countCells++;
		}
		if (leftBottom) {
			countCells++;
		}
		if (top) {
			countCells++;
		}
		if (bottom) {
			countCells++;
		}
		if (topRight) {
			countCells++;
		}
		if (right) {
			countCells++;
		}
		if (rightBottom) {
			countCells++;
		}
		
		if (countCells < 2 || countCells > 3) {
			return false;
		} else if (countCells == 2) {
			return center;
		} // countCells == 3
		return true;
	}

}
