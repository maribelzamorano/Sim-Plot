package de.tudarmstadt.dvs.p4.simdb;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

/**
 * The Class OptionsContainer sets all the elements in the options menu of the
 * GUI.
 */
public class OptionsContainer extends SimDBUI {

	/** The subpanel east. */
	private static JPanel subpanelEast;

	/** The remove db label. */
	private JLabel removeDBLabel;

	/** The add db label. */
	private JLabel addDBLabel;

	/** The refresh db label. */
	private JLabel refreshDBLabel;

	/** The remove db button */
	private JButton removeDB;

	/** The add db button */
	private JButton addDB;

	/** The refresh db button */
	private JButton refreshDB;

	/** The replot button. */
	private JButton replot;

	/** The stop replot button. */
	private JButton stopReplot;

	/** The secs label. */
	private JLabel secsLabel;

	/** The txt label. */
	private JLabel txtLabel;

	/** The txt button. */
	private JButton txtButton;

	/** The replot label. */
	private JLabel replotLabel;

	/** The stop replot label. */
	private JLabel stopReplotLabel;

	/** The replot value. */
	private static boolean isReploting;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The timer for the replot function. */
	private static Timer timer;

	/**
	 * Instantiates a new options container.
	 * 
	 * @throws SimDBException
	 * @throws IOException
	 * @throws SQLException
	 */
	public OptionsContainer() throws SimDBException, IOException, SQLException {
		super();
	}

	/**
	 * Gets the options container.
	 * 
	 * @return the options container
	 */
	public JPanel getOptionsContainer() {
		setSubpanelEast(new JPanel());
		TableLayout subpanelEastLayout = new TableLayout(new double[][] {
				{ 7.0, 150.0, 40.0, 7.0 },
				{ 20.0, 54.0, 54.0, TableLayoutConstants.FILL, 54.0, 54.0,
						54.0, 54.0, 54.0, 7.0 } });
		subpanelEastLayout.setHGap(5);
		subpanelEastLayout.setVGap(5);
		setHistoryIndex(false);

		getSubpanelEast().setLayout(subpanelEastLayout);
		{
			secsLabel = new JLabel();
			secsLabel.setText("Secs.");
			secsLabel.setVisible(false);
			setAggregateTextField(new JTextField());
			getAggregateTextField().setVisible(false);
			getAggregateTextField().setPreferredSize(new Dimension(25, 20));
			getAggregateTextField().addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(final KeyEvent e) {
					char c = e.getKeyChar();
					if (!((c >= '0') && (c <= '9')
							|| (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
						getToolkit().beep();
						e.consume();
					}
				}
			});

			setAggregationCheckBox(new JCheckBox());
			getSubpanelEast().add(getAggregationCheckBox(), "1, 2, l, b");
			getAggregationCheckBox().setSelected(false);
			getAggregationCheckBox().setText("Aggregate");
			getAggregationCheckBox().addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(final ItemEvent e) {
					setAggregate((e.getStateChange() == ItemEvent.SELECTED));
					getAggregationCheckBox().repaint();
					if (isAggregate() == true) {
						secsLabel.setVisible(true);
						getAggregateTextField().setVisible(true);
					} else {
						secsLabel.setVisible(false);
						getAggregateTextField().setVisible(false);
					}

				}
			});

		}
		{
			setMinMaxButton(new JCheckBox());
			getSubpanelEast().add(getMinMaxButton(), "1, 2, l, t");
			getMinMaxButton().setSelected(false);
			getMinMaxButton().setText("Min & Max");
			getMinMaxButton().addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					setMinMax((e.getStateChange() == ItemEvent.SELECTED));
					getMinMaxButton().repaint();
				}
			});
		}
		{
			setYScalesButton(new JCheckBox());
			getSubpanelEast().add(getYScalesButton(), "1, 1, l, t");
			getYScalesButton().setText("Different Y Scales");
			getYScalesButton().setSelected(false);
			getYScalesButton().addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					setYDifferentScales((e.getStateChange() == ItemEvent.SELECTED));

				}
			});
		}
		{
			setErrorBarsButton(new JCheckBox());
			getSubpanelEast().add(getErrorBarsButton(), "1, 1, l, b");
			getErrorBarsButton().setSelected(false);
			getErrorBarsButton().setText("Error Bars");
			getErrorBarsButton().addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(final ItemEvent e) {
					setErrorBars((e.getStateChange() == ItemEvent.SELECTED));
					getErrorBarsButton().repaint();
				}
			});
		}
		txtButton = new JButton();
		{
			txtButton.setPreferredSize(new Dimension(140, 26));
			txtLabel = new JLabel();
			txtLabel.setText("Save TXT");
			txtLabel.setIcon(new ImageIcon(
					getClass()
							.getClassLoader()
							.getResource(
									"de/tudarmstadt/dvs/p4/util/icons/default_document.png")));
			txtLabel.setBounds(32, 37, 68, 16);

			txtButton.add(txtLabel);
			txtButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent ev) {

					try {
						saveTXTButtonAction();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}

		JButton savePDF = new JButton();
		{
			savePDF.setPreferredSize(new Dimension(140, 26));
			getSubpanelEast().add(savePDF, "1, 6, l, t");
			savePDF.setLayout(new BorderLayout());
			{
				JLabel savePDFLabel = new JLabel();
				savePDF.add(savePDFLabel);
				savePDFLabel.setText("Save PDF");
				savePDFLabel.setIcon(new ImageIcon(getClass().getClassLoader()
						.getResource(
								"de/tudarmstadt/dvs/p4/util/icons/save.png")));
			}
		}

		setPlotButton(new JButton());
		{
			getPlotButton().setPreferredSize(new Dimension(140, 26));
			getSubpanelEast().add(getPlotButton(), "1, 7, l, t");
			getSubpanelEast().add(getReplot(), "1, 7, l, b");
			getSubpanelEast().add(getStopReplot(), "1, 7, l, b");
			getSubpanelEast().add(txtButton, "1, 6,l,b");
			getSubpanelEast().add(getAggregateTextField(), "1,2,r,b");
			getSubpanelEast().add(secsLabel, "2, 2, l, b");
			getSubpanelEast().add(getRefreshDB(), "1, 5, l, t");
			getSubpanelEast().add(getAddDB(), "1, 4, l, t");
			getSubpanelEast().add(getRemoveDB(), "1, 4, l, b");
			// getStopReplot().setVisible(false);
			getPlotButton().setLayout(new BorderLayout());
			{
				JLabel plotLabel = new JLabel();
				getPlotButton().add(plotLabel);
				plotLabel
						.setIcon(new ImageIcon(
								getClass()
										.getClassLoader()
										.getResource(
												"de/tudarmstadt/dvs/p4/util/icons/chart_curve.png")));
				plotLabel.setText("Plot");
				plotLabel.setPreferredSize(new java.awt.Dimension(75, 17));
			}
		}
		getPlotButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {

				try {
					plotButtonAction();
				} catch (SimDBException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
		savePDF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {

				try {
					savePDFButtonAction();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		addDB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {

				try {
					DatabasesTree treeDB = new DatabasesTree();
					treeDB.initAddDatabaseDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		refreshDB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {

				try {

					DatabasesTree treeDB = new DatabasesTree();
					treeDB.updateDatabaseTree();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		removeDB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {

				try {

					if (Tree.isDatabaseSelected == true) {

						TreePath[] selectedDBNodes = Tree.tree
								.getSelectionPaths();

						for (int i = 0; i < selectedDBNodes.length; i++) {
							String selectedNodePath = selectedDBNodes[i]
									.toString();
							int index = selectedNodePath.indexOf(",");
							selectedNodePath = selectedNodePath.substring(
									index + 2, selectedNodePath.length() - 1);
							int selectedIx = getDatabasesList().indexOf(
									selectedNodePath);

							getDatabasesList().remove(selectedIx);
							Tree.tree.collapsePath(selectedDBNodes[i]);

						}
						DatabasesTree treeDB = new DatabasesTree();
						treeDB.updateDatabaseTree();

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		replot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {

				try {
					if (Graphic.getGp() != null) {
						isReploting = true;
						getStopReplot().setVisible(true);
						getStopReplotLabel().setVisible(true);
						getReplot().setVisible(false);
						getReplotLabel().setVisible(false);

						final TimerTask timerTask = new TimerTask() {
							public void run() {

								if (isReploting == true) {
									Graphic.getGp().cmd("replot;");
								} else {
									timer.cancel();
								}

							}
						};
						timer = new Timer();
						timer.scheduleAtFixedRate(timerTask, 0, 1000);
					} else {
						JOptionPane.showMessageDialog(null,
								"There is no graphic open to replot.", " ", 1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		stopReplot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {

				try {

					isReploting = false;
					getStopReplot().setVisible(false);
					getStopReplotLabel().setVisible(false);
					getReplot().setVisible(true);
					getReplotLabel().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		return getSubpanelEast();
	}

	/**
	 * Gets the adds the db button
	 * 
	 * @return the adds the db
	 */
	private JButton getAddDB() {
		if (addDB == null) {
			addDB = new JButton();
			addDB.add(getAddDBLabel());
			addDB.setPreferredSize(new Dimension(140, 26));
		}
		return addDB;
	}

	/**
	 * Gets the refresh db button
	 * 
	 * @return the refresh db
	 */
	private JButton getRefreshDB() {
		if (refreshDB == null) {
			refreshDB = new JButton();
			refreshDB.add(getRefreshDBLabel());
			refreshDB.setPreferredSize(new Dimension(140, 26));
		}
		return refreshDB;
	}

	/**
	 * Gets the remove db button.
	 * 
	 * @return the remove db button
	 */
	private JButton getRemoveDB() {
		if (removeDB == null) {
			removeDB = new JButton();
			removeDB.add(getRemoveDBLabel());
			removeDB.setPreferredSize(new Dimension(140, 26));
		}
		return removeDB;
	}

	/**
	 * Gets the replot button.
	 * 
	 * @return the replot button
	 */
	private JButton getReplot() {
		if (replot == null) {
			replot = new JButton();
			replot.add(getReplotLabel());
			replot.setPreferredSize(new Dimension(140, 26));
		}
		return replot;
	}

	/**
	 * Gets the replotlabel.
	 * 
	 * @return the replotlabel
	 */
	private JLabel getReplotLabel() {
		if (replotLabel == null) {
			replotLabel = new JLabel();
			replotLabel.setText("Replot");
			replotLabel.setIcon(new ImageIcon(
					getClass().getClassLoader().getResource(
							"de/tudarmstadt/dvs/p4/util/icons/refresh.png")));
			replotLabel.setBounds(73, 37, 93, 16);
		}
		return replotLabel;
	}

	/**
	 * Gets the stop replot button.
	 * 
	 * @return the stop replot button
	 */
	private JButton getStopReplot() {
		if (stopReplot == null) {
			stopReplot = new JButton();
			stopReplot.add(getStopReplotLabel());
			stopReplot.setPreferredSize(new Dimension(140, 26));
		}
		return stopReplot;
	}

	/**
	 * Gets the stopReplotlabel.
	 * 
	 * @return the StopReplotlabel
	 */
	private JLabel getStopReplotLabel() {
		if (stopReplotLabel == null) {
			stopReplotLabel = new JLabel();
			stopReplotLabel.setText("Stop Replot");
			stopReplotLabel
					.setIcon(new ImageIcon(
							getClass()
									.getClassLoader()
									.getResource(
											"de/tudarmstadt/dvs/p4/util/icons/stop-replot.png")));
			stopReplotLabel.setBounds(73, 37, 93, 16);
		}
		return stopReplotLabel;
	}

	/**
	 * Gets the add db label.
	 * 
	 * @return the add db label
	 */
	private JLabel getAddDBLabel() {
		if (addDBLabel == null) {
			addDBLabel = new JLabel();
			addDBLabel.setText("Add DB");
			addDBLabel.setIcon(new ImageIcon(getClass().getClassLoader()
					.getResource("de/tudarmstadt/dvs/p4/util/icons/add.png")));
			addDBLabel.setBounds(73, 37, 93, 16);
		}
		return addDBLabel;
	}

	/**
	 * Gets the refresh db label.
	 * 
	 * @return the refresh db label
	 */
	private JLabel getRefreshDBLabel() {
		if (refreshDBLabel == null) {
			refreshDBLabel = new JLabel();
			refreshDBLabel.setText("Refresh DB");
			refreshDBLabel.setIcon(new ImageIcon(getClass().getClassLoader()
					.getResource("de/tudarmstadt/dvs/p4/util/icons/add.png")));
			refreshDBLabel.setBounds(73, 37, 93, 16);
		}
		return refreshDBLabel;
	}

	/**
	 * Gets the remove db label.
	 * 
	 * @return the remove db label
	 */
	private JLabel getRemoveDBLabel() {
		if (removeDBLabel == null) {
			removeDBLabel = new JLabel();
			removeDBLabel.setText("Remove DB");
			removeDBLabel
					.setIcon(new ImageIcon(
							getClass()
									.getClassLoader()
									.getResource(
											"de/tudarmstadt/dvs/p4/util/icons/delete.png")));
			removeDBLabel.setBounds(112, 38, 114, 16);
		}
		return removeDBLabel;
	}

	/**
	 * This method calls @see plotMeasurements to generate a PDF of the current
	 * graphic.
	 * 
	 * @throws SQLException
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws SimDBException
	 */
	public static void savePDFButtonAction() throws SQLException,
			InterruptedException, IOException, SimDBException {

		String statisticId = "";

		if (getHistoryList().isSelectionEmpty() == false) {
			setHistoryIndex(true);
			int selectedIx = getHistoryList().getSelectedIndex();
			setHistoryIndexNo(selectedIx);
			statisticId = String.valueOf(selectedIx);
		} else if (getStatisticsToPlot() != null) {
			setHistoryIndex(false);
			if (isSeveral() == true) {
				String[] multipleStatistics = getStatisticsToPlot().split(";");
				for (int i = 0; i < multipleStatistics.length; i++) {
					statisticId += multipleStatistics[i] + ";";
					setMinMax(false);
					setErrorBars(false);
				}
			} else {

				int endIndex = getStatisticsToPlot().indexOf(";");
				statisticId = getStatisticsToPlot().substring(0, endIndex);

			}

		}
		Document pdf = new Document();
		pdf.createDocument("PDF", statisticId, isMinMax(), isAggregate(),
				isErrorBars(), isSeveral(), isYDifferentScales(),
				isHistoryIndex());
		getHistoryList().clearSelection();

	}

	/**
	 * This method calls @see plotMeasurements to generate a text file of the
	 * current graphic.
	 * 
	 * @throws SQLException
	 *             the sQL exception
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SimDBException
	 *             the sim db exception
	 */
	public static void saveTXTButtonAction() throws SQLException,
			InterruptedException, IOException, SimDBException {

		String statisticId = "";

		if (getHistoryList().isSelectionEmpty() == false) {
			setHistoryIndex(true);
			int selectedIx = getHistoryList().getSelectedIndex();
			setHistoryIndexNo(selectedIx);
			statisticId = String.valueOf(selectedIx);
		} else if (getStatisticsToPlot() != null) {

			setHistoryIndex(false);
			if (isSeveral() == true) {
				String[] multipleStatistics = getStatisticsToPlot().split(";");
				for (int i = 0; i < multipleStatistics.length; i++) {
					statisticId += multipleStatistics[i] + ";";
					setMinMax(false);
					setErrorBars(false);
				}
			} else {

				int endIndex = getStatisticsToPlot().indexOf(";");
				statisticId = getStatisticsToPlot().substring(0, endIndex);

			}

		}
		Document doc = new Document();
		doc.createDocument("TXT", statisticId, isMinMax(), isAggregate(),
				isErrorBars(), isSeveral(), isYDifferentScales(),
				isHistoryIndex());
		getHistoryList().clearSelection();

	}

	/**
	 * This method contains the actions that will occur every time the button
	 * "Plot" is pressed(plot a graphic with the selected properties and save
	 * the graphic in the "History of graphics" list).
	 * 
	 * @throws SimDBException
	 *             the sim db exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SQLException
	 *             the sQL exception
	 */
	public static void plotButtonAction() throws SimDBException, IOException,
			SQLException {

		if (getYScalesButton().isSelected() == false) {
			setYDifferentScales(false);
		}
		if (getErrorBarsButton().isSelected() == false) {
			setErrorBars(false);
		}
		if (getMinMaxButton().isSelected() == false) {
			setMinMax(false);
		}
		if (getAggregationCheckBox().isSelected() == false) {
			setAggregate(false);
		}

		setGraphic(new Graphic());

		String statisticId = "";
		if (getStatisticsToPlot() != null) {

			if (isSeveral() == true) {
				String[] multipleStatistics = getStatisticsToPlot().split(";");
				setHistoryOfstatisticId("");
				for (int i = 0; i < multipleStatistics.length; i++) {
					statisticId += multipleStatistics[i] + ";";
				}
				setMinMax(false);
				setErrorBars(false);

			} else {

				int endIndex = getStatisticsToPlot().indexOf(";");
				statisticId = getStatisticsToPlot().substring(0, endIndex);

			}

			try {

				getGraphic().prepareToPlot(statisticId, isMinMax(),
						isAggregate(), isErrorBars(), isSeveral(),
						isYDifferentScales(), isHistoryIndex());
				addToHistoryList(Graphic.getHistoryStatistics());
				getHistoryList().setListData(
						getStatisticsHistoryList().toArray());

				if (getSaveToHistoryButton().isVisible() == false) {
					getSaveToHistoryButton().setVisible(true);
					getImportHistoryButton().setVisible(true);
					getExportHistoryButton().setVisible(true);
					getHistoryScrollPane().setVisible(true);
					getHistoryList().setVisible(true);
					getHistoryLabel().setVisible(true);
				}
				getHistoryList().repaint();
			} catch (SimDBException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}

	}

	public static JPanel getSubpanelEast() {
		return subpanelEast;
	}

	public static void setSubpanelEast(JPanel subpanelEast) {
		OptionsContainer.subpanelEast = subpanelEast;
	}

}
