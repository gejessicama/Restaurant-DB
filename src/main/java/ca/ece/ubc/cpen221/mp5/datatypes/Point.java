package ca.ece.ubc.cpen221.mp5.datatypes;

public class Point {
	private double x;
	private double y;

	/*
	 * Abstraction Function: a point such as could be modeled on a 2D graph with x
	 * as the x coordinate and y as the y coordinate
	 * 
	 * Rep Invariant: fields are not null
	 */

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	/**
	 * calculates the distance from this to p
	 * 
	 * @param p
	 *            a point that is not null
	 * @return the distance from this to p
	 */
	public double distanceTo(Point p) {
		double xDiffsq = Math.pow(this.x - p.x, 2);
		double yDiffsq = Math.pow(this.y - p.y, 2);

		return Math.sqrt(xDiffsq + yDiffsq);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Point)
			return (this.getX() == ((Point) obj).getX() && this.getY() == ((Point) obj).getY());
		else
			return false;
	}

}
