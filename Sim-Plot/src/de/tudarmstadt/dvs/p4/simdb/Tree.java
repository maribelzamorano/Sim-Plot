package de.tudarmstadt.dvs.p4.simdb;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * The Class Tree.
 */
public class Tree {

	/** The Group names. */
	static ArrayList<String> GroupNames = new ArrayList<String>();
	static ArrayList<String> SubGroupNames = new ArrayList<String>();
	static ArrayList<String> SubSubGroupNames = new ArrayList<String>();
	/** The Experiment name. */
	static List<String> ExperimentName = new ArrayList<String>();
	static ArrayList<String> subGroup;
	/** The Sub statistics name. */
	static List<String> SubStatisticsName = new ArrayList<String>();

	/** The Sub statistics nodes. */
	static List<String> SubStatisticsNodes = new ArrayList<String>();

	/** The Statistics id list. */
	static List<String> StatisticsIdList;

	/** The Statistics name. */
	static List<String> StatisticsName;

	/** The Sub statistics id list. */
	static List<String> SubStatisticsIdList;

	/** The Experiments id list. */
	static List<String> ExperimentsIdList;

	/** The list of Experiments that contain statistics. */
	static List<String> ExperimentsWStatisticsList = new ArrayList<String>();

	/** The list of Statistics that contain statistics(Node = -1). */
	static List<String> StatisticsWStatisticsList = new ArrayList<String>();

	/** The Groups List. */
	static List<String> Groups = new ArrayList<String>();
	static List<String> SubGroups = new ArrayList<String>();
	static List<String> SubSubGroups = new ArrayList<String>();

	/** The database node. */
	static DefaultMutableTreeNode[] DBNode;

	/** The Statistics groups nodes. */
	static DefaultMutableTreeNode[] StatisticsGroupsNodes;
	static DefaultMutableTreeNode[] StatisticsSubGroupsNodes;
	static DefaultMutableTreeNode[] StatisticsSubSubGroupsNodes;
	/** The Experiments nodes. */
	static DefaultMutableTreeNode[] ExperimentsNodes;
	static DefaultMutableTreeNode[] StatisticsNodes;
	/** The Databases nodes. */
	static DefaultMutableTreeNode[] DatabasesNodes;
	static boolean isStatisticSubGroup;
	static boolean isStatisticSubSubGroup;
	static boolean isDatabaseSelected;
	static String valueOfTreeNode;
	static JTree tree;

	/**
	 * Gets the distinct list.
	 * 
	 * @param inArray
	 * @return the distinct list
	 */
	public static ArrayList<String> getDistintcsList(ArrayList<String> inArray) {
		String[] tmpLst = new String[inArray.size()];
		for (int i = 0; i < inArray.size(); i++) {
			tmpLst[i] = inArray.get(i);
		}
		Arrays.sort(tmpLst);
		inArray.clear();
		int lastArray = -1;
		for (int i = 1; i < tmpLst.length; i++) {
			if (lastArray == -1 || !tmpLst[i].equals(inArray.get(lastArray))) {
				inArray.add(tmpLst[i]);
				lastArray++;
			}
		}
		return inArray;
	}

	/** The database reader. */
	SimDBReader reader;

	/** The resultset with Experiments names. */
	ResultSet ExperimentsNamesResultset;

	/**
	 * This method creates the tree nodes containing the selected databases.
	 * 
	 * @throws SimDBException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void createDatabasesNodes() throws SimDBException, SQLException,
			IOException {
		List<String> DatabasesLi = new ArrayList<String>();
		DatabasesLi = SimDBUI.getDatabasesList();

		DatabasesNodes = new DefaultMutableTreeNode[DatabasesLi.size()];
		for (int i = 0; i < DatabasesLi.size(); i++) {
			try {
				reader = new SimDBReader(DatabasesLi.get(i));
				DatabasesNodes[i] = new DefaultMutableTreeNode(
						DatabasesLi.get(i));

				ExperimentsNamesResultset = reader.selectAllExperiments();
				while (ExperimentsNamesResultset.next()) {
					ExperimentsIdList = new ArrayList<String>();
					ExperimentsIdList = getList(ExperimentsNamesResultset,
							"Experiment");
					createExperimentsNodes(i);
				}
				ExperimentsNamesResultset.close();
				DBNode[0].add(DatabasesNodes[i]);
			} catch (SQLException eSQL) {
				JOptionPane.showMessageDialog(null, eSQL.getMessage(),
						"Error: ", JOptionPane.ERROR_MESSAGE);
				SimDBUI.getDatabasesList().remove(DatabasesLi.get(i));
				DatabasesTree.setDatabasePath(null);
				i++;
			}

		}

	}

	/**
	 * This method creates the tree nodes containing the experiment list of
	 * every selected database.
	 * 
	 * @param DBIterator
	 * @throws SimDBException
	 * @throws SQLException
	 */
	public void createExperimentsNodes(int DBIterator) throws SimDBException,
			SQLException {
		ExperimentsNodes = new DefaultMutableTreeNode[ExperimentsIdList.size()];
		for (int i = 0; i < ExperimentsIdList.size(); i++) {

			ResultSet resultsetStat = reader.selectStatistics(ExperimentsIdList
					.get(i));
			ExperimentsNodes[i] = new DefaultMutableTreeNode(
					ExperimentName.get(i));
			while (resultsetStat.next()) {
				StatisticsIdList = new ArrayList<String>();
				StatisticsName = new ArrayList<String>();
				StatisticsIdList = getList(resultsetStat, "Statistic");
				StatisticsNodes = new DefaultMutableTreeNode[StatisticsName
						.size()];
				createStatisticsGroupsNodes(i);
				DatabasesNodes[DBIterator].add(ExperimentsNodes[i]);

			}
			resultsetStat.close();

		}

	}

	/**
	 * This method creates the tree nodes containing the different statistic
	 * groups of every experiment.
	 * 
	 * @param experimentIterator
	 * @throws SQLException
	 * @throws SimDBException
	 */
	public void createStatisticsGroupsNodes(int experimentIterator)
			throws SQLException, SimDBException {

		for (int ii = 0; ii < StatisticsName.size(); ii++) {
			String StatisticGroup = StatisticsName.get(ii);

			if (StatisticGroup.contains("/")) {
				int index = StatisticGroup.indexOf("/");
				String statisticSubGroup = StatisticGroup.substring(index + 1);
				if (statisticSubGroup.contains("/")) {
					SubSubGroupNames.add(StatisticsName.get(ii));
				}

				SubGroupNames.add(StatisticsName.get(ii));
			}
			if (StatisticGroup.contains("/")) {
				int index2 = StatisticGroup.indexOf("/");
				StatisticGroup = StatisticGroup.substring(0, index2);
			}
			GroupNames.add(StatisticGroup);

		}

		Groups = getDistintcsList(GroupNames);
		SubGroups = getDistintcsList(SubGroupNames);
		SubSubGroups = getDistintcsList(SubSubGroupNames);

		StatisticsGroupsNodes = new DefaultMutableTreeNode[Groups.size()];
		for (int iii = 0; iii < Groups.size(); iii++) {
			StatisticsGroupsNodes[iii] = new DefaultMutableTreeNode(
					Groups.get(iii));
			ExperimentsNodes[experimentIterator]
					.add(StatisticsGroupsNodes[iii]);

			createStatisticsSubGroupsNodes(iii, experimentIterator);
			isStatisticSubGroup = false;
			isStatisticSubSubGroup = false;
			createStatisticsNodes(0, 0, iii, experimentIterator);

		}

	}

	public void createStatisticsSubGroupsNodes(int groupIterator,
			int experimentIterator) throws SQLException, SimDBException {
		subGroup = new ArrayList<String>();
		ArrayList<String> subGroupformatedName = new ArrayList<String>();
		for (int a = 0; a < SubGroups.size(); a++) {

			String group = SubGroups.get(a);
			String subgroup = "";
			if (group.contains("/")) {
				int index2 = group.indexOf("/");
				subgroup = group;
				group = group.substring(0, index2);

			}

			String[] subGroupHierarchie = subgroup.split("/");
			String formatedSubGroup = subGroupHierarchie[1];
			if ((Groups.get(groupIterator).contains(group))
					&& (!subGroupformatedName.contains(formatedSubGroup))
					&& (subGroupHierarchie.length > 2)) {

				subGroup.add(subgroup);
				subGroupformatedName.add(formatedSubGroup);
			}

		}
		StatisticsSubGroupsNodes = new DefaultMutableTreeNode[subGroup.size()];

		for (int a = 0; a < subGroup.size(); a++) {
			String[] statisticSubGroup = subGroup.get(a).split("/");
			StatisticsSubGroupsNodes[a] = new DefaultMutableTreeNode(
					statisticSubGroup[1]);

			isStatisticSubGroup = true;
			isStatisticSubSubGroup = false;
			createStatisticsNodes(0, a, groupIterator, experimentIterator);

			StatisticsGroupsNodes[groupIterator]
					.add(StatisticsSubGroupsNodes[a]);
		}

	}

	/**
	 * This method creates the tree nodes containing the statistic list.
	 * 
	 * @param statisticGroupIterator
	 * @throws SQLException
	 * @throws SimDBException
	 */
	public void createStatisticsNodes(int subSubGroupIterator,
			int subGroupIterator, int groupIterator, int experimentIterator)
			throws SQLException, SimDBException {

		if (isStatisticSubGroup == true) {
			for (int ii = 0; ii < StatisticsName.size(); ii++) {
				String[] statisticsHierarchies = StatisticsName.get(ii).split(
						"/");
				String[] subGroupHierarchies = subGroup.get(subGroupIterator)
						.split("/");

				if ((subGroupHierarchies[1]
						.contentEquals(statisticsHierarchies[1]))) {

					if (statisticsHierarchies.length > 2
							&& (subGroupHierarchies[0]
									.contains(statisticsHierarchies[0]))) {
						StatisticsNodes[ii] = new DefaultMutableTreeNode(
								new CheckNode(statisticsHierarchies[2] + "#"
										+ StatisticsIdList.get(ii)));

						createSubStatisticsNodes(ii, experimentIterator,
								StatisticsNodes);
						StatisticsSubGroupsNodes[subGroupIterator]
								.add(StatisticsNodes[ii]);

					}

				}
			}

			isStatisticSubGroup = false;

		} else if (isStatisticSubSubGroup == true) {

			for (int ii = 0; ii < StatisticsName.size(); ii++) {
				String[] statisticsHierarchies = StatisticsName.get(ii).split(
						"/");
				String[] subSubGroupHierarchies = SubSubGroups.get(
						subSubGroupIterator).split("/");

				StatisticsNodes[ii] = new DefaultMutableTreeNode(new CheckNode(
						statisticsHierarchies[2] + "#"
								+ StatisticsIdList.get(ii)));
				createSubStatisticsNodes(ii, experimentIterator,
						StatisticsNodes);
				if (subSubGroupHierarchies[1]
						.contentEquals(statisticsHierarchies[1])) {
					StatisticsSubGroupsNodes[subGroupIterator]
							.add(StatisticsNodes[ii]);
				}

			}
			isStatisticSubSubGroup = false;

		} else {
			for (int ii = 0; ii < StatisticsName.size(); ii++) {
				String[] statisticsHierarchies = StatisticsName.get(ii).split(
						"/");
				if ((statisticsHierarchies.length < 3)
						&& (Groups.get(groupIterator)
								.contentEquals(statisticsHierarchies[0]))) {

					StatisticsNodes[ii] = new DefaultMutableTreeNode(
							new CheckNode(statisticsHierarchies[1] + "#"
									+ StatisticsIdList.get(ii)));

					createSubStatisticsNodes(ii, experimentIterator,
							StatisticsNodes);
					StatisticsGroupsNodes[groupIterator]
							.add(StatisticsNodes[ii]);
				}

			}
		}
	}

	/**
	 * This method creates the tree nodes containing the statistic list with
	 * nodes different of -1.
	 * 
	 * @param iterator
	 * @param StatisticsNodes
	 * @throws SQLException
	 * @throws SimDBException
	 */
	public void createSubStatisticsNodes(int iterator, int experimentIterator,
			DefaultMutableTreeNode[] StatisticsNodes) throws SQLException,
			SimDBException {
		ResultSet resultsetStat2 = reader.selectSecondaryStatistics(
				StatisticsName.get(iterator),
				Integer.parseInt(ExperimentsIdList.get(experimentIterator)));
		SubStatisticsIdList = new ArrayList<String>();
		SubStatisticsName = new ArrayList<String>();
		while (resultsetStat2.next()) {

			SubStatisticsIdList = getList(resultsetStat2, "SubStatistics");

			DefaultMutableTreeNode[] SecondaryStatisticsNodes = new DefaultMutableTreeNode[SubStatisticsIdList
					.size()];
			for (int iii = 0; iii < SubStatisticsIdList.size(); iii++) {
				String[] subStatisticName = SubStatisticsName.get(iii).split(
						"/");
				SecondaryStatisticsNodes[iii] = new DefaultMutableTreeNode(
						new CheckNode(SubStatisticsIdList.get(iii) + ", "
								+ subStatisticName[2]));

				StatisticsNodes[iterator].add(SecondaryStatisticsNodes[iii]);

			}

		}

		resultsetStat2.close();
	}

	/**
	 * Creates the tree.
	 * 
	 * @return a JScrollPane with the created tree
	 * @throws SimDBException
	 * @throws SQLException
	 * @throws IOException
	 */
	public JScrollPane createTree() throws SimDBException, SQLException,
			IOException {

		DBNode = new DefaultMutableTreeNode[1];
		DBNode[0] = new DefaultMutableTreeNode("Databases ");
		createDatabasesNodes();
		tree = new JTree(DBNode[0]);
		tree.setCellRenderer(new CheckRenderer());
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

		tree.putClientProperty("JTree.lineStyle", "Angled");
		tree.addMouseListener(new NodeSelectionListener(tree, DBNode[0]));
		JScrollPane treePane = new JScrollPane(tree);

		return treePane;

	}

	/**
	 * This method reads resultsets and add experiments and statistics to the
	 * corresponding lists.
	 * 
	 * @param resultset
	 * @param type
	 * @return the list
	 * @throws SQLException
	 */
	protected List<String> getList(ResultSet resultset, String type)
			throws SQLException {

		ResultSetMetaData metaData = resultset.getMetaData();
		int columns = metaData.getColumnCount();

		do {
			for (int i = 1; i <= columns; i++) {
				String value = resultset.getString(i);
				if (i == 1) {
					if (type.equals("Experiment")) {
						ExperimentsIdList.add(value);
					}
					if (type.equals("Statistic")) {
						StatisticsIdList.add(value);
					}
					if (type.equals("SubStatistics")) {
						SubStatisticsIdList.add(value);
					}
				} else {
					if (type == "Experiment") {
						ExperimentName.add(value);
					}
					if (type == "Statistic") {
						StatisticsName.add(value);
					}
					if (type == "SubStatistics") {
						SubStatisticsName.add(value);
					}
				}
			}

		} while (resultset.next());

		if (type.equals("Statistic")) {
			return StatisticsIdList;
		}
		if (type.equals("Experiment")) {
			return ExperimentsIdList;
		}
		if (type.equals("SubStatistics")) {
			return SubStatisticsIdList;
		}
		return null;
	}

	/**
	 * Gets the statistic name substring.
	 * 
	 * @param Statistic
	 * @return the name of the statistic group, for instance in the case of
	 *         statistic=bubblestorm/join time, just bubblestorm will be
	 *         returned
	 */
	public String getStatisticNameSubstring(String Statistic) {
		String StatisticGroup = "";
		int separator = Statistic.indexOf('/');
		StatisticGroup = Statistic.substring(0, separator);
		return StatisticGroup;
	}

}
