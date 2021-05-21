package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

/**
 * Razred predstavlja model komponente koja prikazuje: graf, opis x i y osi i vrijednosti grafa
 * 
 * @author Hrvoje
 */
public class BarChartComponent extends JComponent{

	private static final long serialVersionUID = 1L;

	private BarChart chart;
	
	private int xySpace = 100;	//dodatni prostor 
	
	public BarChartComponent(BarChart chart) {
		this.chart = chart;
	}
	
	public void setChart(BarChart chart) {
		this.chart = chart;
		repaint();
	}
	@Override
	protected void paintComponent(Graphics g) {
		Dimension dimenzije = getSize();
		Insets ins = getInsets();
		
		int chartHeight = dimenzije.height - ins.bottom - ins.top - 2 * xySpace;
		int chartWidth = dimenzije.width - ins.left - ins.right - 2 * xySpace;
		
		int numberOfRows = (chart.getMaxY() - chart.getMinY()) / chart.getDelta();
		int numberOfColumns = chart.getList().size();
		
		int pretinacHeight = chartHeight / numberOfRows;
		int pretinacWidth = chartWidth / numberOfColumns;
		
		g.setColor(getBackground());
		g.fillRect(0, 0, dimenzije.width, dimenzije.height);
		
		drawYCord(g, numberOfRows, pretinacHeight, numberOfColumns, pretinacWidth);
		drawXCord(g, numberOfRows, pretinacHeight, numberOfColumns, pretinacWidth, chart.getDelta(), chartWidth);
		drawGraph(g, chartHeight, chartWidth, pretinacHeight, pretinacWidth, numberOfRows, chart.getDelta());
		writeYDesc(g, chartHeight);
	}

	/**
	 * Metoda crta y os i n njoj paralelnih duzina, gdje je n broj stupaca grafa
	 * 
	 * @param g grafika
	 * @param numberOfRows broj redaka grafa
	 * @param pretinacHeight visina jedne celije grafa
	 * @param numberOfColumns broj stupaca grafa
	 * @param pretinacWidth sirina celije grafa
	 */
	public void drawYCord(Graphics g, int numberOfRows, int pretinacHeight, int numberOfColumns, int pretinacWidth) {
		g.setColor(Color.BLACK);
		g.drawLine(xySpace, xySpace, xySpace, numberOfRows * pretinacHeight + xySpace);
		
		int j = 1;
		
		int x = pretinacWidth  + xySpace;
		for (int i = 0; i < numberOfColumns; i++, j++) {
			g.drawLine(x, xySpace, x, numberOfRows * pretinacHeight + xySpace);
			
			g.drawString(String.valueOf(j), (int) (x + pretinacWidth * 0.5 - pretinacWidth)
					, numberOfRows * pretinacHeight + xySpace + 20);
			
			x += pretinacWidth;
		}
	}
	
	/**
	 * Metoda crta x os i n njoj paralelnih duzina, gdje je n broj redaka grafa
	 * 
	 * @param g grafika
	 * @param numberOfRows broj redaka grafa
	 * @param pretinacHeight visina jedne celije grafa
	 * @param numberOfColumns broj stupaca grafa
	 * @param pretinacWidth sirina celije grafa
	 * @param delta razlika dviju susjednih vrijednosti grafa
	 * @param chartWidth sirina cijelog grafa
	 */
	public void drawXCord(Graphics g, int numberOfRows, int pretinacHeight,
			int numberOfColumns, int pretinacWidth, int delta, int chartWidth) {
		
		g.setColor(Color.BLACK);
		g.drawLine(xySpace, numberOfRows * pretinacHeight + xySpace, numberOfColumns * pretinacWidth + xySpace, numberOfRows * pretinacHeight + xySpace);
		
		int j = 0;
		
		int y = numberOfRows * pretinacHeight + xySpace;
		for (int i = 0; i <= numberOfRows; i++, j += delta) {
			g.drawLine(xySpace, y, numberOfColumns * pretinacWidth + xySpace, y);
			
			g.drawString(String.valueOf(j), xySpace - 20, y + 5);
			
			y -= pretinacHeight;
		}
		
		g.drawString(chart.getxDesc(), chartWidth / 2, numberOfRows * pretinacHeight + xySpace + 50);
	}
	
	/**
	 * Metoda ispisuje opis y osi, ispisuje se pod kutem od 90
	 * 
	 * @param g grafika
	 * @param chartHeight visina cijelog grafa
	 */
	public void writeYDesc(Graphics g, int chartHeight) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		g2d.setTransform(at);
		g2d.drawString(chart.getyDesc(), -chartHeight / 2 - xySpace - 20, xySpace - 50);
		at.rotate(Math.PI / 2);
		g2d.setTransform(at);
	}
	
	/**
	 * Metoda crta stupce grafa
	 * 
	 * @param g grafika
	 * @param chartHeight visina cijelog grafa
	 * @param chartWidth sirina cijelog grafa
	 * @param pretinacHeight visina celije grafa
	 * @param pretinacWidth sirina celije grafa
	 * @param numberOfRows broj redaka grafa
	 * @param delta razlika dviju susjednih vrijednosti grafa

	 */
	public void drawGraph(Graphics g, int chartHeight, int chartWidth, int pretinacHeight,
			int pretinacWidth, int numberOfRows, int delta) {
		
		g.setColor(Color.RED.darker());
		for (XYValue startCord : chart.getList()) {
			int y = startCord.getY();
			int x = (startCord.getX() - 1) * pretinacWidth;
			int xCord = x + xySpace + 1;
			int yCord = xySpace + pretinacHeight * numberOfRows - ((y / delta) * pretinacHeight);
			g.fillRect(xCord, yCord, pretinacWidth - 1, (y / delta) * pretinacHeight);
		}
	}
}
