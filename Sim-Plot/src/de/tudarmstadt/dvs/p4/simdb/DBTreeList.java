package de.tudarmstadt.dvs.p4.simdb;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 * The Class DBTreeList displays the databases that are being used and the user
 * is able to select one of this databases to remove it from the databases tree
 * in the GUI.
 */
public class DBTreeList extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The remove database button label. */
	private JLabel removeDBButtonLabel;

	/** The close button. */
	private JButton closeButton;

	/** The remove DB label. */
	private static JLabel removeDBLabel;

	/** The remove DB button. */
	private static JButton removeDBButton;

	/** The databases list. */
	private static JList databasesList;

	/**
	 * Removes database button action.
	 * 
	 * @throws SimDBException
	 * @throws IOException
	 * @throws SQLException
	 */
	public final void removeButtonAction() throws SimDBException, IOException,
			SQLException {

		if (databasesList.isSelectionEmpty() == false) {
			int selectedIx = databasesList.getSelectedIndex();
			SimDBUI.getDatabasesList().remove(selectedIx);
			databasesList.setListData(SimDBUI.getDatabasesList().toArray());
			databasesList.repaint();

			DatabasesTree treeDB = new DatabasesTree();
			treeDB.updateDatabaseTree();
		}
	}

	/**
	 * Starts the "Remove Database" Dialog.
	 */
	public final void initRemoveDBDialog() {
		try {

			TableLayout thisLayout = new TableLayout(new double[][] {
					{ 20.0, TableLayoutConstants.FILL,
							TableLayoutConstants.FILL, 20.0 },
					{ TableLayoutConstants.FILL, TableLayoutConstants.FILL,
							TableLayoutConstants.FILL,
							TableLayoutConstants.FILL,
							TableLayoutConstants.FILL, 5.0 } });
			thisLayout.setHGap(5);
			thisLayout.setVGap(5);
			getContentPane().setLayout(thisLayout);
			{

				JScrollPane databaseListScrollPane = new JScrollPane();
				databaseListScrollPane.setBorder(null);
				{
					databasesList = new JList();
					databasesList.setBackground(getContentPane()
							.getBackground());
					databasesList.setListData(SimDBUI.getDatabasesList()
							.toArray());
					databasesList.repaint();
					databaseListScrollPane.setViewportView(databasesList);

				}

				this.setVisible(true);
				getContentPane().add(databaseListScrollPane, "1, 1, 2, 2,f,f");
			}
			{
				removeDBLabel = new JLabel();
				removeDBLabel.setText("Select a database to remove:");
				getContentPane().add(removeDBLabel, "1, 0, 2, 0");
				removeDBButton = new JButton();

				getContentPane().add(removeDBButton, "2, 4,c,c");
				{
					removeDBButtonLabel = new JLabel();
					removeDBButton.add(removeDBButtonLabel);
					removeDBButtonLabel.setText("Remove Database");
					removeDBButtonLabel
							.setIcon(new ImageIcon(
									getClass()
											.getClassLoader()
											.getResource(
													"de/tudarmstadt/dvs/p4/util/icons/delete.png")));
					removeDBButtonLabel.setBounds(127, 71, 114, 16);
				}
				removeDBButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ev) {
						try {
							removeButtonAction();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}
			{
				closeButton = new JButton();
				getContentPane().add(closeButton, "1, 4,r,c");
				closeButton.setText("Close");
				closeButton.setIcon(new ImageIcon(getClass().getClassLoader()
						.getResource(
								"de/tudarmstadt/dvs/p4/util/icons/stop.png")));
				closeButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ev) {
						try {
							close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}

			{
				this.setTitle("Remove Database");
				this.setSize(new Dimension(428, 234));
				this.setLocation(700, 400);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close the dialog.
	 */
	public final void close() {
		this.setVisible(false);
	}

}
