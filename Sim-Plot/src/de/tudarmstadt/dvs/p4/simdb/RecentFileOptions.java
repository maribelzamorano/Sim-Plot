package de.tudarmstadt.dvs.p4.simdb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * A menu used to store and display recently used files. Saves entries in a file
 * called "[user.dir]/[name passed to constructor].recent".
 */
public abstract class RecentFileOptions extends JMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pathToSavedFile;
	private int itemCount;
	private String[] recentEntries;
	private final static String defaultText = "__R_e_C_e_N_t__:_?";

	/**
	 * Create a new instance of RecentFileMenu.
	 * 
	 * @param name
	 *            The name of this menu, not displayed but used to store the
	 *            list of recently used file names.
	 * @param count
	 *            The number of recent files to store.
	 */
	public RecentFileOptions(String name, int count) {
		super();
		this.setText("Recent Databases");
		this.setMnemonic('R');
		this.itemCount = count;

		this.recentEntries = new String[count];
		for (int index = 0; index < this.itemCount; index++) {
			this.recentEntries[index] = defaultText;
		}

		this.pathToSavedFile = System.getProperty("user.dir");
		if ((this.pathToSavedFile == null)
				|| (this.pathToSavedFile.length() <= 0)) {
			this.pathToSavedFile = new String(name + ".recent");
		} else if (this.pathToSavedFile.endsWith(File.separator)) {
			this.pathToSavedFile = this.pathToSavedFile + name + ".recent";
		} else {
			this.pathToSavedFile = this.pathToSavedFile + File.separator + name
					+ ".recent";
		}

		File recentFile = new File(this.pathToSavedFile);
		if (recentFile.exists()) {
			try {
				@SuppressWarnings("resource")
				LineNumberReader reader = new LineNumberReader(new FileReader(
						this.pathToSavedFile));
				while (reader.ready()) {
					this.addEntry(reader.readLine(), false);
				}
			} catch (Exception x) {
				x.printStackTrace();
			}
		} else { // disable
			this.setEnabled(false);
		}
	}

	/**
	 * Adds a new entry to the menu. Moves everything "down" one row.
	 * 
	 * @param filePath
	 *            The new path to add.
	 */
	public void addEntry(String filePath) {
		this.addEntry(filePath, true);
	}

	/**
	 * Adds a new entry to the menu. Moves everything "down" one row.
	 * 
	 * @param filePath
	 *            The new path to add.
	 * @param updateFile
	 *            Whether to update the saved file, only false when called from
	 *            constructor.
	 */
	private void addEntry(String filePath, boolean updateFile) {
		if (!this.isEnabled()) {
			this.setEnabled(true);
		}
		this.removeAll();
		int count = this.itemCount - 1;
		for (int index = count; index > 0; index--) {
			if (!this.recentEntries[index - 1].equalsIgnoreCase(filePath)) {
				this.recentEntries[index] = new String(
						this.recentEntries[index - 1]);
			}
		}
		if (!this.recentEntries[0].equalsIgnoreCase(filePath)) {
			this.recentEntries[0] = new String(filePath);
		}
		for (int index = 0; index < this.itemCount; index++) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(this.recentEntries[index]);
			if (this.recentEntries[index].equals(defaultText)) {
				menuItem.setVisible(false);
			} else {
				menuItem.setVisible(true);
				menuItem.setToolTipText(this.recentEntries[index]);
				menuItem.setActionCommand(this.recentEntries[index]);
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent actionEvent) {
						onSelectFile(actionEvent.getActionCommand());
					}
				});
			}
			this.add(menuItem);
		}

		if (updateFile) {
			try {
				FileWriter writer = new FileWriter(new File(
						this.pathToSavedFile));
				int topIndex = this.itemCount - 1;
				for (int index = topIndex; index >= 0; index--) {
					if (!this.recentEntries[index].equals(defaultText)) {
						writer.write(this.recentEntries[index]);
						writer.write("\n");
					}
				}
				writer.flush();
				writer.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
	}

	/**
	 * Event that fires when a recent file is selected from the menu. Override
	 * this when implementing.
	 * 
	 * @param filePath
	 *            The file that was selected.
	 */
	public abstract void onSelectFile(String filePath);
}