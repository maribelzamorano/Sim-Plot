package de.tudarmstadt.dvs.p4.simdb;

import info.clearthought.layout.TableLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * The Class ImportContainer creates a Dialog box on the GUI, in this dialog box
 * the user can introduce a string that was previously exported and contains all
 * the necesary values to reproduce a graphic saved with specific properties.
 */
public class ImportContainer extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The import text field. */
	private static JTextField importTextField;

	/** The paste label. */
	private static JLabel pasteLabel;

	/** The import button label. */
	private JLabel importButtonLabel;

	/** The do import button. */
	private static JButton doImportButton;

	/**
	 * Gets the import text field.
	 * 
	 * @return the import text field
	 */
	public static JTextField getImportTextField() {
		return importTextField;
	}

	/**
	 * Sets the import text field.
	 * 
	 * @param importTextField
	 *            the new import text field
	 */
	public static void setImportTextField(final JTextField importTextField) {
		ImportContainer.importTextField = importTextField;
	}

	/**
	 * Do import button action. Creates the necessary gnuplot command to plot a
	 * new graphic with specific properties, and saves the graphic in the
	 * "History of graphics" list.
	 * 
	 * @throws SimDBException
	 * @throws IOException
	 * @throws SQLException
	 */
	public final void doImportButtonAction() throws SimDBException,
			IOException, SQLException {

		String plotConfiguration = getImportText();

		PlotCommand command = new PlotCommand();
		Graphic graphic = new Graphic();
		boolean imported = true;

		String s = command.prepareCommand(plotConfiguration, imported);
		graphic.plot(s);
		Graphic.getHistoryListOfPlots().add(0, plotConfiguration);
		SimDBUI.addToHistoryList(Graphic.getHistoryStatistics());
		SimDBUI.getHistoryList().setListData(
				SimDBUI.getStatisticsHistoryList().toArray());
		SimDBUI.getHistoryList().repaint();
		this.setVisible(false);
	}

	/**
	 * Gets the import text.
	 * 
	 * @return the export text
	 */
	public String getImportText() {
		String importCommand = getImportTextField().getText();
		return importCommand;
	}

	/**
	 * Starts the import dialog box.
	 */
	public void initImportDialog() {
		try {

			TableLayout thisLayout = new TableLayout(new double[][] {
					{ 20.0, 173.0, 123.0, 20.0 },
					{ TableLayout.FILL, TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL, 5.0 } });
			thisLayout.setHGap(5);
			thisLayout.setVGap(5);
			getContentPane().setLayout(thisLayout);
			{
				setImportTextField(new JTextField());
				getContentPane().add(getImportTextField(), "1, 2, 2, 2,f,f");
			}
			{
				pasteLabel = new JLabel();
				pasteLabel.setText("Paste here the string to import:");
				getContentPane().add(pasteLabel, "1, 1, 2, 1,f,f");
				doImportButton = new JButton();
				doImportButton.setPreferredSize(new Dimension(95, 26));
				getContentPane().add(doImportButton, "2,4,f,c");
				{
					importButtonLabel = new JLabel();
					doImportButton.add(importButtonLabel);
					importButtonLabel.setText("Import");
					importButtonLabel
							.setIcon(new ImageIcon(
									getClass()
											.getClassLoader()
											.getResource(
													"de/tudarmstadt/dvs/p4/util/icons/import.png")));
					importButtonLabel.setBounds(7, 4, 91, 16);
				}
				doImportButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ev) {
						try {
							doImportButtonAction();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}

			{
				this.setTitle("Import");
				this.setSize(353, 199);
				this.setLocation(700, 400);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
