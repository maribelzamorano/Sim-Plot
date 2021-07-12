package de.tudarmstadt.dvs.p4.simdb;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeNode;

public class SelectableTree extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean fWholeRowSelectionEnabled;
	private MouseAdapter fRowSelectionListener;
	final SelectableTree fThis;

	public SelectableTree(TreeNode rootNode) {
		super(rootNode);
		fThis = this;
		init();
	}

	public SelectableTree() {
		fThis = this;
		init();
	}

	private void init() {
		fRowSelectionListener = new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					int closestRow = fThis.getClosestRowForLocation(e.getX(),
							e.getY());
					Rectangle closestRowBounds = fThis.getRowBounds(closestRow);
					if (e.getY() >= closestRowBounds.getY()
							&& e.getY() < closestRowBounds.getY()
									+ closestRowBounds.getHeight()) {
						if (e.getX() > closestRowBounds.getX()
								&& closestRow < fThis.getRowCount()) {

							fThis.setSelectionRow(closestRow);
						}

					} else
						fThis.setSelectionRow(-1);
				}
			}

		};
		setWholeRowSelectionEnabled(true);
	}

	public void setWholeRowSelectionEnabled(boolean wholeRowSelectionEnabled) {
		fWholeRowSelectionEnabled = wholeRowSelectionEnabled;
		if (fWholeRowSelectionEnabled)
			addMouseListener(fRowSelectionListener);
		else
			removeMouseListener(fRowSelectionListener);
	}

	public boolean isWholeRowSelectionEnabled() {
		return fWholeRowSelectionEnabled;
	}

}