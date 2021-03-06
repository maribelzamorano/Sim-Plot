package de.tudarmstadt.dvs.p4.simdb;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

/**
 * The Class SimDBUI creates a GUI in which the user is able to plot graphics of
 * statistics with specific properties.
 */
public class SimDBUI extends JFrame {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The selected statistics scroll pane. */
	private static JScrollPane selectedStatisticsScrollPane;

	/** The export history button. */
	private static JButton SaveToHistoryButton, importHistoryButton,
			exportHistoryButton;

	/** The selected statistics list. */
	private static JList selectedStatisticsList;

	/** The selected statistics array list. */
	private static List<String> selectedStatisticsArrayList = new ArrayList<String>();

	/** The History list scroll pane. */
	private static JScrollPane HistoryScrollPane;

	/** The History list of plotted graphics. */
	private static JList HistoryList;

	/** The content pane. */
	private static JPanel contentPane;

	/** The History list label. */
	private static JLabel HistoryLabel = new JLabel();

	/** The database and statistic to plot. */
	private static String statisticsToPlot, HistoryOfstatisticId = "";

	/** Options defined in the GUI. */
	private static boolean several, YDifferentScales, minMax, aggregate,
			errorBars, historyIndex;

	/** The Statistics history list. */
	private static ArrayList<String> StatisticsHistoryList = new ArrayList<String>();

	/** The Databases list. */
	private static List<String> DatabasesList = new ArrayList<String>();

	/** The history list index no. */
	private static int historyIndexNo;

	/** The databases tree container. */
	private static JPanel treeContainer;

	/** The panel tree. */
	private static JScrollPane panelTree;

	/** The north split pane. */
	private static JSplitPane northSplitPane;

	/** The Exit button. */
	private static JButton Exit;

	/** The Plot button. */
	private static JButton PlotButton;

	/** The continue button. */
	private JButton continueButton;

	/** The cancel button. */
	private JButton cancelButton;

	/** The browse database button. */
	private JButton browseDatabaseButton;

	/** The tree. */
	private static Tree tree;

	/** The aggregate text field. */
	private static JTextField aggregateTextField;

	/** The aggregation check box. */
	private static JCheckBox aggregationCheckBox;

	/** The min max button. */
	private static JCheckBox minMaxButton;

	/** The error bars button. */
	private static JCheckBox errorBarsButton;

	/** The Y scales button. */
	private static JCheckBox YScalesButton;

	/** The graphic. */
	private static Graphic graphic;
	private static RecentFileOptions recentMenu;

	/**
	 * The main method creates a new frame and sets gnuplot as argument.
	 * 
	 * @param args
	 *            the arguments
	 * @throws SimDBException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void main(final String[] args) throws SimDBException,
			IOException, SQLException {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final SimDBUI frame = new SimDBUI();

					frame.setIconImage(new ImageIcon(
							getClass()
									.getClassLoader()
									.getResource(
											"de/tudarmstadt/dvs/p4/util/icons/graph_icon.gif"))
							.getImage());
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setBounds(100, 100, 527, 670);
					frame.setLocation(708, 50);
					frame.setVisible(true);
					Graphic.setGnuplot(args.length == 0 ? "gnuplot" : args[0]);
					createTreeInterface();
					frame.setContentPane(contentPane);
					frame.setTitle("Sim-Plot");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * This method calls the class Tree to create the database tree nodes, and
	 * then it adds the tree into the interface. Creates the interface elements
	 * that are going to be used with the tree.
	 * 
	 * @throws SimDBException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void createTreeInterface() throws SimDBException,
			IOException, SQLException {

		contentPane = new JPanel(new BorderLayout());

		contentPane.setLayout(new BorderLayout());
		{
			JMenuBar menuBar = new JMenuBar();
			contentPane.add(menuBar, BorderLayout.NORTH);
			recentMenu = new RecentFileOptions("Recent Databases", 10) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void onSelectFile(String filePath) {
					onRecentFile(filePath);
				}
			};

			menuBar.add(recentMenu);
			recentMenu.setBackground(contentPane.getBackground());
			menuBar.setBackground(contentPane.getBackground());
			JPanel panelSouth2 = new JPanel(new BorderLayout());
			contentPane.add(panelSouth2, BorderLayout.SOUTH);

			{
				JPanel buttonsPanel2 = new JPanel();
				panelSouth2.add(buttonsPanel2, BorderLayout.EAST);
				{
					Exit = new JButton();
					buttonsPanel2.add(Exit, BorderLayout.WEST);
					JLabel exitButtonLabel = new JLabel();
					Exit.add(exitButtonLabel);
					exitButtonLabel.setText("Exit");
					exitButtonLabel
							.setIcon(new ImageIcon(
									SimDBUI.class
											.getClassLoader()
											.getResource(
													"de/tudarmstadt/dvs/p4/util/icons/exit.png")));

				}

			}
		}

		setTree(new Tree());

		contentPane.add(new SplitContainer().getJSplitPane(),
				BorderLayout.CENTER);

		getSaveToHistoryButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				try {
					int selectedIx;
					if (getHistoryList().isSelectionEmpty() == false) {

						selectedIx = getHistoryList().getSelectedIndex();
					} else {
						selectedIx = 0;
					}
					try {
						setGraphic(new Graphic());
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						getGraphic().saveToHistoryList(selectedIx);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					String modifiedHistoryElement = getStatisticsHistoryList()
							.get(selectedIx);
					if (modifiedHistoryElement.contains("*") == false) {
						modifiedHistoryElement = "* "
								+ getStatisticsHistoryList().get(selectedIx);
					}
					getStatisticsHistoryList().set(selectedIx,
							modifiedHistoryElement);
					getHistoryList().setListData(
							getStatisticsHistoryList().toArray());
					getHistoryList().repaint();

				} catch (SimDBException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		getExportHistoryButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {

				try {
					exportButtonAction();
				} catch (SimDBException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});

		getImportHistoryButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {

				try {
					importButtonAction();
				} catch (SimDBException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});

		Exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				try {
					if (Graphic.getGp() != null) {
						Graphic.getGp().close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);

			}
		});

	}

	/**
	 * Adds plotted graphic the to history list.
	 * 
	 * @param StatisticId
	 */
	public static void addToHistoryList(String StatisticId) {

		if (isMinMax() == true) {
			StatisticId = "(Max and Min)" + StatisticId;
			setMinMax(true);
		}
		if (isErrorBars() == true) {
			StatisticId = "(Error Bars)" + StatisticId;
			setErrorBars(true);
		}
		if (isYDifferentScales() == true) {
			StatisticId = "(Different Y Scales)" + StatisticId;
			setYDifferentScales(true);
		}
		if (isAggregate() == true) {
			StatisticId = "(Aggregate)" + StatisticId;
			setAggregate(true);
		}

		getStatisticsHistoryList().add(0, StatisticId);

	}

	/**
	 * When the button "Export" is pressed, a string containing specific
	 * properties of a graphic selected in the "History of graphics" list will
	 * be showed and the user will be able to copy it and paste it later to
	 * reproduce the same graphic.
	 * 
	 * @see ExportContainer.java
	 * @throws SimDBException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void exportButtonAction() throws SimDBException, IOException,
			SQLException {
		setHistoryIndex(true);
		int selectedIx;
		if (getHistoryList().isSelectionEmpty() == false) {

			selectedIx = getHistoryList().getSelectedIndex();
		} else {
			selectedIx = 0;
		}

		String plotConfiguration = Graphic.getHistoryListOfPlots().get(
				selectedIx);

		ExportContainer exportFrame = new ExportContainer();
		exportFrame.initExportDialog();
		exportFrame.setExportText(plotConfiguration);
		exportFrame.setVisible(true);

	}

	/**
	 * When the button "Import" is pressed, an previously exported string
	 * containing specific properties of a graphic selected will be examined and
	 * the GUI will plot the respective graphic and will save it in the
	 * "History of graphics" list.
	 * 
	 * @see ImportContainer.java
	 * @throws SimDBException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void importButtonAction() throws SimDBException, IOException,
			SQLException {

		setGraphic(new Graphic());

		ImportContainer importFrame = new ImportContainer();
		importFrame.initImportDialog();
		importFrame.setVisible(true);

	}

	public static int getHistoryIndexNo() {
		return historyIndexNo;
	}

	public static void setHistoryIndexNo(int historyIndexNo) {
		SimDBUI.historyIndexNo = historyIndexNo;
	}

	public static ArrayList<String> getStatisticsHistoryList() {
		return StatisticsHistoryList;
	}

	public static void setStatisticsHistoryList(
			ArrayList<String> statisticsHistoryList) {
		StatisticsHistoryList = statisticsHistoryList;
	}

	public static JButton getSaveToHistoryButton() {
		return SaveToHistoryButton;
	}

	public static void setSaveToHistoryButton(JButton saveToHistoryButton) {
		SaveToHistoryButton = saveToHistoryButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public JButton getBrowseDatabaseButton() {
		return browseDatabaseButton;
	}

	public void setBrowseDatabaseButton(JButton browseDatabaseButton) {
		this.browseDatabaseButton = browseDatabaseButton;
	}

	public JButton getContinueButton() {
		return continueButton;
	}

	public void setContinueButton(JButton continueButton) {
		this.continueButton = continueButton;
	}

	public static List<String> getDatabasesList() {
		return DatabasesList;
	}

	public static void setDatabasesList(List<String> databasesList) {
		DatabasesList = databasesList;
	}

	public static JPanel getTreeContainer() {
		return treeContainer;
	}

	public static void setTreeContainer(JPanel treeContainer) {
		SimDBUI.treeContainer = treeContainer;
	}

	/**
	 * @return the panelTree
	 */
	public static JScrollPane getPanelTree() {
		return panelTree;
	}

	/**
	 * @param panelTree
	 *            the panelTree to set
	 */
	public static void setPanelTree(JScrollPane panelTree) {
		SimDBUI.panelTree = panelTree;
	}

	/**
	 * @return the tree
	 */
	public static Tree getTree() {
		return tree;
	}

	/**
	 * @param tree
	 *            the tree to set
	 */
	public static void setTree(Tree tree) {
		SimDBUI.tree = tree;
	}

	/**
	 * @return the aggregateTextField
	 */
	public static JTextField getAggregateTextField() {
		return aggregateTextField;
	}

	/**
	 * @param aggregateTextField
	 *            the aggregateTextField to set
	 */
	public static void setAggregateTextField(JTextField aggregateTextField) {
		SimDBUI.aggregateTextField = aggregateTextField;
	}

	/**
	 * @return the historyList
	 */
	public static JList getHistoryList() {
		return HistoryList;
	}

	/**
	 * @param historyList
	 *            the historyList to set
	 */
	public static void setHistoryList(JList historyList) {
		HistoryList = historyList;
	}

	/**
	 * @return the selectedStatisticsArrayList
	 */
	public static List<String> getSelectedStatisticsArrayList() {
		return selectedStatisticsArrayList;
	}

	/**
	 * @param selectedStatisticsArrayList
	 *            the selectedStatisticsArrayList to set
	 */
	public static void setSelectedStatisticsArrayList(
			List<String> selectedStatisticsArrayList) {
		SimDBUI.selectedStatisticsArrayList = selectedStatisticsArrayList;
	}

	/**
	 * @return the selectedStatisticsList
	 */
	public static JList getSelectedStatisticsList() {
		return selectedStatisticsList;
	}

	/**
	 * @param selectedStatisticsList
	 *            the selectedStatisticsList to set
	 */
	public static void setSelectedStatisticsList(JList selectedStatisticsList) {
		SimDBUI.selectedStatisticsList = selectedStatisticsList;
	}

	/**
	 * @return the errorBarsButton
	 */
	public static JCheckBox getErrorBarsButton() {
		return errorBarsButton;
	}

	/**
	 * @param errorBarsButton
	 *            the errorBarsButton to set
	 */
	public static void setErrorBarsButton(JCheckBox errorBarsButton) {
		SimDBUI.errorBarsButton = errorBarsButton;
	}

	/**
	 * @return the yDifferentScales
	 */
	public static boolean isYDifferentScales() {
		return YDifferentScales;
	}

	/**
	 * @param yDifferentScales
	 *            the yDifferentScales to set
	 */
	public static void setYDifferentScales(boolean yDifferentScales) {
		YDifferentScales = yDifferentScales;
	}

	/**
	 * @return the yScalesButton
	 */
	public static JCheckBox getYScalesButton() {
		return YScalesButton;
	}

	/**
	 * @param yScalesButton
	 *            the yScalesButton to set
	 */
	public static void setYScalesButton(JCheckBox yScalesButton) {
		YScalesButton = yScalesButton;
	}

	/**
	 * @return the minMaxButton
	 */
	public static JCheckBox getMinMaxButton() {
		return minMaxButton;
	}

	/**
	 * @param minMaxButton
	 *            the minMaxButton to set
	 */
	public static void setMinMaxButton(JCheckBox minMaxButton) {
		SimDBUI.minMaxButton = minMaxButton;
	}

	/**
	 * @return the several
	 */
	public static boolean isSeveral() {
		return several;
	}

	/**
	 * @param several
	 *            the several to set
	 */
	public static void setSeveral(boolean several) {
		SimDBUI.several = several;
	}

	/**
	 * @return the selectedStatisticsScrollPane
	 */
	public static JScrollPane getSelectedStatisticsScrollPane() {
		return selectedStatisticsScrollPane;
	}

	/**
	 * @param selectedStatisticsScrollPane
	 *            the selectedStatisticsScrollPane to set
	 */
	public static void setSelectedStatisticsScrollPane(
			JScrollPane selectedStatisticsScrollPane) {
		SimDBUI.selectedStatisticsScrollPane = selectedStatisticsScrollPane;
	}

	/**
	 * @return the minMax
	 */
	public static boolean isMinMax() {
		return minMax;
	}

	/**
	 * @param minMax
	 *            the minMax to set
	 */
	public static void setMinMax(boolean minMax) {
		SimDBUI.minMax = minMax;
	}

	/**
	 * @return the errorBars
	 */
	public static boolean isErrorBars() {
		return errorBars;
	}

	/**
	 * @param errorBars
	 *            the errorBars to set
	 */
	public static void setErrorBars(boolean errorBars) {
		SimDBUI.errorBars = errorBars;
	}

	/**
	 * @return the statisticsToPlot
	 */
	public static String getStatisticsToPlot() {
		return statisticsToPlot;
	}

	/**
	 * @param statisticsToPlot
	 *            the statisticsToPlot to set
	 */
	public static void setStatisticsToPlot(String statisticsToPlot) {
		SimDBUI.statisticsToPlot = statisticsToPlot;
	}

	/**
	 * @return the aggregationCheckBox
	 */
	public static JCheckBox getAggregationCheckBox() {
		return aggregationCheckBox;
	}

	/**
	 * @param aggregationCheckBox
	 *            the aggregationCheckBox to set
	 */
	public static void setAggregationCheckBox(JCheckBox aggregationCheckBox) {
		SimDBUI.aggregationCheckBox = aggregationCheckBox;
	}

	/**
	 * @return the aggregate
	 */
	public static boolean isAggregate() {
		return aggregate;
	}

	/**
	 * @param aggregate
	 *            the aggregate to set
	 */
	public static void setAggregate(boolean aggregate) {
		SimDBUI.aggregate = aggregate;
	}

	/**
	 * @return the plotButton
	 */
	public static JButton getPlotButton() {
		return PlotButton;
	}

	/**
	 * @param plotButton
	 *            the plotButton to set
	 */
	public static void setPlotButton(JButton plotButton) {
		PlotButton = plotButton;
	}

	/**
	 * @return the historyIndex
	 */
	public static boolean isHistoryIndex() {
		return historyIndex;
	}

	/**
	 * @param historyIndex
	 *            the historyIndex to set
	 */
	public static void setHistoryIndex(boolean historyIndex) {
		SimDBUI.historyIndex = historyIndex;
	}

	/**
	 * @return the historyOfstatisticId
	 */
	public static String getHistoryOfstatisticId() {
		return HistoryOfstatisticId;
	}

	/**
	 * @param historyOfstatisticId
	 *            the historyOfstatisticId to set
	 */
	public static void setHistoryOfstatisticId(String historyOfstatisticId) {
		HistoryOfstatisticId = historyOfstatisticId;
	}

	/**
	 * @return the graphic
	 */
	public static Graphic getGraphic() {
		return graphic;
	}

	/**
	 * @param graphic
	 *            the graphic to set
	 */
	public static void setGraphic(Graphic graphic) {
		SimDBUI.graphic = graphic;
	}

	/**
	 * @return the importHistoryButton
	 */
	public static JButton getImportHistoryButton() {
		return importHistoryButton;
	}

	/**
	 * @param importHistoryButton
	 *            the importHistoryButton to set
	 */
	public static void setImportHistoryButton(JButton importHistoryButton) {
		SimDBUI.importHistoryButton = importHistoryButton;
	}

	/**
	 * @return the exportHistoryButton
	 */
	public static JButton getExportHistoryButton() {
		return exportHistoryButton;
	}

	/**
	 * @param exportHistoryButton
	 *            the exportHistoryButton to set
	 */
	public static void setExportHistoryButton(JButton exportHistoryButton) {
		SimDBUI.exportHistoryButton = exportHistoryButton;
	}

	/**
	 * @return the historyScrollPane
	 */
	public static JScrollPane getHistoryScrollPane() {
		return HistoryScrollPane;
	}

	/**
	 * @param historyScrollPane
	 *            the historyScrollPane to set
	 */
	public static void setHistoryScrollPane(JScrollPane historyScrollPane) {
		HistoryScrollPane = historyScrollPane;
	}

	/**
	 * @return the historyLabel
	 */
	public static JLabel getHistoryLabel() {
		return HistoryLabel;
	}

	/**
	 * @param historyLabel
	 *            the historyLabel to set
	 */
	public static void setHistoryLabel(JLabel historyLabel) {
		HistoryLabel = historyLabel;
	}

	/**
	 * @return the northSplitPane
	 */
	public static JSplitPane getNorthSplitPane() {
		return northSplitPane;
	}

	/**
	 * @param northSplitPane
	 *            the northSplitPane to set
	 */
	public static void setNorthSplitPane(JSplitPane northSplitPane) {
		SimDBUI.northSplitPane = northSplitPane;
	}

	public static void addRecentEntry(String filePath) {
		recentMenu.addEntry(filePath);
	}

	public static void onRecentFile(String filePath) {
		DatabasesTree treeDB = new DatabasesTree();
		DatabasesTree.setDatabasePath(filePath);
		if (filePath.trim().isEmpty() == false) {
			if (getDatabasesList().size() == 0) {
				getDatabasesList().add(0, DatabasesTree.getDatabasePath());
			} else {
				getDatabasesList().add(getDatabasesList().size(),
						DatabasesTree.getDatabasePath());
			}

		}
		treeDB.updateDatabaseTree();
	}
}