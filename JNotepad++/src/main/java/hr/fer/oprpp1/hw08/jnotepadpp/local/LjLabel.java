package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.JLabel;

public class LjLabel extends JLabel {

	private String key;
	private ILocalizationProvider lProvider;

	public LjLabel(String key, ILocalizationProvider provider) {
		this.key = key;
		this.lProvider = provider;

		updateLabel();
	}
	
	private void updateLabel() {
		String translator = lProvider.getString(key);
		this.setText(translator + ":");
		
		lProvider.addLocalizationListener(() -> {
			String translate = lProvider.getString(key);
			setText(translate + ":");
		});
	}
}
