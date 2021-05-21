package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Model podataka za prim brojeve
 * 
 * @author Hrvoje
 */
public class PrimListModel implements ListModel<Integer>{

	private List<ListDataListener> listeners = new ArrayList<>();
	private List<Integer> data = new ArrayList<>();
	private int current;	//trenutni prim broj za dodavanje
	
	public PrimListModel() {
		data.add(1);
		current = 2;
	}
	
	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	public void addElement() {
		while(true) {
			if (prim(current)) {
				data.add(current);
				current++;
				break;
			}
			current++;
		}
			
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, data.size()-1, data.size()-1);
		for(ListDataListener l : listeners) {
			l.intervalAdded(e);
		}
	}

	/**
	 * Metoda provjerava je li predani broj prim broj
	 * @param current2 broj koji se provjerava
	 * @return true ako je prim, inace false
	 */
	private boolean prim(int current2) {
		for (int i = 2; i <= Math.sqrt(current2); i++) {
			if (current2 % i == 0) return false;
		}
		return true;
	}
	
	
}
