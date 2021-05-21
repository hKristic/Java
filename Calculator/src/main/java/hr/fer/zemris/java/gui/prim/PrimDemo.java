package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class PrimDemo extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("PrimDemo");
		setLocation(20, 20);
		setSize(500, 200);
		initGUI();

	}

	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		PrimListModel plm = new PrimListModel();
		JList<Integer> first = new JList<>(plm);
		JList<Integer> drugi = new JList<>(plm);
		
		JPanel p = new JPanel(new GridLayout(1, 0));
		p.add(new JScrollPane(first));
		p.add(new JScrollPane(drugi));
		
		cp.add(p, BorderLayout.CENTER);

		JButton button = new JButton("Sljedeci");
		button.addActionListener(listener -> plm.addElement());
		cp.add(button, BorderLayout.PAGE_END);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new PrimDemo().setVisible(true);
		});
	}

}
