package de.tudarmstadt.dvs.p4.simdb;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;

/**
 * The Class DatabasesTree displays a dialog box where the user can browse and
 * select a database to add in the databases tree of the GUI.
 */
public class DatabasesTree extends SimDBUI {

	/** The Database path. */
	private static String databasePath;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Opens the "Add Database" Dialog box.
	 */
	public final void initAddDatabaseDialog() {
		String databasePath=showOpenDialog();
		setDatabasePath(databasePath);
		if (getDatabasePath().trim().isEmpty() == false) {
			addRecentEntry(databasePath);
			if (getDatabasesList().size() == 0) {
				getDatabasesList().add(0, getDatabasePath());
			} else {
				getDatabasesList().add(getDatabasesList().size(),
						getDatabasePath());
			}

		}
		updateDatabaseTree();
	}

	/**
	 * Close.
	 */
	public final void close() {
		this.setVisible(false);
	}

	/**
	 * Update database tree of the GUI.
	 */
	public final void updateDatabaseTree() {
		try {
			setPanelTree(new JScrollPane());
			setTree(new Tree());
			getTreeContainer().removeAll();
			setPanelTree(getTree().createTree());
			getTreeContainer().add(getPanelTree(), "0, 0,1,0,f,f");
			getTreeContainer().revalidate();
			getTreeContainer().repaint();
			this.setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show open file dialog.
	 * @return the string
	 */
	public final String showOpenDialog() {
		File file;
		String path = "";
		String wd = System.getProperty("user.dir");
		JFileChooser fc = new JFileChooser(wd);
		int rc = fc.showOpenDialog(null);
		if (rc == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			if (System.getProperty("File.separator") != "/") {
				path = file.toString().replaceAll(
						File.separator + File.separator, "/");
			}
		}

		return path;
	}

	public static String getDatabasePath() {
		return databasePath;
	}

	public static void setDatabasePath(String databasePath) {
		DatabasesTree.databasePath = databasePath;
	}


}
