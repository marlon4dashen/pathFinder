package pathFinder;

public class Squares {
	
	private int x;
	private int y;
	private int val; 
	
	public Squares(int y, int x, int rowNum) {
		this.x = x;
		this.y = y;
		this.val = y * rowNum + x;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getVal() {
		return val;
	}
	
	public String toString() {
		String tmp = "";
		tmp += "x: " + x + ", y = " + y + "val = " + val;
		return tmp;
	}
	
	public boolean equals(Squares other) {
		return x == other.x && y == other.y && val == other.val;
	}
}
