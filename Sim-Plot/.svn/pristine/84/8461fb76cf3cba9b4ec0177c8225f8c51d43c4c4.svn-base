package de.tudarmstadt.dvs.p4.simdb;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JList;
/**
 * The Class ActionHistoryList plots a graphic saved in the History of graphics list
 * when the user double clicked on it.
 */
public class ActionHistoryList extends MouseAdapter {

	/** The HistoryList. */
	protected JList HistoryList;

	/** The graphic. */
	private static Graphic graphic;

	/**
	 * Instantiates a new action Jlist.
	 * 
	 * @param historyList the HistoryList
	 */
	public ActionHistoryList(final JList historyList) {
		this.HistoryList = historyList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	/**
	 * In the part "History of graphics" of the GUI, the user can see a list of
	 * the graphics already plotted, in this list the user can double click on
	 * one of the graphics and this graphic will be plotted again with the
	 * respective properties saved.
	 * @param e mouse event
	 */
	@Override
	public final void mouseClicked(final MouseEvent e) {

		if (e.getClickCount() == 2) {

			int index = HistoryList.locationToIndex(e.getPoint());

			HistoryList.ensureIndexIsVisible(index);

			try {
				graphic = new Graphic();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

			String statisticId = "";
			int selectedIx = HistoryList.getSelectedIndex();
			SimDBUI.setHistoryIndexNo(selectedIx);
			SimDBUI.getStatisticsHistoryList().get(selectedIx);
			statisticId = String.valueOf(selectedIx);

			try {
				graphic.prepareToPlot(statisticId, false, false, false, false,
						false, true);

			} catch (SimDBException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			SimDBUI.getSaveToHistoryButton().setVisible(true);

		}
	}
}
