package hr.fer.zemris.java.gui.layouts;

import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Main extends JFrame{
	private static final long serialVersionUID = 1L;

	public Main() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Main");
		setLocation(20, 20);
		setSize(300, 500);
		initGUI();
	}

	private void initGUI() {
		Container cp = getContentPane();
		JPanel p = new JPanel(new CalcLayout(3));
		p.add(l("x"), new RCPosition(1,1));
		p.add(l("y"), new RCPosition(2,3));
		p.add(l("z"), new RCPosition(2,7));
		p.add(l("w"), new RCPosition(4,2));
		p.add(l("a"), new RCPosition(4,5));
		p.add(l("b"), new RCPosition(4,7));
		
		cp.add(p);
	}
	
	private JLabel l(String text) {
		JLabel l = new JLabel(text);
		l.setBackground(Color.YELLOW);
		l.setOpaque(true);
		return l;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Main().setVisible(true);
		});
	}
}
