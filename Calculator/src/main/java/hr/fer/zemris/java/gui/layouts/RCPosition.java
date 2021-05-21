package hr.fer.zemris.java.gui.layouts;

/**
 * Razred predstavlja model pozicija komponenata unutar layouta
 * 
 * @author Hrvoje
 */
public class RCPosition {
	private int row;
	private int column;
	
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	
	/**
	 * Metoda parsira predani text i vraca novi primjerak RCPosition
	 * @param text predani text
	 * @return novi RCPosition objekt
	 */
	public static RCPosition parse(String text) {
		String[] params = text.split(",");
		int redak = Integer.parseInt(params[0]);
		
		String stupacStr = params[1].contains(" ") ? params[1].substring(1, params[1].length()) : params[1];
		int stupac = Integer.parseInt(stupacStr);
		
		return new RCPosition(redak, stupac);
	}
}
