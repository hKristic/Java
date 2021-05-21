package hr.fer.zemris.java.gui.layouts;

import java.awt.Color;
import java.awt.Container;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class CalculatorGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private CalcModelImpl calcModel = new CalcModelImpl();
	private JCheckBox inv = new JCheckBox("inv");
	private boolean drugiOperand = false;
	
	public CalculatorGui() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(500, 200);
		setSize(700, 500);
		initGUI();
	}
	
	private void initGUI() {	
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(3));
		
		JLabel display = new JLabel();
		display.setSize(display.getSize().width + 100, display.getSize().height);
		display.setOpaque(true);
		display.setBackground(Color.ORANGE);
		display.setFont(display.getFont().deriveFont(30f));
		display.setHorizontalAlignment(SwingConstants.RIGHT);
		calcModel.addCalcValueListener(listener -> display.setText(calcModel.toString()));
		cp.add(display, new RCPosition(1, 1));
		
		inv.setBackground(Color.gray);
		cp.add(inv, new RCPosition(5, 7));
		
		NumberButton numberOrDot = new NumberButton("0");
		cp.add(numberOrDot, new RCPosition(5, 3));
		numberOrDot = new NumberButton("1");
		cp.add(numberOrDot, new RCPosition(4, 3));
		numberOrDot = new NumberButton("2");
		cp.add(numberOrDot, new RCPosition(4, 4));
		numberOrDot = new NumberButton("3");
		cp.add(numberOrDot, new RCPosition(4, 5));
		numberOrDot = new NumberButton("4");
		cp.add(numberOrDot, new RCPosition(3, 3));
		numberOrDot = new NumberButton("5");
		cp.add(numberOrDot, new RCPosition(3, 4));
		numberOrDot = new NumberButton("6");
		cp.add(numberOrDot, new RCPosition(3, 5));
		numberOrDot = new NumberButton("7");
		cp.add(numberOrDot, new RCPosition(2, 3));
		numberOrDot = new NumberButton("8");
		cp.add(numberOrDot, new RCPosition(2, 4));
		numberOrDot = new NumberButton("9");
		cp.add(numberOrDot, new RCPosition(2, 5));		
		
		ResultOrClearOrNegateOrDot rcn = new ResultOrClearOrNegateOrDot("=");
		rcn.addActionListener(listener -> {
			double result = calcModel.getPendingBinaryOperation().
							applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue());
			calcModel.setValue(result);
			calcModel.clearActiveOperand();
			calcModel.setPendingBinaryOperation(null);
		});
		cp.add(rcn, new RCPosition(1, 6));
		rcn = new ResultOrClearOrNegateOrDot("clr");
		rcn.addActionListener(listener -> calcModel.clear());
		cp.add(rcn, new RCPosition(1, 7));
		rcn = new ResultOrClearOrNegateOrDot("res");
		rcn.addActionListener(listener -> calcModel.clearAll());
		cp.add(rcn, new RCPosition(2, 7));
		rcn = new ResultOrClearOrNegateOrDot("+/-");
		rcn.addActionListener(listener -> calcModel.swapSign());
		cp.add(rcn, new RCPosition(5, 4));
		rcn = new ResultOrClearOrNegateOrDot(".");
		rcn.addActionListener(listener -> calcModel.insertDecimalPoint());
		cp.add(rcn, new RCPosition(5, 5));
		rcn = new ResultOrClearOrNegateOrDot("push");
		cp.add(rcn, new RCPosition(3, 7));
		rcn = new ResultOrClearOrNegateOrDot("pop");
		cp.add(rcn, new RCPosition(4, 7));
		
		UnarniButton ub = new UnarniButton("1/x", "1/x", a -> Math.pow(a, -1), inv, a -> Math.pow(a, -1));
		cp.add(ub, new RCPosition(2, 1));	
		ub = new UnarniButton("sin", "arcsin", a -> Math.sin(a), inv, a -> Math.asin(a));
		cp.add(ub, new RCPosition(2, 2));
		ub = new UnarniButton("log", "10^x", a -> Math.log10(a), inv, a -> Math.pow(10, a));
		cp.add(ub, new RCPosition(3, 1));
		ub = new UnarniButton("cos", "arcos", a -> Math.cos(a), inv, a -> Math.acos(a));
		cp.add(ub, new RCPosition(3, 2));
		ub = new UnarniButton("ln", "e^x", a -> Math.log(a), inv, a -> Math.pow(Math.E, a));
		cp.add(ub, new RCPosition(4, 1));
		ub = new UnarniButton("tg", "arctg", a -> Math.tan(a), inv, a -> Math.atan(a));
		cp.add(ub, new RCPosition(4, 2));
		ub = new UnarniButton("ctg", "arcctg", a -> 1 / Math.tan(a), inv, a -> 1 / Math.atan(a));
		cp.add(ub, new RCPosition(5, 2));
		
		BinarniButton bb = new BinarniButton("x^n", "x^(1/n)", (a, b) -> Math.pow(a, b), inv, (a, b) -> Math.pow(a, b));
		cp.add(bb, new RCPosition(5, 1));
		
		bb = new BinarniButton("/", "/", (a, b) -> a / b, inv, (a, b) -> a / b);
		cp.add(bb, new RCPosition(2, 6));
		
		bb = new BinarniButton("*", "*", (a, b) -> a * b, inv, (a, b) -> a * b);
		cp.add(bb, new RCPosition(3, 6));
		
		bb = new BinarniButton("-", "-", (a, b) -> a - b, inv, (a, b) -> a - b);
		cp.add(bb, new RCPosition(4, 6));
		
		bb = new BinarniButton("+", "+", (a, b) -> a + b, inv, (a, b) -> a + b);
		cp.add(bb, new RCPosition(5, 6));
	}
	
	private class ResultOrClearOrNegateOrDot extends JButton {
		private static final long serialVersionUID = 1L;

		public ResultOrClearOrNegateOrDot(String arg) {	
			setText(arg);
			setOpaque(true);
			setBackground(Color.GRAY);
			setFont(getFont().deriveFont(30f));
		}
	}
	
	private class NumberButton extends JButton {
		private static final long serialVersionUID = 1L;

		public NumberButton(String arg) {
			addActionListener(a -> {
				if (drugiOperand) {
					drugiOperand = false;
					calcModel.clear();
				}
				CalculatorGui.this.calcModel.insertDigit(Integer.parseInt(arg));
			});
			
			setText(arg);
			setOpaque(true);
			setBackground(Color.GRAY);
			setFont(getFont().deriveFont(30f));
		}
	}
	
	private class UnarniButton extends JButton {
		private static final long serialVersionUID = 1L;
		
		public UnarniButton(String arg, String invertedArg, DoubleUnaryOperator op, 
				JCheckBox invertedButton, DoubleUnaryOperator invertedOp) {	
			
			String print = invertedButton.isSelected() ? invertedArg : arg;
			DoubleUnaryOperator operator = print.equals(arg) ? op : invertedOp;
			invertedButton.addActionListener(listener -> setText(invertedButton.isSelected() ? invertedArg : arg));
			
			addActionListener(listener -> {
				calcModel.setValue(operator.applyAsDouble(calcModel.getValue()));
//				setText(print);
			});
			
			setText(arg);
			setOpaque(true);
			setBackground(Color.GRAY);
			setFont(getFont().deriveFont(30f));
		}
	}
	
	private class BinarniButton extends JButton {
		private static final long serialVersionUID = 1L;
		
		public BinarniButton(String arg, String invertedArg, DoubleBinaryOperator op, 
				JCheckBox invertedButton, DoubleBinaryOperator invertedOp) {	
			
			String print = invertedButton.isSelected() ? invertedArg : arg;
			DoubleBinaryOperator operator = print.equals(arg) ? op : invertedOp;
			invertedButton.addActionListener(listener -> setText(invertedButton.isSelected() ? invertedArg : arg));
			
			addActionListener(listener -> {
				if (!calcModel.isActiveOperandSet()) {
					calcModel.setActiveOperand(calcModel.getValue());
					calcModel.setPendingBinaryOperation(operator);
					drugiOperand = true;
				}
				else {
					calcModel.setValue(operator.applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue()));
//					calcModel.clear();
				}
			});
			
			setText(arg);
			setOpaque(true);
			setBackground(Color.GRAY);
			setFont(getFont().deriveFont(30f));
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new CalculatorGui().setVisible(true);
		});
	}
}
