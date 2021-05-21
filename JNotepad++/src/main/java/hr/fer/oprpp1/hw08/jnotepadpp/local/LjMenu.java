package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.JMenu;

public class LjMenu extends JMenu{

	private String key;
	public LjMenu(String key, ILocalizationProvider lp) {
		this.key = key;

		String translate = lp.getString(key);
		setText(translate);

		lp.addLocalizationListener(() -> {
			String translation = lp.getString(key);
			setText(translation);
		});
	}
}
