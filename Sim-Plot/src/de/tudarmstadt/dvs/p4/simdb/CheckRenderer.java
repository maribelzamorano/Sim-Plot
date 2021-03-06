package de.tudarmstadt.dvs.p4.simdb;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 * The Class CheckRenderer sets the properties of the databases trees in the GUI
 * like adding the correspondent checkboxes and labels.
 */
public class CheckRenderer extends JPanel implements TreeCellRenderer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The checkBox. */
	private JCheckBox checkBox;

	/** The label. */
	private TreeLabel label;

	/** The panel. */
	private JPanel panel;

	/**
	 * Instantiates a new check renderer.
	 */
	public CheckRenderer() {
		setLayout(null);
		add(checkBox = new JCheckBox());
		add(label = new TreeLabel());

		panel = new JPanel();
		panel.setOpaque(false);

		checkBox.setBackground(UIManager.getColor("Tree.textBackground"));
		label.setForeground(UIManager.getColor("Tree.textForeground"));
		Font f = label.getFont();

		label.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#doLayout()
	 */
	/**
	 * Creates the layout for the checkboxes and labes.
	 */
	@Override
	public final void doLayout() {
		Dimension dCheck = checkBox.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
		int yCheck = 0;
		int yLabel = 0;
		if (dCheck.height < dLabel.height) {
			yCheck = (dLabel.height - dCheck.height) / 2;
		} else {
			yLabel = (dCheck.height - dLabel.height) / 2;
		}
		checkBox.setLocation(0, yCheck);
		checkBox.setBounds(0, yCheck, dCheck.width, dCheck.height);
		label.setLocation(dCheck.width, yLabel);
		label.setBounds(dCheck.width, yLabel, dLabel.width, dLabel.height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public final Dimension getPreferredSize() {
		Dimension dCheck = checkBox.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
		return new Dimension(dCheck.width + dLabel.width,
				(dCheck.height < dLabel.height ? dLabel.height : dCheck.height));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.
	 * swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public final Component getTreeCellRendererComponent(JTree tree,
			Object value, final boolean isSelected, final boolean expanded,
			final boolean leaf, final int row, final boolean hasFocus) {

		String stringValue = tree.convertValueToText(value, isSelected,
				expanded, leaf, row, hasFocus);
		setEnabled(tree.isEnabled());

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		if (node.getUserObject() instanceof CheckNode) {
			CheckNode splitNode = (CheckNode) node.getUserObject();
			checkBox.setSelected((splitNode).isSelected());
			label.setFont(tree.getFont());
			if (stringValue.contains("#")) {
				int index = stringValue.indexOf("#");
				label.setText("<html><font size=\"3\" color=\"000000\"><i>"
						+ stringValue.substring(0, index)
						+ "</i></font><font size=\"3\" color=\"FFFFFF\"><i>"
						+ stringValue.substring(index) + "</i></font></html>");// .setText(stringValue);
			} else {
				label.setText(stringValue);
			}
			panel.add(checkBox);
			panel.add(label);
			checkBox.setBackground(UIManager.getColor("Tree.textBackground"));
			label.setForeground(UIManager.getColor("Tree.textForeground"));

			return panel;
		} else {

			if (stringValue.contains("db")) {
				if (tree.getLastSelectedPathComponent() != null) {
					label.setSelected(isSelected);
					Tree.isDatabaseSelected = true;
					Tree.valueOfTreeNode = tree.getLastSelectedPathComponent()
							.toString();
				} else {
					Tree.isDatabaseSelected = false;
				}
			} else {
				label.setSelected(false);

			}

			label.setForeground(UIManager.getColor("Tree.textForeground"));
			label.setText(node.toString());

			return label;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setBackground(java.awt.Color)
	 */
	@Override
	public final void setBackground(Color color) {
		if (color instanceof ColorUIResource) {
			color = null;
		}
		super.setBackground(color);
	}
}