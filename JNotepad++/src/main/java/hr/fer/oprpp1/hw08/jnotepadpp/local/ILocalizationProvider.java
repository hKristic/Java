package hr.fer.oprpp1.hw08.jnotepadpp.local;

public interface ILocalizationProvider {
	
	void addLocalizationListener(ILocalizationListener l);

	void removeLocalizationListener(ILocalizationListener l);

	String getString(String key);
}
