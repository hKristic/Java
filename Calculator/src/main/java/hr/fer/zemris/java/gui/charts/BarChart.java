package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Razred predstavlja model podataka koji se nalaze na grafu
 * 
 * @author Hrvoje
 */
public class BarChart {
	
	private List<XYValue> list;
	private String xDesc;
	private String yDesc;
	private int minY;
	private int maxY;
	private int delta;
	
	public List<XYValue> getList() {
		return list;
	}

	public String getxDesc() {
		return xDesc;
	}

	public String getyDesc() {
		return yDesc;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getDelta() {
		return delta;
	}

	public BarChart(List<XYValue> list, String xDesc, String yDesc, int minY, int maxY, int delta) {
		if (minY < 0) throw new IllegalArgumentException("Minimalni y ne smije biti manji od 0");
		if (maxY <= minY) throw new IllegalArgumentException("Maksimalni y mora biti veci od minimalnog");
		
		for (XYValue data : list) {
			if (data.getY() < minY) throw new IllegalArgumentException("Podaci ne smiju biti manji od 0");
		}
		
		this.list = list;
		this.xDesc = xDesc;
		this.yDesc = yDesc;
		this.minY = minY;
		this.maxY = maxY;
		this.delta = delta;
	}
	
}
