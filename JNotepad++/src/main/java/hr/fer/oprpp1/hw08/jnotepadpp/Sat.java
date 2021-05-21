package hr.fer.oprpp1.hw08.jnotepadpp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Sat extends JLabel {
	private static final long serialVersionUID = 1L;
	
	volatile String vrijeme;
	volatile boolean stopRequested;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	public Sat() {
		updateTime();
		
		Thread t = new Thread(()->{
			while(true) {
				try {
					Thread.sleep(500);
				} catch(Exception ex) {}
				if(stopRequested) break;
				SwingUtilities.invokeLater(()->{
					updateTime();
				});
			}
			System.out.println("Zaustavljen.");
		});
		t.setDaemon(true);
		t.start();
	}
	
	private void zaustavi() {
		stopRequested = true;
	}
	
	private void updateTime() {
		vrijeme = formatter.format(LocalDateTime.now());
		this.setText(vrijeme);
	}
}
