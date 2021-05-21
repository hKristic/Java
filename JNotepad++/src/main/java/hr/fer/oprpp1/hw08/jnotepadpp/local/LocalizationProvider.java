package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {

	private String language;
	private ResourceBundle bundle;
	private static LocalizationProvider instance;
	
	private LocalizationProvider() {
		language = "en";
		setBundel();
	}

	public static LocalizationProvider getInstance() {		
		if (instance == null) instance = new LocalizationProvider();
		return instance;
	}
	
	public void setLanguage(String language) {
		this.language = language;
		setBundel();
		this.fire();
	}
	
	public String getLanguage() {
		return language;
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}
	
	private void setBundel() {
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.prijevodi", locale);
	}
}
