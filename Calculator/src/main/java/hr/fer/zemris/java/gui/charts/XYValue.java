package hr.fer.zemris.java.gui.charts;

/**
 * Razred predstavlja model tocaka koordinatnog sustava
 * 
 * @author Hrvoje
 */
public class XYValue {

	private int x;
	private int y;
	
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
}
