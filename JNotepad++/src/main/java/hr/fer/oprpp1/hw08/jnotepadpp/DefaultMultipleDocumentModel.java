package hr.fer.oprpp1.hw08.jnotepadpp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel{

	private static final long serialVersionUID = 1L;
	
	private List<SingleDocumentModel> documents = new ArrayList<>();
	private SingleDocumentModel current;
	private List<MultipleDocumentListener> listeners = new ArrayList<>();
	
	public DefaultMultipleDocumentModel() {
		this.addChangeListener(l -> {
			if (!documents.isEmpty()) {
				listeners.forEach(listener -> listener.currentDocumentChanged(current, documents.get(getSelectedIndex())));
				current = documents.get(getSelectedIndex());
		}
		});
	}
	
	private SingleDocumentListener listener = new SingleDocumentListener() {
		
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			String saved = "/greenDisk.png";
			String unsaved = "/redDisk.png";
			ImageIcon icon = model.isModified() ? getIcon(unsaved) : getIcon(saved);
			DefaultMultipleDocumentModel.this.setIconAt(getSelectedIndex(), icon);
		}
		
		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			Path newPath = model.getFilePath();
			int index = getSelectedIndex();
			DefaultMultipleDocumentModel.this.setTitleAt(index, newPath.getFileName().toString());
			DefaultMultipleDocumentModel.this.setToolTipTextAt(index, newPath.toString());
		}
	};
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return new DocumentIterator();
	}

	class DocumentIterator implements Iterator<SingleDocumentModel> {

		private int docAt = 0;
		@Override
		public boolean hasNext() {
			return docAt < documents.size();
		}

		@Override
		public SingleDocumentModel next() {
			if (!hasNext()) {
				throw new NoSuchElementException("Nema vise elemenata");
			}
			return documents.get(docAt++);
		}
		
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		current = new DefaultSingleDocumentModel(null, "");
		documents.add(current);
		
		JScrollPane scroll = new JScrollPane(current.getTextComponent());
		this.addTab("unnamed", getIcon("/greenDisk.png"), scroll);
		
		this.setSelectedComponent(scroll);
		this.setToolTipTextAt(getSelectedIndex(), "unnamed");
		
		listeners.forEach(listener -> listener.documentAdded(current));
		current.addSingleDocumentListener(listener);
		
		return current;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return current;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		String tekst = "";
		
		SingleDocumentModel old = getCurrentDocument();
		
		try {
			tekst = Files.readString(path);
			current = new DefaultSingleDocumentModel(path, tekst);
			documents.add(current);
			JScrollPane scroll = new JScrollPane(current.getTextComponent());
			this.add(scroll);
			this.setSelectedComponent(scroll);
			this.setIconAt(getSelectedIndex(), getIcon("/greenDisk.png"));
			this.setToolTipTextAt(getSelectedIndex(), path.toString());
			this.setTitleAt(getSelectedIndex(), current.getFilePath().getFileName().toString());
			if (old == null) {
				listeners.forEach(listener -> listener.currentDocumentChanged(old, current));
			}
			else {
				listeners.forEach(listener -> listener.documentAdded(current));
				current.addSingleDocumentListener(listener);
			}
		} catch(Exception ex) {
			throw new IllegalArgumentException();
		}
		return current;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		String podatci = model.getTextComponent().getText();
		try {
			if (newPath == null) Files.writeString(model.getFilePath(), podatci);
			else {
				Files.writeString(newPath, podatci);
				current.setFilePath(newPath);
			}
			int index = getSelectedIndex();
			this.setToolTipTextAt(index, current.getFilePath().toString());
			this.setTitleAt(index, current.getFilePath().getFileName().toString());
			current.setModified(false);
			
		} catch (IOException e1) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		documents.remove(model);
		listeners.forEach(listener -> listener.documentRemoved(model));
		current.removeSingleDocumentListener(listener);
		this.removeTabAt(getSelectedIndex());
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}
	
	private ImageIcon getIcon(String p) {
		byte[] bytes = null;
		try (InputStream is = this.getClass().getResourceAsStream(p)) {
			if (is == null) {
				System.out.println("greska");
				System.exit(0);
			}
			bytes = is.readAllBytes();
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
		return new ImageIcon(bytes);
	}
}
