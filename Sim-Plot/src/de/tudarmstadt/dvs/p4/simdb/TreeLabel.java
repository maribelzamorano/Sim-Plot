package de.tudarmstadt.dvs.p4.simdb;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/**
 * The Class TreeLabel sets the label's properties of the databases tree.
 */
public class TreeLabel extends JLabel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The is selected. */
	boolean isSelected;

	/** The has focus. */
	boolean hasFocus;

	/**
	 * Instantiates a new tree label.
	 */
	public TreeLabel() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension retDimension = super.getPreferredSize();
		if (retDimension != null) {
			retDimension = new Dimension(retDimension.width + 3,
					retDimension.height);
		}
		return retDimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		String str;
		if ((str = getText()) != null) {
			if (0 < str.length()) {
				if (isSelected) {
					g.setColor(UIManager.getColor("Tree.selectionBackground"));
				} else {
					g.setColor(UIManager.getColor("Tree.textBackground"));
				}
				Dimension d = getPreferredSize();
				int imageOffset = 0;
				Icon currentI = getIcon();
				if (currentI != null) {
					imageOffset = currentI.getIconWidth()
							+ Math.max(0, getIconTextGap() - 1);
				}
				g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);
				if (hasFocus) {
					g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
					g.drawRect(imageOffset, 0, d.width - 1 - imageOffset,
							d.height - 1);
				}
			}
		}
		super.paint(g);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setBackground(java.awt.Color)
	 */
	@Override
	public void setBackground(Color color) {
		if (color instanceof ColorUIResource)
			color = null;
		super.setBackground(color);
	}

	/**
	 * Sets the focus.
	 * 
	 * @param hasFocus
	 */
	public void setFocus(boolean hasFocus) {
		this.hasFocus = hasFocus;
	}

	/**
	 * Sets the selected.
	 * 
	 * @param isSelected
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
