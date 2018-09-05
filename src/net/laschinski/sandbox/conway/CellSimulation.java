package net.laschinski.sandbox.conway;

public interface CellSimulation {
	public boolean calculateNewCellStatus(boolean topLeft, boolean left, boolean leftBottom, boolean top,
			boolean center, boolean bottom, boolean topRight, boolean right, boolean rightBottom);
}
