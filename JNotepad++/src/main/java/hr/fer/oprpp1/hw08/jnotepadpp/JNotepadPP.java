package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LjMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;

public class JNotepadPP extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private DefaultMultipleDocumentModel documents = new DefaultMultipleDocumentModel();
	private JLabel lines = new JLabel();
	private JLabel stats = new JLabel();
	private Sat sat = new Sat();
	private ILocalizationProvider localize = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(0, 0);
		setSize(700, 600);
		
		documents.addMultipleDocumentListener(listener);
		initGUI();
		
		WindowListener wl = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (documents.getNumberOfDocuments() == 0) dispose();
				
				String[] opcija = new String[] {localize.getString("Yes"), localize.getString("No"), localize.getString("Cancel")};
				for (SingleDocumentModel doc : documents) {
					if (doc.isModified()) {
						int rezultat = JOptionPane.showOptionDialog(JNotepadPP.this, 
								localize.getString("modifiedContent"),
								localize.getString("content"),
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.WARNING_MESSAGE, null, opcija, opcija[0]);
						
						if (rezultat == 0) {
							Path p = doc.getFilePath();
							if (p != null) {
								documents.saveDocument(doc, p);
								documents.closeDocument(documents.getCurrentDocument());
							}
							else {
								JNotepadPP.this.saveAsDocumentMethod();
								documents.closeDocument(documents.getCurrentDocument());
							}
							documents.closeDocument(documents.getCurrentDocument());
							if (documents.getNumberOfDocuments() == 0) dispose();
							return;
						}
						if (rezultat == 2 || rezultat == JOptionPane.CLOSED_OPTION) {
							return;
						}
					}
				}
				dispose();
				System.exit(0);
			}
		};
		
		this.addWindowListener(wl);
		localize.addLocalizationListener(() -> statusBarData(documents.getCurrentDocument().getTextComponent()));
	}
	
	private void initGUI() {	
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(documents, BorderLayout.CENTER);
		
		createActions();
		createMenus();
		createToolbars();
		createStatusBar();
		
		documents.createNewDocument();
		documents.addMultipleDocumentListener(listener);
		statusBarData(documents.getCurrentDocument().getTextComponent());
	}

	private MultipleDocumentListener listener = new MultipleDocumentListener() {
		
		@Override
		public void documentRemoved(SingleDocumentModel model) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void documentAdded(SingleDocumentModel model) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
			Path p = currentModel.getFilePath();
			JTextArea text = currentModel.getTextComponent();
			JNotepadPP.this.setTitle((p == null ? localize.getString("unnamed") : p.toString()) + " - JNotepad++");
			statusBarData(text);
			
			text.addCaretListener(l -> {
				boolean len = Math.abs(text.getCaret().getDot()-text.getCaret().getMark()) != 0;
				copyDocumentAction.setEnabled(len);
				cutDocumentAction.setEnabled(len);
				uniqueDocumentAction.setEnabled(len);
				uperCaseDocumentAction.setEnabled(len);
				lowerCaseDocumentAction.setEnabled(len);
				invertCaseDocumentAction.setEnabled(len);
				statusBarData(text);
			});
		}
	};
	
	private void saveAsDocumentMethod() {	
		SingleDocumentModel doc = documents.getCurrentDocument();
		if(doc.getFilePath() == null) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Save document");
			if(jfc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(
						JNotepadPP.this, 
						localize.getString("error"), 
						localize.getString("warning"), 
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			Path dest = jfc.getSelectedFile().toPath();
			documents.saveDocument(doc, dest);
		}
		else documents.saveDocument(doc, null);
	}
	
	private void characterTransform(Function<String, String> function) {
		JTextArea editor = documents.getCurrentDocument().getTextComponent();
		Document doc = editor.getDocument();
		int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
		int offset = 0;
		if(len!=0) {
			offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
		} else {
			len = doc.getLength();
		}
		try {
			String text = doc.getText(offset, len);
			text = function.apply(text);
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch(BadLocationException ex) {
			ex.printStackTrace();
		}
	}
	
	private String changeCase(String text) {
		char[] znakovi = text.toCharArray();
		for(int i = 0; i < znakovi.length; i++) {
			char c = znakovi[i];
			if(Character.isLowerCase(c)) {
				znakovi[i] = Character.toUpperCase(c);
			} else if(Character.isUpperCase(c)) {
				znakovi[i] = Character.toLowerCase(c);
			}
		}
		return new String(znakovi);
	}
	
	private void unique() {
		JTextArea txt = documents.getCurrentDocument().getTextComponent();
		Document document = txt.getDocument();

		int start = Math.min(txt.getCaret().getDot(), txt.getCaret().getMark());
		int length = Math.abs(txt.getCaret().getDot() - txt.getCaret().getMark());

		try {
			String select = document.getText(start, length);

			Set<String> set = new LinkedHashSet<>(Arrays.asList(select.split("\n")));

			StringBuilder builder = new StringBuilder();
			set.forEach(s -> builder.append(s + "\n"));
			
			document.remove(start, length);
			document.insertString(start, builder.toString(), null);
		} 
		catch (BadLocationException ignorable) {
			
		}

	}
	
	private void sort(int direction) {
		JTextArea txt = documents.getCurrentDocument().getTextComponent();
		Document document = txt.getDocument();

		int start = Math.min(txt.getCaret().getDot(), txt.getCaret().getMark());
		int length = Math.abs(txt.getCaret().getDot() - txt.getCaret().getMark());

		try {
			int line = txt.getLineOfOffset(start);
			int lineStart = txt.getLineStartOffset(line);
			int end = txt.getLineEndOffset(txt.getLineOfOffset(start + length));
			String text = txt.getDocument().getText(lineStart, end);
			
			List<String> list = new ArrayList<>(Arrays.asList(text.split("\\s")));
			Locale locale = new Locale(LocalizationProvider.getInstance().getLanguage());
			Collator collator = Collator.getInstance(locale);
			list.sort(direction == 1 ? collator : collator.reversed());
			
			StringBuilder builder = new StringBuilder();
			list.forEach(s -> builder.append(s + "\n"));
			
			document.remove(lineStart, end);
			document.insertString(lineStart, builder.toString(), null);
		} 
		catch (BadLocationException ignorable) {
			
		}

	}
	
	private Action createDocument = new LocalizableAction("Create", localize) {

		@Override
		public void actionPerformed(ActionEvent e) {
			documents.createNewDocument();
		}
	};
	
	private Action openDocument = new LocalizableAction("Open", localize) {	
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open file");
			if(fc.showOpenDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
				return;
			}
			File fileName = fc.getSelectedFile();
			Path filePath = fileName.toPath();
			if(!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(
						JNotepadPP.this, 
						localize.getString("File") + " "+fileName.getAbsolutePath()+ " " + localize.getString("non"), 
						localize.getString("err"), 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			documents.loadDocument(fileName.toPath());
		}
	};
	
	private Action saveAsDocument = new LocalizableAction("SaveAs", localize) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JNotepadPP.this.saveAsDocumentMethod();
		}
	};
	
	private Action saveDocument = new LocalizableAction("Save", localize) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = documents.getCurrentDocument();
			if (doc.getFilePath() != null) {
				documents.saveDocument(doc, null);
			}
			else {
				JNotepadPP.this.saveAsDocumentMethod();
			}
		}
	};
	
	private Action closeDocument = new LocalizableAction("Close", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (documents.getCurrentDocument().isModified()) {
				String[] opcija = new String[] {localize.getString("Yes"), localize.getString("No"), localize.getString("Cancel")};
				int rezultat = JOptionPane.showOptionDialog(JNotepadPP.this, 
						localize.getString("modifiedContent"),
						localize.getString("content"),
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, opcija, opcija[0]);
				if (rezultat == 0) saveAsDocumentMethod();
			}
			documents.closeDocument(documents.getCurrentDocument());
		}
	};
	
	private Action statsOfDocument = new LocalizableAction("Stats", localize) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea txt = documents.getCurrentDocument().getTextComponent();
			int charactersNum = txt.getText().length();
			int linesNum = txt.getLineCount();
			char[] charWithoutWhitespace = txt.getText().toCharArray();
			int charWithoutWhitespaceNum = 0;
			for (int i = 0; i < charWithoutWhitespace.length; i++) {
				if (charWithoutWhitespace[i] != ' ' && charWithoutWhitespace[i] != '\t' 
					&& charWithoutWhitespace[i] != '\n' && charWithoutWhitespace[i] != '\r')
					
					charWithoutWhitespaceNum++;
			}
			String statistics = localize.getString("InText") + " " + charactersNum +
					" " + localize.getString("chars") + " "
					+ linesNum + localize.getString("rows") + " " + 
					charWithoutWhitespaceNum + " " + localize.getString("words");
			
			JOptionPane.showMessageDialog(JNotepadPP.this, statistics, "Stats", JOptionPane.DEFAULT_OPTION);
		}
	};
	
	private Action copyDocumentAction = new LocalizableAction("Copy", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			new DefaultEditorKit.CopyAction().actionPerformed(e);
		}
	};
	
	private Action cutDocumentAction = new LocalizableAction("Cut", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			new DefaultEditorKit.CutAction().actionPerformed(e);
		}
	};
	
	private Action pasteDocumentAction = new LocalizableAction("Paste", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			new DefaultEditorKit.PasteAction().actionPerformed(e);
		}
	};
	
	private final Action lowerCaseDocumentAction = new LocalizableAction("lowerCase", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			characterTransform(a -> a.toLowerCase());
		}
	};
	
	private final Action invertCaseDocumentAction = new LocalizableAction("invertCase", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			characterTransform(a -> changeCase(a));
		}
	};
	
	private final Action uperCaseDocumentAction = new LocalizableAction("upperCase", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			characterTransform(a -> a.toUpperCase());
		}
	};
	
	private final Action uniqueDocumentAction = new LocalizableAction("unique", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			unique();
		}
	};
	
	private final Action ascSortDocumentAction = new LocalizableAction("AscSort", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			sort(1);
		}
	};
	
	private final Action descSortDocumentAction = new LocalizableAction("DescSort", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			sort(2);
		}
	};
	
	private final Action hrvatski = new LocalizableAction("hr", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};
	
	private final Action english = new LocalizableAction("en", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};
	
	private final Action deutsch = new LocalizableAction("de", localize) {
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
	};
	
	private void statusBarData(JTextArea txt) {
		try {
			Caret caret = txt.getCaret();
			int caretPos = txt.getCaretPosition();
			int selectedSize = Math.abs(caret.getDot()-caret.getMark());
			String statsInformation = localize.getString("Ln") + (txt.getLineOfOffset(caretPos) + 1) +
					" " + localize.getString("Col") + Math.abs(caretPos - txt.getLineStartOffset(txt.getLineOfOffset(caretPos)) + 1)+
					" " + localize.getString("Sel") + selectedSize;
			
			stats.setText(statsInformation);
			
		} catch (BadLocationException e1) {
			
		}
		
		lines.setText(localize.getString("length") + " " + txt.getText().length());
	}
	
	private void createActions() {
		createDocument.putValue(
				Action.NAME, 
				localize.getString("Create"));
		createDocument.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control C")); 
		createDocument.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_C); 
		createDocument.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("createInfo")); 
		
		openDocument.putValue(
				Action.NAME, 
				localize.getString("Open"));
		openDocument.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control O")); 
		openDocument.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_O); 
		openDocument.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("openInfo")); 
		
		saveAsDocument.putValue(
				Action.NAME, 
				localize.getString("SaveAs"));
		saveAsDocument.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control Q")); 
		saveAsDocument.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_Q); 
		saveAsDocument.putValue(
				Action.SHORT_DESCRIPTION, 
				"saveAsInfo"); 
		
		saveDocument.putValue(
				Action.NAME, 
				localize.getString("Save"));
		saveDocument.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control S")); 
		saveDocument.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_S); 
		saveDocument.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("saveInfo")); 
		
		closeDocument.putValue(
				Action.NAME, 
				localize.getString("Close"));
		closeDocument.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control M")); 
		closeDocument.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_M); 
		closeDocument.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("closeInfo")); 
		
		statsOfDocument.putValue(
				Action.NAME, 
				localize.getString("Statistics"));
		statsOfDocument.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control z")); 
		statsOfDocument.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_Z); 
		statsOfDocument.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("statsInfo"));
		
		copyDocumentAction.putValue(
				Action.NAME, 
				localize.getString("Copy"));
		copyDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control c")); 
		copyDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_C); 
		copyDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("copyInfo"));
		
		cutDocumentAction.putValue(
				Action.NAME, 
				localize.getString("Cut"));
		cutDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control x")); 
		cutDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_X); 
		cutDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("cutInfo"));
		
		pasteDocumentAction.putValue(
				Action.NAME, 
				localize.getString("Paste"));
		pasteDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control v")); 
		pasteDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_V); 
		pasteDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("pasteInfo"));
		
		uperCaseDocumentAction.putValue(
				Action.NAME, 
				localize.getString("upperCase"));
		uperCaseDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control 2")); 
		uperCaseDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_2); 
		uperCaseDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("uperCaseInfo"));
		
		lowerCaseDocumentAction.putValue(
				Action.NAME, 
				localize.getString("lowerCase"));
		lowerCaseDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control 3")); 
		lowerCaseDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_3); 
		lowerCaseDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("lowerCaseInfo"));
		
		invertCaseDocumentAction.putValue(
				Action.NAME, 
				localize.getString("invertCase"));
		invertCaseDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control 4")); 
		invertCaseDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_4); 
		invertCaseDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("invertCaseInfo"));
		
		uniqueDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control 1")
		);
		uniqueDocumentAction.putValue(
				Action.MNEMONIC_KEY,
				KeyStroke.getKeyStroke(localize.getString("u")).getKeyCode()
		);
		uniqueDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("uniqueInfo")
				);
		
		ascSortDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control 9")
		);
		ascSortDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("sortInfoAsc")
				);
		
		descSortDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control 8")
		);
		descSortDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				localize.getString("sortInfoDesc")
				);
		
		hrvatski.putValue(
				Action.MNEMONIC_KEY, 
				KeyStroke.getKeyStroke(localize.getString("hrm")).getKeyCode());

		english.putValue(
				Action.MNEMONIC_KEY, 
				KeyStroke.getKeyStroke(localize.getString("enm")).getKeyCode());
		
		deutsch.putValue(
				Action.MNEMONIC_KEY, 
				KeyStroke.getKeyStroke(localize.getString("dem")).getKeyCode());
	}
	
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new LjMenu("File", localize);
		menuBar.add(fileMenu);
		fileMenu.add(new JMenuItem(createDocument));
		fileMenu.add(new JMenuItem(openDocument));
		fileMenu.add(new JMenuItem(saveAsDocument));
		fileMenu.add(new JMenuItem(saveDocument));
		fileMenu.add(new JMenuItem(closeDocument));
		
		JMenu infoMenu = new LjMenu("Info", localize);
		infoMenu.add(new JMenuItem(statsOfDocument));		
		menuBar.add(infoMenu);
		
		JMenu editMenu = new LjMenu("Edit", localize);
		menuBar.add(editMenu);
		editMenu.add(new JMenuItem(copyDocumentAction));
		editMenu.add(new JMenuItem(cutDocumentAction));
		editMenu.add(new JMenuItem(pasteDocumentAction));
		
		JMenu languageMenu = new LjMenu("Languages", localize);
		languageMenu.add(new JMenuItem(hrvatski));
		languageMenu.add(new JMenuItem(english));
		languageMenu.add(new JMenuItem(deutsch));
		menuBar.add(languageMenu);
		
		JMenu toolsMenu = new LjMenu("Tools", localize);
		toolsMenu.add(new JMenuItem(uperCaseDocumentAction));
		toolsMenu.add(new JMenuItem(lowerCaseDocumentAction));
		toolsMenu.add(new JMenuItem(invertCaseDocumentAction));
		toolsMenu.add(new JMenuItem(uniqueDocumentAction));
		toolsMenu.add(new JMenuItem(ascSortDocumentAction));
		toolsMenu.add(new JMenuItem(descSortDocumentAction));
		
		menuBar.add(toolsMenu);
		
		this.setJMenuBar(menuBar);
	}
	
	private void createToolbars() {
		JToolBar toolBar = new JToolBar("Tools");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(createDocument));
		toolBar.add(new JButton(openDocument));
		toolBar.addSeparator();
		toolBar.add(new JButton(saveDocument));
		toolBar.add(new JButton(saveAsDocument));
		toolBar.add(new JButton(closeDocument));
		toolBar.add(new JButton(statsOfDocument));
		toolBar.addSeparator();
		toolBar.add(new JButton(copyDocumentAction));
		toolBar.add(new JButton(cutDocumentAction));
		toolBar.add(new JButton(pasteDocumentAction));
		
		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}
	
	private void createStatusBar() {
		GridLayout gl = new GridLayout(0, 3);
		JPanel panel = new JPanel(gl);
		sat.setHorizontalAlignment(JLabel.RIGHT);
		
		panel.add(lines);
		panel.add(stats);
		panel.add(sat);
		
		this.getContentPane().add(panel, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new JNotepadPP().setVisible(true);
			}
		});
	}
}
