package de.tudarmstadt.dvs.p4.simdb;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The Class CheckNode sets the checkboxes that represent the different
 * statistics in the database tree of the GUI.
 */
class CheckNode extends DefaultMutableTreeNode {

	/** The Constant serialVersionUID*/
	private static final long serialVersionUID = 1L;

	/** The Constant SINGLE_SELECTION. */
	public static final int SINGLE_SELECTION = 0;

	/** The Constant DIG_IN_SELECTION. */
	public static final int DIG_IN_SELECTION = 4;

	/** The selection mode. */
	private int selectionMode;

	/** The is selected. */
	private boolean isSelected;

	/**
	 * Instantiates a new check node.
	 */
	public CheckNode() {
		this(null);
	}

	/**
	 * Instantiates a new check node.
	 * @param userObject the user object
	 */
	public CheckNode(final Object userObject) {
		this(userObject, true, false);
	}

	/**
	 * Instantiates a new check node.
	 * @param userObject the user object
	 * @param allowsChildren the allows children
	 * @param isSelected the is selected
	 */
	public CheckNode(final Object userObject, final boolean allowsChildren,
			boolean isSelected) {
		super(userObject, allowsChildren);
		this.isSelected = isSelected;
		setSelectionMode(DIG_IN_SELECTION);
	}

	/**
	 * Gets the selection mode.
	 * @return the selection mode
	 */
	public int getSelectionMode() {
		return selectionMode;
	}

	/**
	 * Checks if the checkbox is selected.
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * Sets the checkbox selected.
	 * @param isSelected contains the value of the checkbox (true if is selected)
	 */
	public void setSelected(boolean isSelected) {

		this.isSelected = isSelected;

		if ((selectionMode == DIG_IN_SELECTION) && (children != null)) {
			Enumeration<?> e = children.elements();
			while (e.hasMoreElements()) {
				CheckNode node = (CheckNode) e.nextElement();
				node.setSelected(isSelected);
			}
		}
	}

	/**
	 * Sets the selection mode.
	 * @param mode of the new selection mode
	 */
	public void setSelectionMode(final int mode) {
		selectionMode = mode;
	}

}