package de.tudarmstadt.dvs.p4.simdb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * The Class Document creates a new document when the user clicks on the buttons
 * "Save PDF" for PDF documents containing the image of the selected graphic or
 * "Save TXT" for text files containing the proper command to use in gnuplot.
 */
public class Document extends Graphic {

	/**
	 * Instantiates a new document.
	 * @throws SimDBException
	 * @throws IOException
	 * @throws SQLException
	 */
	public Document() throws SimDBException, IOException, SQLException {
		super();
	}

	/**
	 * Creates the document.
	 * @param documentType the document type(PDF or text file)
	 * @param StatisticId the statistic id
	 * @param minMax if the min max option is selected in the GUI.
	 * @param aggregate if the aggregate option is selected in the GUI.
	 * @param errorBars if the error bars option is selected in the GUI.
	 * @param several if the several statistics are selected.
	 * @param YDifferentScales the Y different scales option is selected.
	 * @param historyIndex if the selected statistic is in the history list.
	 * @throws InterruptedException the interrupted exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SQLException the sQL exception
	 * @throws SimDBException the sim db exception
	 */
	public void createDocument(String documentType, String StatisticId,
			boolean minMax, boolean aggregate, boolean errorBars,
			boolean several, boolean YDifferentScales, boolean historyIndex)
			throws InterruptedException, IOException, SQLException,
			SimDBException {

		PlotCommand plotCommand = new PlotCommand();
		String s2 = "";
		String s1 = "";
		String historyCommand = null;

		boolean imported = false;
		if (historyIndex == true) {

			int index = Integer.parseInt(StatisticId);

			historyCommand = plotCommand.prepareCommand(
					getHistoryListOfPlots().get(index), imported);

			s2 = historyCommand;

		} else {

			Graphic graphic = new Graphic();
			s2 += graphic.GetRanges();

			setCommand(plotCommand.getPlotCommand(StatisticId, minMax,
					aggregate, errorBars, several, YDifferentScales, imported));
			s2 += getCommand();

		}

		if (getCommand() != null || historyCommand != null) {
			String file = showSaveDialog(documentType);
			if (file != null) {

				if (historyCommand == null) {
					SimDBReader reader = new SimDBReader(PlotCommand.getDatabase());
					String label = PlotCommand.readResultSet(reader
							.selectUpperLabel());
					label = Graphic.formatLabel(label);
					s1 = "unset label; unset arrow" + getLineSeparator();
					s1 += "set xdata time" + getLineSeparator();
					s1 += "set timefmt \"%H:%M:%S\";" + getLineSeparator()
							+ "set format x \"%H:%M:%S\"" + getLineSeparator();
					s1 += "set xlabel \"Simulated time\"" + getLineSeparator();
					if(PlotCommand.isyScales()==true)
					{
						int indexYLabels=PlotCommand.getyLabel().indexOf("/");
						String yLabel=PlotCommand.getyLabel();
						String yLabel1=yLabel.substring(8,indexYLabels);
						String yLabel2=yLabel.substring((indexYLabels+1),(yLabel.length()-1));
						PlotCommand.setyLabel("set ylabel '"+yLabel1+"';set y2label '"+yLabel2+"';");
						s1 += PlotCommand.getyLabel() + getLineSeparator();
					}
					else
					{
						s1 += "set ylabel \""
							+ Graphic.getYLabel(PlotCommand.getyLabel()) + "\"" + getLineSeparator();
					}
					s1 += "set datafile separator \"|\"" + getLineSeparator();
					s1 += label;
				}
				if (documentType == "PDF") {
					s1 += "set term pdf" + getLineSeparator() + "set output \"" + file
							+ ".pdf\"" + getLineSeparator();
					s1 += s2;
					s1 += "unset output" + getLineSeparator();
					s1 += "set term wxt" + getLineSeparator();
				}
				if (documentType == "TXT") {
					createTextFile(file, s1 + s2);
				} else {
					plot(s1);
				}
			}
		}
	}

	/**
	 * Creates a text file.
	 * @param file the file
	 * @param content the content
	 */
	public static void createTextFile(final String file, final String content) {
		try {

			FileWriter fstream = new FileWriter(file + ".txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Show save dialog.
	 * @param documentType the document type
	 * @return the string
	 */
	public final String showSaveDialog(final String documentType) {
		File file;
		String path = "";

		String wd = System.getProperty("user.dir");
		JFileChooser fc = new JFileChooser(wd);
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(final File f) {
				String ending = "";
				if (documentType == "PDF") {
					ending = ".pdf";
				} else if (documentType == "TXT") {
					ending = ".txt";
				}
				return f.getName().toLowerCase().endsWith(ending)
						|| f.isDirectory();
			}

			@Override
			public String getDescription() {
				String filesType = "";
				if (documentType == "PDF") {
					filesType = "PDF Files";
				} else if (documentType == "TXT") {
					filesType = "Text Files";
				}
				return filesType;
			}
		});
		int rc = fc.showSaveDialog(null);

		if (rc == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			if (System.getProperty("File.separator") != "/") {
				path = file.toString().replaceAll(
						File.separator + File.separator, "/");
			}
		}

		return path;
	}
}
