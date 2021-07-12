package de.tudarmstadt.dvs.p4.simdb;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The Class Graphic sets properties in gnuplot(e.g. xdata,yabel,xlabel), then
 * it sends the created command in @see
 * de.tudarmstadt.dvs.p4.simdb.PlotCommand.java
 */
public class Graphic {

	/** The history statistics. */
	private static String historyStatistics;

	/** The gnuplot. */
	private static String gnuplot;

	/** The gnuplot process. */
	private static GnuplotProcess gp = null;

	/** The History list of plots. */
	private static ArrayList<String> HistoryListOfPlots = new ArrayList<String>();

	/** The plot command. */
	private static PlotCommand plotCommand;

	/** The line separator value. */
	private static String n = System.getProperty("line.separator");

	/** The command that gnuplot receives. */
	private static String command;

	/** The gnuplot properties. */
	private static String gnuplotProperties;

	/**
	 * Format label.
	 * 
	 * @param data
	 *            the data
	 * @return the string
	 */
	public static String formatLabel(final String data) {

		String[] labels = data.split(System.getProperty("line.separator"));
		String formatedLabel = "";
		for (int i = 0; i < labels.length; i++) {

			int index = labels[i].indexOf(",");
			if (index < 0) {
				continue;
			}
			String labelPosition = labels[i].substring(0, index);
			formatedLabel += "set label \"" + labels[i].substring(index + 1)
					+ "\"  at first \"" + labelPosition
					+ "\", graph 1.03 tc rgb \"#EE7A34\"" + getLineSeparator()
					+ "set arrow from first \"" + labelPosition
					+ "\", graph 0 to first \"" + labelPosition
					+ "\", graph 1.0 lc rgb \"#EE7A34\"" + getLineSeparator();
		}
		return formatedLabel;
	}

	/**
	 * Gets the history statistics.
	 * 
	 * @return the history statistics
	 */
	public static String getHistoryStatistics() {
		return historyStatistics;
	}

	/**
	 * Gets the y label.
	 * 
	 * @param labelAndUnits
	 *            the label and units
	 * @return the y label
	 */
	public static String getYLabel(String labelAndUnits) {

		String yFormatedLabel = "";
		if (labelAndUnits.contains("@")) {
			int index = labelAndUnits.indexOf("@");
			yFormatedLabel = labelAndUnits.substring((index + 1),
					(labelAndUnits.length() - 1));
		} else {
			if (labelAndUnits.contains(",")) {
				int index = labelAndUnits.indexOf(",");
				if (System.getProperty("line.separator") != "\n") {
					labelAndUnits = labelAndUnits.replaceAll(
							System.getProperty("line.separator"), "\n");

				}
				yFormatedLabel = labelAndUnits.substring(0, index)
						+ " ("
						+ labelAndUnits.substring((index + 1),
								(labelAndUnits.length() - 1)) + ")";
			}
		}

		return yFormatedLabel;
	}

	/**
	 * Sets the history statistics.
	 * 
	 * @param historyStatistics
	 *            the new history statistics
	 */
	public static void setHistoryStatistics(final String historyStatistics) {
		Graphic.historyStatistics = historyStatistics;
	}

	/**
	 * Instantiates a new graphic.
	 * 
	 * @throws SimDBException
	 *             the sim db exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SQLException
	 *             the sQL exception
	 */
	public Graphic() throws SimDBException, IOException, SQLException {
		super();
	}

	/**
	 * Gets the ranges.
	 * 
	 * @return the string
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public final String GetRanges() throws InterruptedException, IOException {

		String inputStreamXRange = getGp().getVar("xr");
		String inputStreamYRange = getGp().getVar("yr");
		if (inputStreamXRange.contains("*") || inputStreamYRange.contains("*")) {
			return "";
		} else {
			String ranges = inputStreamXRange + ";" + inputStreamYRange;
			int comma = ranges.indexOf(';');
			String xr = "", yr = "";
			int current = 0, beginyr, begin;
			if (ranges.contains("#")) {
				current = ranges.indexOf('#');
				if (ranges.contains("drawn")) {
					if (ranges.contains("set")) {
						int index = ranges.indexOf("set");
						ranges = ranges.substring(index);
					}
					if (ranges.contains(";")) {
						xr = ranges.substring(0, comma).substring(current);
						yr = ranges.substring(comma).substring(current);
					}
				}
			} else {
				xr = ranges.substring(0, comma);
				yr = ranges.substring(comma);
			}

			if (true == xr.contains("[")) {
				begin = yr.indexOf('[');
			} else {
				begin = 0;
			}

			begin = xr.indexOf('[');
			int end = xr.indexOf(']');
			String xrange = "", yrange = "";

			if (xr.contains("drawn")) {

				if (xr.contains("set")) {
					int index = ranges.indexOf("set");
					xr = xr.substring(index);
					xrange = xr.substring(begin, end) + "]";
				}
			} else if (xr.contains("[") && xr.contains("]")) {
				xrange = xr.substring(begin, end) + "]";
			}

			int endyr = yr.indexOf(']');

			if (yr.contains("[")) {
				beginyr = yr.indexOf('[');
				yrange = yr.substring(beginyr, endyr);
			} else if (yr.contains("]")) {
				beginyr = 0;
				yrange = "[" + yr.substring(beginyr, endyr);
			}

			String completeRangeString = "set xrange" + xrange
					+ getLineSeparator() + "set yrange" + yrange + "]"
					+ getLineSeparator();

			return completeRangeString;
		}
	}

	/**
	 * Plot.
	 * 
	 * @param command
	 *            the command that gnuplot receives.
	 */
	public void plot(final String command) {
		try {
			setHistoryStatistics(PlotCommand.getStatisticsToHistory());
			setGp(new GnuplotProcess(getGnuplot()));
			getGp().cmd(command);

		} catch (Exception xa) {
			System.out.println(xa.getMessage());
		}
	}

	/**
	 * Prepare to plot.
	 * 
	 * @param statisticId
	 *            the statistic id
	 * @param minMax
	 *            the min max
	 * @param aggregate
	 *            the aggregate
	 * @param errorBars
	 *            the error bars
	 * @param several
	 *            the several
	 * @param yDifferentScales
	 *            the y different scales
	 * @param historyIndex
	 *            the history index
	 * @throws SimDBException
	 *             the sim db exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SQLException
	 *             the sQL exception
	 * @throws ClassNotFoundException
	 */
	protected final void prepareToPlot(final String statisticId,
			final boolean minMax, final boolean aggregate,
			final boolean errorBars, final boolean several,
			final boolean yDifferentScales, final boolean historyIndex)
			throws SimDBException, IOException, SQLException {

		plotCommand = new PlotCommand();
		gnuplotProperties = "";
		setCommand(null);
		boolean imported = false;
		if (historyIndex == true) {

			int index = Integer.parseInt(statisticId);

			String history = getHistoryListOfPlots().get(index);

			gnuplotProperties = plotCommand.prepareCommand(history, imported);

		} else {
			setCommand(plotCommand.getPlotCommand(statisticId, minMax,
					aggregate, errorBars, several, yDifferentScales, imported));

			if (getCommand() != null) {
				SimDBReader reader = new SimDBReader(PlotCommand.getDatabase());
				String label = PlotCommand.readResultSet(reader
						.selectUpperLabel());
				label = formatLabel(label);
				gnuplotProperties += "unset label; unset arrow"
						+ getLineSeparator();
				gnuplotProperties += "set xdata time" + getLineSeparator();
				gnuplotProperties += "set timefmt \"%H:%M:%S\";"
						+ getLineSeparator() + " set format x \"%H:%M:%S\""
						+ getLineSeparator();
				gnuplotProperties += "set xlabel \"Simulated time\""
						+ getLineSeparator();
				if (PlotCommand.isyScales() == true) {
					int indexYLabels = PlotCommand.getyLabel().indexOf("/");
					String yLabel = PlotCommand.getyLabel();
					String yLabel1 = yLabel.substring(8, indexYLabels);
					String yLabel2 = yLabel.substring((indexYLabels + 1),
							(yLabel.length() - 1));
					PlotCommand.setyLabel("set ylabel '" + yLabel1
							+ "';set y2label '" + yLabel2 + "';");
					gnuplotProperties += PlotCommand.getyLabel()
							+ getLineSeparator();
				} else {
					gnuplotProperties += "set ylabel \""
							+ getYLabel(PlotCommand.getyLabel()) + "\""
							+ getLineSeparator();
				}
				gnuplotProperties += "set datafile separator \"|\""
						+ getLineSeparator();
				gnuplotProperties += "set term wxt persist"
						+ getLineSeparator();
				gnuplotProperties += label;
				String history = "&" + statisticId + "," + minMax + ",";
				if (aggregate == true) {
					history += aggregate + "/"
							+ SimDBUI.getAggregateTextField().getText() + ","
							+ errorBars + "," + several + ","
							+ yDifferentScales;
				} else {
					history += aggregate + "," + errorBars + "," + several
							+ "," + yDifferentScales;
				}
				getHistoryListOfPlots().add(0, history);

				gnuplotProperties += getCommand();

			}
		}
		plot(gnuplotProperties);
	}

	/**
	 * Save to history of graphics list.
	 * 
	 * @param historyIndexNo
	 *            the history index no
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SimDBException
	 *             the sim db exception
	 * @throws SQLException
	 *             the sQL exception
	 */
	public final void saveToHistoryList(final int historyIndexNo)
			throws InterruptedException, IOException, SimDBException,
			SQLException {

		String historyCommand = "";
		String grid = getGp().getVar("grid");
		String prevCommand = getHistoryListOfPlots().get(historyIndexNo);
		int index = prevCommand.indexOf("&");
		prevCommand = prevCommand.substring(index);

		if (grid.contains("OFF") == false) {
			historyCommand = "set grid%" + GetRanges() + "%" + prevCommand;
		} else {
			historyCommand = "unset grid%" + GetRanges() + "%" + prevCommand;
		}
		getHistoryListOfPlots().set(historyIndexNo, historyCommand);

	}

	public static String getGnuplot() {
		return gnuplot;
	}

	public static void setGnuplot(String gnuplot) {
		Graphic.gnuplot = gnuplot;
	}

	public static ArrayList<String> getHistoryListOfPlots() {
		return HistoryListOfPlots;
	}

	public static void setHistoryListOfPlots(
			ArrayList<String> historyListOfPlots) {
		HistoryListOfPlots = historyListOfPlots;
	}

	public static String getLineSeparator() {
		return n;
	}

	public static void setLineSeparator(String n) {
		Graphic.n = n;
	}

	public static String getCommand() {
		return command;
	}

	public static void setCommand(String command) {
		Graphic.command = command;
	}

	public static GnuplotProcess getGp() {
		return gp;
	}

	public static void setGp(GnuplotProcess gp) {
		Graphic.gp = gp;
	}

}
