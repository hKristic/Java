package hr.fer.oprpp1.hw08.jnotepadpp.local;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	private boolean connected;
	private ILocalizationListener listener = () -> fire();
	private ILocalizationProvider lProvider;
	
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		lProvider = provider;
	}
	
	@Override
	public String getString(String key) {
		return lProvider.getString(key);
	}

	public void disconnect() {
		if (connected == false) return;
		
		connected = false;
		lProvider.removeLocalizationListener(listener);
	}
	
	public void connect() {
		if (connected == true) return;
		
		connected = true;
		lProvider.addLocalizationListener(listener);
	}
}
