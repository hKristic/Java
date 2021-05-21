package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DefaultSingleDocumentModel implements SingleDocumentModel{

	private Path filePath;
	private JTextArea text;
	private boolean modified = false;
	private List<SingleDocumentListener> listeners;
	
	public DefaultSingleDocumentModel(Path filePath, String textContent) {
		this.filePath = filePath;
		text = new JTextArea(textContent);
		listeners = new ArrayList<>();
		
		text.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(true);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setModified(true);
			}
		});
	}

	@Override
	public JTextArea getTextComponent() {
		return text;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		if (path == null) throw new IllegalArgumentException("Path ne smije biti null");
		this.filePath = path;
		listeners.forEach(l -> l.documentFilePathUpdated(this));
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		listeners.forEach(l -> l.documentModifyStatusUpdated(this));
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}

}
