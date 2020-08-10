package pathFinder;

public class Point {
	
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean equals(Point other) {
		return x == other.x && y == other.y;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
