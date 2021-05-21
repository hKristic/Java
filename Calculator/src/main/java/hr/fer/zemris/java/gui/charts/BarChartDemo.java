package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class BarChartDemo extends JFrame{

	private static final long serialVersionUID = 1L;

	private BarChartComponent komponenta1;
	private String putanja;
	
	public BarChartDemo(BarChartComponent komponenta1, String putanja) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("BarChart");
		setLocation(20, 20);
		setSize(1000, 700);
		this.putanja = putanja;
		this.komponenta1 = komponenta1;
		initGUI();	
	}
	
	private void initGUI() {
		komponenta1.setLocation(0, 0);
		komponenta1.setSize(1000, 700);
		komponenta1.setOpaque(true);

		JLabel putanja = new JLabel("Datoteka " + this.putanja, SwingConstants.CENTER);
		getContentPane().add(putanja, BorderLayout.NORTH);
		getContentPane().add(komponenta1, BorderLayout.CENTER);
	}
	
	private static List<XYValue> xyValueConstructor(String line) {
		String[] parovi = line.split(" ");
		List<XYValue> list = new ArrayList<>();
		for (String koordinate : parovi) {
			String[] komponente = koordinate.split(",");
			XYValue value = new XYValue(Integer.parseInt(komponente[0]), Integer.parseInt(komponente[1]));
			list.add(value);
		}		
		return list;
	}

	public static void main(String[] args) {
		try {
			BufferedReader br = Files.newBufferedReader(Paths.get(args[0]));
			String line = "";
			List<String> list = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				line = br.readLine();
				list.add(line);
			}
			
			List<XYValue> listArgs = xyValueConstructor(list.get(2));
			
			BarChart chart = new BarChart(listArgs, list.get(0), list.get(1), Integer.parseInt(list.get(3)),
										  Integer.parseInt(list.get(4)), Integer.parseInt(list.get(5)));
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					BarChartDemo prozor = new BarChartDemo(new BarChartComponent(chart), args[0]);
					prozor.setVisible(true);
				}
			});
		} catch (Exception e) {
			System.out.println("Greska");
		}
	}

}
