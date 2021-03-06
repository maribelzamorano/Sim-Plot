package de.tudarmstadt.dvs.p4.simdb;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * The listener interface for receiving nodeSelection events in the databases
 * tree of the GUI. When the nodeSelection event occurs, the selected node is
 * displayed in the "Selected statistics" list of the GUI. Depending on how many
 * statistics are selected, some options are enabled in the GUI.(When two
 * statistics are selected the option Different Y scales is enabled).
 * @see NodeSelectionEvent
 */
class NodeSelectionListener extends MouseAdapter {

	/** The tree. */
	private JTree tree;

	/** The counter. */
	private int counter;

	/** The selected path. */
	private String statisticsId = "", lastSelectedPath = "", database = "",
			selectedPath = "";

	/**
	 * Instantiates a new node selection listener.
	 * @param tree
	 * @param value
	 */
	NodeSelectionListener(JTree tree, Object value) {
		this.tree = tree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(final MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int row = tree.getRowForLocation(x, y);
		TreePath path = tree.getPathForRow(row);

		if (path != null) {
			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) path
					.getLastPathComponent();

			if (node1.getUserObject() instanceof CheckNode) {
				CheckNode node = (CheckNode) node1.getUserObject();
				boolean isSelected = !(node.isSelected());
				node.setSelected(isSelected);
				if (SimDBUI.getHistoryList().isSelectionEmpty() == false) {
					SimDBUI.getHistoryList().clearSelection();
				}
				lastSelectedPath = tree.getLastSelectedPathComponent()
						.toString();
				selectedPath = path.toString().substring(13);
				int endIndex = selectedPath.indexOf(",");
				database = selectedPath.substring(0, endIndex);

				if (isSelected == true) {
					counter += tree.getSelectionCount();

					statisticsId += database + "=" + lastSelectedPath + ";";
					SimDBUI.getSelectedStatisticsArrayList().add(lastSelectedPath);
					SimDBUI.getSelectedStatisticsList()
							.setListData(SimDBUI.getSelectedStatisticsArrayList()
									.toArray());
					SimDBUI.getSelectedStatisticsList().repaint();
				} else {

					counter -= tree.getSelectionCount();
					SimDBUI.getSelectedStatisticsArrayList()
							.remove(lastSelectedPath);
					SimDBUI.getSelectedStatisticsList()
							.setListData(SimDBUI.getSelectedStatisticsArrayList()
									.toArray());
					SimDBUI.getSelectedStatisticsList().repaint();
					statisticsId = statisticsId.replace(database + "="
							+ lastSelectedPath + ";", "");
				}
				if (counter == 1) {
					SimDBUI.getMinMaxButton().setVisible(true);
					SimDBUI.getErrorBarsButton().setVisible(true);
					SimDBUI.getYScalesButton().setVisible(false);

					OptionsContainer.getSubpanelEast().revalidate();
					OptionsContainer.getSubpanelEast().repaint();
					SimDBUI.setYDifferentScales(false);
					SimDBUI.setSeveral(false);

				} else if (counter == 2) {
					SimDBUI.getMinMaxButton().setVisible(false);
					SimDBUI.getYScalesButton().setVisible(true);
					SimDBUI.getErrorBarsButton().setVisible(false);
					OptionsContainer.getSubpanelEast().revalidate();
					OptionsContainer.getSubpanelEast().repaint();
				} else {
					SimDBUI.getYScalesButton().setVisible(false);
					OptionsContainer.getSubpanelEast().revalidate();
					OptionsContainer.getSubpanelEast().repaint();
					SimDBUI.setYDifferentScales(false);
					SimDBUI.setSeveral(true);
				}

				SimDBUI.setStatisticsToPlot(statisticsId);

				if (counter > 1) {

					SimDBUI.getMinMaxButton().setVisible(false);
					SimDBUI.getErrorBarsButton().setVisible(false);
					OptionsContainer.getSubpanelEast().revalidate();
					OptionsContainer.getSubpanelEast().repaint();
					SimDBUI.getSelectedStatisticsScrollPane().revalidate();
					SimDBUI.getSelectedStatisticsScrollPane().repaint();
					SimDBUI.setMinMax(false);
					SimDBUI.setErrorBars(false);
					SimDBUI.setSeveral(true);

				} else {
					SimDBUI.setSeveral(false);
				}
				tree.revalidate();
				tree.repaint();
			}
		}
		SimDBUI.getPanelTree().revalidate();
		SimDBUI.getPanelTree().repaint();
	}
}