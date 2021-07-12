package de.tudarmstadt.dvs.p4.simdb;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * The Class ExportContainer creates a Dialog box on the GUI, in this dialog box
 * the user can copy a string that contains all the necessary values to reproduce
 * a graphic selected on the "History of graphics" list and is saved with
 * specific properties.
 */
public class ExportContainer extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The export text field. */
	private static JTextField exportTextField;

	/** The copy label. */
	private static JLabel copyLabel;

	/**
	 * Starts the export dialog box.
	 */
	public final void initExportDialog() {
		try {

			TableLayout thisLayout = new TableLayout(new double[][] {
					{20.0, TableLayoutConstants.FILL,
							TableLayoutConstants.FILL, 20.0},
					{TableLayoutConstants.FILL, TableLayoutConstants.FILL,
							TableLayoutConstants.FILL,
							TableLayoutConstants.FILL}});
			thisLayout.setHGap(5);
			thisLayout.setVGap(5);
			getContentPane().setLayout(thisLayout);
			{
				exportTextField = new JTextField();
				getContentPane().add(exportTextField, "1, 2, 2, 2,f,f");
			}
			{
				copyLabel = new JLabel();
				copyLabel.setText("Copy the string to export:");
				copyLabel
						.setIcon(new ImageIcon(
								getClass()
										.getClassLoader()
										.getResource(
												"de/tudarmstadt/dvs/p4/util/icons/export.png")));
				getContentPane().add(copyLabel, "1, 1, 2, 1,f,f");
			}

			{
				this.setTitle("Export");
				this.setSize(338, 154);
				this.setLocation(700, 400);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the export text.
	 * @param exportText the new export text
	 */
	public final void setExportText(final String exportText) {
		exportTextField.setText(exportText);
		exportTextField.repaint();
	}

}
