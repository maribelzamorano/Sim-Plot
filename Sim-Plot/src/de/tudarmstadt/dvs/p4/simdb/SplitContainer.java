package de.tudarmstadt.dvs.p4.simdb;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * The Class SplitContainer creates a layout where the elements of the GUI can
 * be resized easily.
 */
public class SplitContainer extends SimDBUI {

	/**
	 * Instantiates a new split container.
	 * 
	 * @throws SimDBException
	 * @throws IOException
	 * @throws SQLException
	 */
	public SplitContainer() throws SimDBException, IOException, SQLException {
		super();

	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Gets the panel that contains the databases tree.
	 * 
	 * @return the panel tree
	 * @throws SimDBException
	 * @throws SQLException
	 * @throws IOException
	 */
	static JPanel getJPanelTree() throws SimDBException, SQLException,
			IOException {

		setTreeContainer(new JPanel());
		setPanelTree(getTree().createTree());
		TableLayout jPanel1Layout = new TableLayout(new double[][] {
				{ TableLayoutConstants.FILL, TableLayoutConstants.FILL },
				{ TableLayoutConstants.FILL } });

		getTreeContainer().setLayout(jPanel1Layout);
		{
			getTreeContainer().add(getPanelTree(), "0, 0,1,0,f,f");
			getTreeContainer().setPreferredSize(
					getTreeContainer().getMaximumSize());
		}

		getTreeContainer().repaint();
		return getTreeContainer();
	}

	/**
	 * Gets the north split pane.
	 * 
	 * @return the north j split pane
	 * @throws SimDBException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static JSplitPane getNorthJSplitPane() throws SimDBException,
			SQLException, IOException {
		OptionsContainer OptsContainer = new OptionsContainer();
		setNorthSplitPane(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				getJPanelTree(), OptsContainer.getOptionsContainer()));

		double weight = getNorthSplitPane().getResizeWeight(); // Get current
																// weight

		// Keep the size of the right component constant
		weight = 1D;
		getNorthSplitPane().setResizeWeight(weight);

		// Split the space evenly
		weight = .5D;
		getNorthSplitPane().setResizeWeight(weight);

		getNorthSplitPane().setOneTouchExpandable(true);
		getNorthSplitPane().repaint();

		return getNorthSplitPane();
	}

	/**
	 * Gets the main split pane.
	 * 
	 * @return the split pane
	 * @throws SimDBException
	 * @throws SQLException
	 * @throws IOException
	 */
	public JSplitPane getJSplitPane() throws SimDBException, SQLException,
			IOException {

		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane.setTopComponent(getNorthJSplitPane());
		mainSplitPane.setBottomComponent(getSudJSplitPane());

		mainSplitPane.setDividerLocation(400);
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.repaint();
		return mainSplitPane;
	}

	/**
	 * Gets the sud split pane.
	 * 
	 * @return the sud split pane
	 * @throws SimDBException
	 * @throws SQLException
	 * @throws IOException
	 */
	public JSplitPane getSudJSplitPane() throws SimDBException, SQLException,
			IOException {

		setHistoryScrollPane(new JScrollPane());
		getHistoryScrollPane().setBorder(null);
		getHistoryScrollPane().setVisible(false);
		{
			setHistoryList(new JList());
			getHistoryList().setBackground(getContentPane().getBackground());
			getHistoryScrollPane().setViewportView(getHistoryList());
			getHistoryList().addMouseListener(
					new ActionHistoryList(getHistoryList()));
			getHistoryList().setVisible(false);
		}

		getHistoryLabel().setText("History of graphics:");

		setSaveToHistoryButton(new JButton());
		getSaveToHistoryButton().setVisible(false);
		getSaveToHistoryButton().setIcon(
				new ImageIcon(getClass().getClassLoader().getResource(
						"de/tudarmstadt/dvs/p4/util/icons/refresh.png")));
		getSaveToHistoryButton().setText("Save change to History");

		setExportHistoryButton(new JButton());
		getExportHistoryButton().setVisible(false);
		getExportHistoryButton().setIcon(
				new ImageIcon(getClass().getClassLoader().getResource(
						"de/tudarmstadt/dvs/p4/util/icons/export.png")));
		getExportHistoryButton().setText("Export");

		setImportHistoryButton(new JButton());
		getImportHistoryButton().setVisible(false);
		getImportHistoryButton().setIcon(
				new ImageIcon(getClass().getClassLoader().getResource(
						"de/tudarmstadt/dvs/p4/util/icons/import.png")));
		getImportHistoryButton().setText("Import");

		JPanel historyPanel = new JPanel();

		TableLayout historyTableLayout = new TableLayout(new double[][] {
				{ TableLayoutConstants.FILL, TableLayoutConstants.FILL },
				{ 20.0, TableLayoutConstants.FILL, 20.0, 5.0, 20.0 } });

		historyPanel.setLayout(historyTableLayout);
		{
			historyPanel.add(getHistoryLabel(), "0, 0,1, 0,c,c");
			historyPanel.add(getHistoryScrollPane(), "0, 1,1, 1,f,f");
			historyPanel.add(getSaveToHistoryButton(), "0, 2,1,2,c,c");
			historyPanel.add(getExportHistoryButton(), "0, 4,r,c");
			historyPanel.add(getImportHistoryButton(), "1, 4,l,c");
		}

		JLabel selectedStatisticsLabel = new JLabel();
		selectedStatisticsLabel.setText("Selected Statistics:");

		setSelectedStatisticsScrollPane(new JScrollPane());
		getSelectedStatisticsScrollPane().setBorder(null);
		{
			setSelectedStatisticsList(new JList());
			getSelectedStatisticsList().setBackground(
					getContentPane().getBackground());
			getSelectedStatisticsScrollPane().setViewportView(
					getSelectedStatisticsList());
		}

		JPanel selectedStatisticsPanel = new JPanel();

		TableLayout selectedStatisticsTableLayout = new TableLayout(
				new double[][] { { TableLayoutConstants.FILL },
						{ 20.0, TableLayoutConstants.FILL } });

		selectedStatisticsPanel.setLayout(selectedStatisticsTableLayout);
		{
			selectedStatisticsPanel.add(selectedStatisticsLabel, "0, 0,c,c");
			selectedStatisticsPanel.add(getSelectedStatisticsScrollPane(),
					"0, 1,f,f");
		}
		JSplitPane sudSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				historyPanel, selectedStatisticsPanel);
		double weight = sudSplitPane.getResizeWeight();
		weight = 1D;
		sudSplitPane.setResizeWeight(weight);
		weight = .5D;
		sudSplitPane.setResizeWeight(weight);

		sudSplitPane.setOneTouchExpandable(true);
		sudSplitPane.repaint();
		return sudSplitPane;

	}

}
