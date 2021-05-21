package hr.fer.zemris.java.gui.layouts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * Razred predstavlja model pojednostavljenog kalkulatora
 * 
 * @author Hrvoje
 */
public class CalcModelImpl implements CalcModel {

	private boolean editable = true;	//je li model promjenjiv
	private boolean negate = false;
	private String input = "";			//trenutno stanje - string
	private double value = 0;			//trenutno stanje - int
	private String current = null;		//zamrznuto stanje
	private Double activeOperand = null;
	private DoubleBinaryOperator pendingOperation = null;
	
	private List<CalcValueListener> list = new ArrayList<>();
	
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		if (l == null) throw new IllegalArgumentException("Promatrac ne smije biti null");
		list.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		if (l == null) throw new IllegalArgumentException("Promatrac ne smije biti null");
		list.remove(l);
	}
	
	@Override
	public void notifyAllListeners() {
		for (CalcValueListener l : list) {
			l.valueChanged(this);
		}
	}

	@Override
	public double getValue() {	
		return value;
	}

	@Override
	public void setValue(double value) {
		this.value = value;
		current = String.valueOf(value);
		editable = false;
		notifyAllListeners();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		value = 0;
//		current = null;
		input = "";
		editable = true;
		notifyAllListeners();
	}

	@Override
	public void clearAll() {
		input = "";
		value = 0;
		current = null;
		activeOperand = null;
		pendingOperation = null;
		editable = true;
		notifyAllListeners();
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (editable == false) throw new CalculatorInputException("Model nije editabilan");
		value = -1 * value;
		if (negate == false) {
			input = "-" + input;
			negate = true;
		}
		else {
			input = input.substring(1, input.length());
			negate = false;
		}
		current = null;
		notifyAllListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if (editable == false) throw new CalculatorInputException("Model nije editabilan");
		if (input.contains(".")) throw new CalculatorInputException("Tocka je vec dodana");
		if (input.equals("") || input.equals("+") || input.equals("-")) throw new CalculatorInputException("Tocka ne moze doci na prvo mjesto");
		input += ".";
		current = null;
		notifyAllListeners();
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if (editable == false) throw new CalculatorInputException("Model nije editabilan");
		
		String tmp = input + digit;
		try {
			if (tmp.equals("00")) tmp = "0";
			if (tmp.charAt(0) == '0' && tmp.length() > 1 && !tmp.contains(".")) tmp = String.valueOf(tmp.charAt(1));	
			value = Double.parseDouble(tmp);
			negate = value < 0 ? true : false;
			if (!Double.isFinite(value)) throw new CalcLayoutException();
			input = tmp;
			current = null;
			notifyAllListeners();
		} catch (Exception e) {
			throw new CalculatorInputException("Broj se ne moze parsirati");
		}
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperand != null;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (!isActiveOperandSet()) throw new IllegalStateException("ne postoji aktivan operand");
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
	}

	@Override
	public void clearActiveOperand() {
		activeOperand = null;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingOperation = op;
	}

	@Override
	public String toString() {
		if (current == null) {
			current = input;
			if (current.equals("") || current.equals("+") || current.equals("-")) current = negate == true ? "-0" : "0";
		}
		return current;
	}
}
