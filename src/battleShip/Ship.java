package battleShip;

public class Ship
{
	public Ship(int length, int row, int col, boolean vertical, String name)
	{
		this.length = length;
		this.row = row;
		this.col = col;
		this.vertical = vertical;
		this.name = name;
		sunk = false;
	}
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public boolean isVertical() {
		return vertical;
	}
	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSunk() {
		return sunk;
	}
	public void setSunk(boolean sunk) {
		this.sunk = sunk;
	}

	private int length;
	private int row;
	private int col;
	private boolean vertical;
	private String name;
	private boolean sunk;
}