package Fonts;

public class Point {

	public int x = 0;
	public int y = 0;
	public boolean onCurve = true;
	public boolean endOfContour = false;
	public boolean touched = false;

	public Point(int x, int y, boolean onCurve, boolean endOfContour) {
		this.x = x;
		this.y = y;
		this.onCurve = onCurve;
		this.endOfContour = endOfContour;
	}
}
