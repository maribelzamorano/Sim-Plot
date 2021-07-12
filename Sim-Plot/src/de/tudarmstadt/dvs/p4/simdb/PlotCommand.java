package de.tudarmstadt.dvs.p4.simdb;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * The Class PlotCommand creates the command that gnuplot receives.
 */
public class PlotCommand {

	/** The database. */
	private static String database;

	/** The statistics sent to history list. */
	private static String statisticsToHistory;

	/** The line separator value. */
	private static String n = System.getProperty("line.separator");

	/** The y label. */
	private static String yLabel;

	/** The seconds used in case of aggregation. */
	private static int seconds;

	/** The boolean value if the function "different Y scales" is enabled. */
	private static boolean yScales;

	/**
	 * Instantiates a new plot command.
	 * 
	 * @throws SimDBException
	 *             the sim db exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SQLException
	 *             the sQL exception
	 */
	public PlotCommand() throws SimDBException, IOException, SQLException {
		super();
	}

	/**
	 * Gets the plot command.
	 * 
	 * @param statisticIdString
	 *            the statistic id
	 * @param minMax
	 *            the min max
	 * @param aggregate
	 *            the aggregate
	 * @param errorBars
	 *            the error bars
	 * @param several
	 *            the several
	 * @param YDifferentScales
	 *            the y different scales
	 * @param Imported
	 *            the imported
	 * @return the plot command
	 * @throws SimDBException
	 *             the sim db exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SQLException
	 *             the sQL exception
	 */
	public String getPlotCommand(String statisticIdString, boolean minMax,
			final boolean aggregate, boolean errorBars, boolean several,
			boolean YDifferentScales, boolean Imported) throws SimDBException,
			IOException, SQLException {
		ResultSet rLabel = null;
		String s = "";
		setStatisticsToHistory("");
		List<String> statisticName = null;
		setyScales(YDifferentScales);

		if (several == true) {
			statisticName = getStatisticsList(several, YDifferentScales,
					statisticIdString);
			s += n;
			String multiQuery = "";
			setyLabel("several@");
			String severalQuerys = "";

			for (int i = 0; i < statisticName.size(); i++) {
				int index = statisticName.get(i).indexOf("=");
				if (index < 0) {
					continue;
				}
				setDatabase(statisticName.get(i).substring(0, index));
				String statistic = statisticName.get(i).substring(index + 1);
				String statisticformatedName = formatStatisticName(statistic);
				int statisticId = getStatisticId(statistic);
				setStatisticsToHistory(statisticformatedName);

				SimDBReader reader = new SimDBReader(getDatabase());
				DBQuery dbQuery = new DBQuery();
				if (statisticName.get(i).contains(",")) {
					if (aggregate == true) {
						if (Imported = false) {
							seconds = Integer.parseInt(SimDBUI
									.getAggregateTextField().getText());
						}
						dbQuery.setSeveralAggregateStatisticsQuery(statisticId,
								seconds);
						severalQuerys += dbQuery
								.getSeveralAggregateStatisticsQuery() + ";";
					} else {
						dbQuery.setSeveralStatisticsQuery(statisticId);
						severalQuerys += dbQuery.getSeveralStatisticsQuery()
								+ ";";
					}

				} else {

					if (aggregate == true) {
						int seconds = Integer.parseInt(SimDBUI
								.getAggregateTextField().getText());
						dbQuery.setGroupNodeSeveralAggregateStatisticsQuery(
								statisticId, seconds);
						severalQuerys += dbQuery
								.getGroupNodeSeveralAggregateStatisticsQuery()
								+ ";";

					} else {
						dbQuery.setGroupNodeSeveralStatisticsQuery(statisticId);
						severalQuerys += dbQuery
								.getGroupNodeSeveralStatisticsQuery() + ";";
					}
				}
				rLabel = reader.selectLabelByStatisticId(statisticId);
				// }

				new Graphic();
				setyLabel(getyLabel()
						+ (Graphic.getYLabel(readResultSet(rLabel)) + "/"));

			}

			int index0 = statisticName.get(0).indexOf("=");

			String[] query = severalQuerys.split(";");
			String databaseCero = statisticName.get(0).substring(0, index0);

			if (YDifferentScales == true) {

				s = "set ytics" + n;
				s += "set y2tics" + n;
				int index1 = statisticName.get(1).indexOf("=");
				String databaseOne = statisticName.get(1).substring(0, index1);
				multiQuery = "plot '<sqlite3 "
						+ databaseCero
						+ " \""
						+ query[0]
						+ "\"' using 1:2 w lines title \""
						+ formatStatisticName(statisticName.get(0).substring(
								index0 + 1))
						+ "\" axis x1y1, '<sqlite3 "
						+ databaseOne
						+ " \""
						+ query[1]
						+ "\"' using 1:2 w lines title \""
						+ formatStatisticName(statisticName.get(1).substring(
								index0 + 1)) + "\" axis x1y2";

			} else {

				multiQuery = "plot '<sqlite3 "
						+ databaseCero
						+ " \""
						+ query[0]
						+ "\"' using 1:2 w lines title \""
						+ formatStatisticName(statisticName.get(0).substring(
								index0 + 1)) + "\"";

				for (int ii = 1; ii < statisticName.size(); ii++) {
					int indexI = statisticName.get(ii).indexOf("=");
					String statisticDatabase = statisticName.get(ii).substring(
							0, indexI);
					multiQuery += ", '<sqlite3 "
							+ statisticDatabase
							+ " \""
							+ query[ii]
							+ "\"' using 1:2 w lines title \""
							+ formatStatisticName(statisticName.get(ii)
									.substring(index0 + 1)) + "\"";
				}
			}

			s += multiQuery;
			s += n;

		} else {
			int index = statisticIdString.indexOf("=");
			setDatabase(statisticIdString.substring(0, index));
			String query = "";
			String minMaxQuery = "";
			SimDBReader reader = new SimDBReader(getDatabase());

			DBQuery dbQuery = new DBQuery();

			String statisticFormatedName = formatStatisticName(statisticIdString
					.substring(index + 1));
			int statisticId = getStatisticId(statisticIdString
					.substring(index + 1));
			setStatisticsToHistory(statisticFormatedName);
			if (statisticIdString.contains(",") == true) {

				if (aggregate == true) {
					int seconds = Integer.parseInt(SimDBUI
							.getAggregateTextField().getText());
					dbQuery.setAggregateStatisticsQuery(statisticId, seconds);
					query = dbQuery.getAggregateStatisticsQuery();

				} else {
					dbQuery.setStatisticsQuery(statisticId);
					query = dbQuery.getStatisticsQuery();

				}

				if (minMax == true) {
					dbQuery.setMinMaxStatisticsQuery(statisticId);
					query = dbQuery.getMinMaxStatisticsQuery();

				}

			} else {

				int seconds = 0;

				if (aggregate == true) {
					seconds = Integer.parseInt(SimDBUI.getAggregateTextField()
							.getText());
					dbQuery.setGroupNodeAggregateStatisticsQuery(statisticId,
							seconds);
					query = dbQuery.getGroupNodeAggregateStatisticsQuery();

				} else {

					dbQuery.setGroupNodeStatisticsQuery(statisticId);

					query = dbQuery.getGroupNodeStatisticsQuery();

				}

				if (minMax == true) {
					if (aggregate == true) {
						dbQuery.setGroupNodeAggregateMinMaxStatisticsQuery(
								statisticId, seconds);
						minMaxQuery = dbQuery
								.getGroupNodeAggregateMinMaxStatisticsQuery();

					} else {
						dbQuery.setGroupNodeMinMaxStatisticsQuery(statisticId);

						minMaxQuery = dbQuery
								.getGroupNodeMinMaxStatisticsQuery();

					}
				}
			}
			rLabel = reader.selectLabelByStatisticId(statisticId);
			setyLabel(readResultSet(rLabel));

			if (errorBars == false) {

				s = "plot '<sqlite3 " + getDatabase() + " \"" + query
						+ "\"' using 1:2 w lines title \""
						+ statisticFormatedName + "\"";

			} else {

				s = "plot '<sqlite3 " + getDatabase() + " \"" + query
						+ "\"' using 1:2 w lines title \""
						+ statisticFormatedName + "\"";
				s += ", \"\" using 1:2:(sqrt($3)) with errorbars ls 9 notitle \"";

			}

			if (minMax == false) {
				s += n;

			} else {
				if (errorBars == true) {
					s = "plot '<sqlite3 "
							+ getDatabase()
							+ " \""
							+ query
							+ "\"' using 1:2:(sqrt($3)) with errorbars ls 9 notitle, ";
				} else {
					s = "plot";
				}
				s += " '<sqlite3 " + getDatabase() + " \"" + minMaxQuery
						+ "\"' using 1:2 w lines title \"Max."
						+ statisticFormatedName + "\"";

				s += ", '<sqlite3 " + getDatabase() + " \"" + minMaxQuery
						+ "\"' using 1:3 w lines title \"Min."
						+ statisticFormatedName + "\"";
				s += ", '<sqlite3 " + getDatabase() + " \"" + query
						+ "\"' using 1:2 w lines title \""
						+ statisticFormatedName + "\"" + n;
			}
		}
		rLabel.close();
		return s;

	}

	/**
	 * Read result set.
	 * 
	 * @param rs
	 * @return a formatted string containing the data of the result set
	 * @throws SQLException
	 * @throws IOException
	 */
	protected static String readResultSet(final ResultSet rs)
			throws SQLException, IOException {
		String results = "";
		@SuppressWarnings("unused")
		int rowcount = 0;
		int colCount = rs.getMetaData().getColumnCount();

		final String n = System.getProperty("line.separator");
		while (rs.next()) {
			for (int i = 0; i < colCount; i++) {
				if (i > 0) {
					Object value = rs.getObject(i + 1);
					if (value == null || rs.wasNull()) {
						results = "NULL";
					} else {
						if (colCount == 3) {
							results += rs.getString(1) + "," + rs.getString(2)
									+ "," + rs.getString(3) + n;
						} else {

							results += rs.getString(1) + "," + rs.getString(2)
									+ n;
						}
					}
				}
			} // end of column. loop to next column.

			rowcount++;
		} // End of current row. loop to next row.

		rs.close();
		return results;

	}

	/**
	 * Gets the statistic name.
	 * 
	 * @param several
	 *            the several
	 * @param yDifferentScales
	 *            the y different scales
	 * @param statisticId
	 *            the statistic id
	 * @return the statistic name
	 */
	public final List<String> getStatisticsList(final boolean several,
			final boolean yDifferentScales, final String statisticId) {
		List<String> statisticsList = new ArrayList<String>();
		if (several == true) {
			String[] statistics = statisticId.split(";");

			statisticsList.add(0, statistics[0]);

			if (yDifferentScales == true) {
				statisticsList.add(1, statistics[1]);
			} else {
				for (int i = 1; i < statistics.length; i++) {
					statisticsList.add(i, statistics[i]);
				}
			}

		}

		return statisticsList;

	}

	private String formatStatisticName(String statisticOldName) {
		String statisticName = "";
		if (statisticOldName.contains(",")) {
			int index = statisticOldName.indexOf(",");
			statisticName = statisticOldName.substring(index + 1);
		} else if (statisticOldName.contains("#")) {
			int index = statisticOldName.indexOf("#");
			statisticName = statisticOldName.substring(0, index);
		}
		return statisticName;
	}

	private int getStatisticId(String statistic) {
		int statisticId = 0;
		if (statistic.contains("#")) {
			int indexNr = statistic.indexOf("#");
			statisticId = Integer.parseInt(statistic.substring(indexNr + 1));
		} else if (statistic.contains(",")) {
			int indexNr = statistic.indexOf(",");
			statisticId = Integer.parseInt(statistic.substring(0, indexNr));
		}
		return statisticId;
	}

	/**
	 * Creates a command to set gnuplot properties.
	 * 
	 * @param preCommand
	 *            the pre command
	 * @param imported
	 *            the imported
	 * @return the string
	 * @throws SimDBException
	 *             the sim db exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SQLException
	 *             the sQL exception
	 */
	public final String prepareCommand(final String preCommand,
			final boolean imported) throws SimDBException, IOException,
			SQLException {

		int index = preCommand.indexOf("&");
		String historyPrepare = preCommand.substring(0, index);

		String historyCommand = preCommand.substring(index + 1);
		String[] values = historyCommand.split(",");
		String statisticId = values[0];

		boolean historyMinMax = Boolean.parseBoolean(values[1]);
		boolean historyAggregate;
		if (values[2].contains("/")) {
			int indexSeconds = values[2].indexOf("/");
			historyAggregate = Boolean.parseBoolean(values[2].substring(0,
					indexSeconds));
			seconds = Integer.parseInt(values[2].substring(indexSeconds + 1));
		} else {
			historyAggregate = Boolean.parseBoolean(values[2]);
		}
		boolean historyErrorBars = Boolean.parseBoolean(values[3]);
		boolean historySeveral = Boolean.parseBoolean(values[4]);
		boolean historyYdifferentScales = Boolean.parseBoolean(values[5]);
		String command = getPlotCommand(statisticId, historyMinMax,
				historyAggregate, historyErrorBars, historySeveral,
				historyYdifferentScales, imported);

		SimDBReader reader = new SimDBReader(getDatabase());
		String label = readResultSet(reader.selectUpperLabel());
		label = Graphic.formatLabel(label);
		String s = "unset label; unset arrow" + n;
		s += "set xdata time" + n;
		s += "set timefmt \"%H:%M:%S\";" + n + "set format x \"%H:%M:%S\"" + n;
		s += "set xlabel \"Simulated time\"" + n;
		if (isyScales() == true) {
			int indexYLabels = getyLabel().indexOf("/");
			String yLabel1 = getyLabel().substring(8, indexYLabels);
			String yLabel2 = getyLabel().substring((indexYLabels + 1),
					(getyLabel().length() - 1));
			setyLabel("set ylabel '" + yLabel1 + "';set y2label '" + yLabel2
					+ "';");
			s += getyLabel() + n;
		} else {
			s += "set ylabel \"" + Graphic.getYLabel(getyLabel()) + "\"" + n;
		}
		s += "set datafile separator \"|\"" + n;
		s += "set term wxt persist" + n;
		s += label;
		if (historyPrepare.contains("xrange")) {
			String[] settingsHistory = historyPrepare.split("%");
			String grid = settingsHistory[0];
			String range = settingsHistory[1];
			s += grid + n + range + n;

		}

		s += command;

		return s;
	}

	/**
	 * Show message dialog in case of an empty statistic.
	 * 
	 * @param statisticId
	 */
	public void showMessageDialog(final String statisticId) {
		JOptionPane.showMessageDialog(null, "Empty statistic: '" + statisticId
				+ "'", "Empty measurements", 1);

	}

	public static String getStatisticsToHistory() {
		return statisticsToHistory;
	}

	public static void setStatisticsToHistory(String statisticsToHistory) {
		PlotCommand.statisticsToHistory = statisticsToHistory;
	}

	public static String getDatabase() {
		return database;
	}

	public static void setDatabase(String database) {
		PlotCommand.database = database;
	}

	public static String getyLabel() {
		return yLabel;
	}

	public static void setyLabel(String yLabel) {
		PlotCommand.yLabel = yLabel;
	}

	public static boolean isyScales() {
		return yScales;
	}

	public static void setyScales(boolean yScales) {
		PlotCommand.yScales = yScales;
	}

}
