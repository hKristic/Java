package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

public abstract class LocalizableAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private String key;

	public LocalizableAction(String key, ILocalizationProvider lp) {
		this.key = key;

		String translation = lp.getString(key);
		putValue(Action.NAME, translation);

		lp.addLocalizationListener(() -> {
			String translate = lp.getString(key);
			putValue(Action.NAME, translate);
		});
	}
}
