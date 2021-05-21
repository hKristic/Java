package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Razred predstavlja layoutManagera za razmjestaj komponenata za kalkulator
 * 
 * @author Hrvoje
 */
public class CalcLayout implements LayoutManager2 {

	private static int numberOfRows = 5;
	private static int minRow = 1;
	private static int numberOfColumns = 7;
	private static int minColumn = 1;
	
	private int gap;	//razmak izmedu gumba 
	private Map<RCPosition, Component> elements;
	
	public CalcLayout(int gap) {
		this.gap = gap;
		elements = new HashMap<>();
	}
	
	public CalcLayout() {
		this(0);
	}

	public int getGap() {
		return gap;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		elements.forEach((k, v) -> {
			if (v.equals(comp)) elements.remove(k);
		});			
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return layoutSize(parent, a -> a.getPreferredSize());
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return layoutSize(parent, a -> a.getMinimumSize());
	}

	@Override
	public void layoutContainer(Container parent) {
		int columnWidth = (int) Math.floor((parent.getWidth() - (numberOfColumns - 1) * gap) / numberOfColumns);
		int rowHeight = (int) Math.floor((parent.getHeight() - (numberOfRows - 1) * gap) / numberOfRows);
		
		for (Map.Entry<RCPosition, Component> entry : elements.entrySet()) {
			int redak = entry.getKey().getRow();
			int stupac = entry.getKey().getColumn();
			
			if (redak == 1 && stupac == 1) {
				entry.getValue().setBounds((stupac - 1) * (columnWidth + gap), (redak - 1) * (rowHeight + gap),
						5 * columnWidth + 4 * gap, rowHeight);
			}
			else {
				entry.getValue().setBounds((stupac - 1) * (columnWidth + gap), (redak - 1) * (rowHeight + gap),
						columnWidth, rowHeight);
			}
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (!(constraints instanceof String) && !(constraints instanceof RCPosition)) {
			throw new IllegalArgumentException("constraints mora biti string ili RCPosition");
		}
		
		RCPosition uvjet;
		
		if (constraints instanceof String) uvjet = RCPosition.parse((String)constraints);
		else uvjet = (RCPosition) constraints;
		
		int redUvjet = uvjet.getRow();
		int stupacUvjet = uvjet.getColumn();
		
		if (redUvjet < minRow || redUvjet > numberOfRows) throw new CalcLayoutException();
		if (stupacUvjet < minColumn || stupacUvjet > numberOfColumns) throw new CalcLayoutException();
		if (redUvjet == 1 && (stupacUvjet < 6 && stupacUvjet > 1)) throw new CalcLayoutException();
		if (elements.containsKey(uvjet)) throw new CalcLayoutException();
		
		elements.put(uvjet, comp);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return layoutSize(target, a -> a.getMaximumSize());
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		
	}
	
	/**
	 * Metoda vraca min/max/prefered dimenzije za container
	 * @param parent - container
	 * @param size min/max/prefered opcija
	 * @return nove dimenzije containera
	 */
	public Dimension layoutSize(Container parent, Function<Component, Dimension> size) {
		Dimension dimension = new Dimension(0, 0);
		
		for (Component component : elements.values()) {
			Dimension temp = size.apply(component);
			if (temp.width > dimension.width) dimension.width = temp.width;
			if (temp.height > dimension.height) dimension.height = temp.height;
		}
		
		dimension.height += parent.getInsets().bottom + parent.getInsets().top + (numberOfColumns - 1) * gap;
		dimension.width += parent.getInsets().left + parent.getInsets().right + (numberOfRows - 1) * gap;
		
		return dimension;
	}
}
