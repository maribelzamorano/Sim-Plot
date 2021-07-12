package de.tudarmstadt.dvs.p4.simdb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Class SimDBReader creates a connection with the database and executes
 * different statements.
 */
public class SimDBReader {

	/** The conn. */
	private Connection conn;

	/** The stmt get experiment by name. */
	private PreparedStatement stmtGetExperimentByName;

	/** The stmt get experiments. */
	private PreparedStatement stmtGetExperiments;

	/** The stmt get dump by name. */
	private PreparedStatement stmtGetDumpByName;

	/** The stmt get dumps. */
	private PreparedStatement stmtGetDumps;

	/** The stmt get dump times. */
	private PreparedStatement stmtGetDumpTimes;

	/** The stmt get dump nodes. */
	private PreparedStatement stmtGetDumpNodes;

	/** The stmt get dump data by time. */
	private PreparedStatement stmtGetDumpDataByTime;

	/** The stmt get dump data by node. */
	private PreparedStatement stmtGetDumpDataByNode;

	/** The stmt get measurements by statistic. */
	private PreparedStatement stmtGetMeasurementsByStatistic;

	/** The stmt get min max measurements by statistic. */
	private PreparedStatement stmtGetMinMaxMeasurementsByStatistic;

	/** The stmt get measurements by statistic name. */
	private PreparedStatement stmtGetMeasurementsByStatisticName;

	/** The stmt get min max measurements by statistic name. */
	private PreparedStatement stmtGetMinMaxMeasurementsByStatisticName;

	/** The stmt get statistic by experiment id. */
	private PreparedStatement stmtGetStatisticByExperimentId;

	/** The stmt get all experiments. */
	private PreparedStatement stmtGetAllExperiments;

	/** The stmt get secondary statistics. */
	private PreparedStatement stmtGetSecondaryStatistics;

	/** The stmt get label by statistic name. */
	private PreparedStatement stmtGetLabelByStatisticName;

	/** The stmt get label by statistic id. */
	private PreparedStatement stmtGetLabelByStatisticId;

	/** The stmt get agg measurements by statistic. */
	private PreparedStatement stmtGetAggMeasurementsByStatistic;

	/** The stmt get agg measurements by statistic name. */
	private PreparedStatement stmtGetAggMeasurementsByStatisticName;

	/** The stmt get agg min max measurements by statistic. */
	private PreparedStatement stmtGetAggMinMaxMeasurementsByStatistic;

	/** The stmt get agg min max measurements by statistic name. */
	private PreparedStatement stmtGetAggMinMaxMeasurementsByStatisticName;

	/** The stmt get upper label by statistic id. */
	private PreparedStatement stmtGetUpperLabelByStatisticId;

	/** The stmt get upper label by statistic name. */
	private PreparedStatement stmtGetUpperLabelByStatisticName;

	/** The stmt get upper label. */
	private PreparedStatement stmtGetUpperLabel;

	/**
	 * Instantiates a new sim db reader.
	 * 
	 * @param dbFile
	 *            the db file
	 * @throws SimDBException
	 *             the sim db exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SQLException
	 */
	public SimDBReader(final String dbFile) throws IOException, SQLException {
		try {
			// create connection
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

			// prepare statements
			setStmtGetExperimentByName(conn
					.prepareStatement("SELECT id FROM experiments WHERE name=?"));
			setStmtGetExperiments(conn
					.prepareStatement("SELECT id FROM experiments"));
			stmtGetAllExperiments = conn
					.prepareStatement("SELECT id,name FROM experiments");
			setStmtGetDumpByName(conn
					.prepareStatement("SELECT id, name, prefix, suffix FROM dumps WHERE experiment=? AND name=?"));
			setStmtGetDumps(conn
					.prepareStatement("SELECT id, name, prefix, suffix FROM dumps WHERE experiment=?"));
			setStmtGetDumpTimes(conn
					.prepareStatement("SELECT DISTINCT time FROM dump_data WHERE dump=?"));
			setStmtGetDumpNodes(conn
					.prepareStatement("SELECT DISTINCT node FROM dump_data WHERE dump=?"));
			setStmtGetDumpDataByTime(conn
					.prepareStatement("SELECT time, node, text FROM dump_data WHERE dump=? and time=?"));
			setStmtGetDumpDataByNode(conn
					.prepareStatement("SELECT time, node, text FROM dump_data WHERE dump=? and node=?"));
			stmtGetStatisticByExperimentId = conn
					.prepareStatement("select id,name from statistics WHERE experiment=? and node=-1");
			stmtGetSecondaryStatistics = conn
					.prepareStatement("select id,name from statistics WHERE name=? and node!=-1 and experiment=?");
			stmtGetMeasurementsByStatistic = conn
					.prepareStatement("SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var FROM measurements WHERE statistic=?");
			stmtGetMinMaxMeasurementsByStatistic = conn
					.prepareStatement("SELECT time(time), max, min FROM measurements WHERE statistic=?");
			stmtGetMeasurementsByStatisticName = conn
					.prepareStatement("SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var  FROM measurements JOIN statistics ON measurements.statistic=statistics.id WHERE statistics.name=? and node=-1");
			stmtGetMinMaxMeasurementsByStatisticName = conn
					.prepareStatement("SELECT time(time), max, min FROM measurements JOIN statistics ON measurements.statistic=statistics.id WHERE statistics.name=? and node=-1");
			stmtGetLabelByStatisticName = conn
					.prepareStatement("SELECT label, units FROM statistics WHERE name=?");
			stmtGetLabelByStatisticId = conn
					.prepareStatement("SELECT label, units FROM statistics WHERE id=?");
			stmtGetAggMeasurementsByStatisticName = conn
					.prepareStatement("SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var  FROM measurements JOIN statistics ON measurements.statistic=statistics.id WHERE statistics.name=? and node=-1 group by round(strftime('%J',time)*24*60*60/?)");
			stmtGetAggMeasurementsByStatistic = conn
					.prepareStatement("SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var FROM measurements WHERE statistic=? group by round(strftime('%J',time)*24*60*60/?)");
			stmtGetAggMinMaxMeasurementsByStatisticName = conn
					.prepareStatement("SELECT time(time), max, min  FROM measurements JOIN statistics ON measurements.statistic=statistics.id WHERE statistics.name=? and node=-1 group by round(strftime('%J',time)*24*60*60/?)");
			stmtGetAggMinMaxMeasurementsByStatistic = conn
					.prepareStatement("SELECT time(time), max, min FROM measurements WHERE statistic=? group by round(strftime('%J',time)*24*60*60/?)");
			stmtGetUpperLabelByStatisticId = conn
					.prepareStatement("select distinct time(time), event_name from workload where plot = 'plot' and name in (select workload from node_sets where name in (select name from node_groups where experiment=(select name from experiments where id=(select distinct experiment from statistics where id=?))));");
			stmtGetUpperLabelByStatisticName = conn
					.prepareStatement("select distinct time(time), event_name from workload where plot = 'plot' and name in (select workload from node_sets where name in (select name from node_groups where experiment=(select name from experiments where id=(select distinct experiment from statistics where name=?))));");
			stmtGetUpperLabel = conn
					.prepareStatement("select distinct time(time), event_name from workload where plot = 'plot' and name in (select workload from node_sets where name in (select name from node_groups where experiment=(select name from experiments where id=(select distinct experiment from statistics))));");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Select agg measurements.
	 * 
	 * @param statisticId2
	 *            the statistic id2
	 * @param seconds
	 *            the seconds
	 * @return the result set
	 * @throws SimDBException
	 *             the sim db exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected final ResultSet selectAggMeasurements(final String statisticId2,
			final int seconds) throws SimDBException, IOException {
		ResultSet rs;
		try {
			if ((statisticId2 != null) && (!statisticId2.isEmpty())
					&& (seconds != 0)) {
				stmtGetAggMeasurementsByStatisticName
						.setString(1, statisticId2);
				stmtGetAggMeasurementsByStatisticName.setInt(2, seconds);
				rs = stmtGetAggMeasurementsByStatisticName.executeQuery();
			} else {
				rs = stmtGetAggMeasurementsByStatisticName.executeQuery();
			}
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select agg measurements by statistic.
	 * 
	 * @param statisticId2
	 * @param seconds
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectAggMeasurementsByStatistic(
			final int statisticId2, int seconds) throws SimDBException,
			IOException {
		ResultSet rs;
		try {

			if (statisticId2 != 0 && seconds != 0) {
				stmtGetAggMeasurementsByStatistic.setInt(1, statisticId2);
				stmtGetAggMeasurementsByStatistic.setInt(2, seconds);
				rs = stmtGetAggMeasurementsByStatistic.executeQuery();
			} else {
				rs = stmtGetAggMeasurementsByStatistic.executeQuery();
				if (!rs.isLast())
					throw new SimDBException(
							"There is more than one statistic. Statistic must be specified.");
			}
			if (rs.isAfterLast())
				throw new SimDBException("No such Statistic.");
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select agg min max measurements.
	 * 
	 * @param statisticId2
	 * @param seconds
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected ResultSet selectAggMinMaxMeasurements(final String statisticId2,
			final int seconds) throws SimDBException, IOException {
		ResultSet rs;
		try {
			if ((statisticId2 != null) && (!statisticId2.isEmpty())
					&& (seconds != 0)) {
				stmtGetAggMinMaxMeasurementsByStatisticName.setString(1,
						statisticId2);
				stmtGetAggMinMaxMeasurementsByStatisticName.setInt(2, seconds);
				rs = stmtGetAggMinMaxMeasurementsByStatisticName.executeQuery();
			} else {
				rs = stmtGetAggMinMaxMeasurementsByStatisticName.executeQuery();
			}
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select agg min max measurements by statistic.
	 * 
	 * @param statisticId2
	 * @param seconds
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectAggMinMaxMeasurementsByStatistic(
			final int statisticId2, int seconds) throws SimDBException,
			IOException {
		ResultSet rs;
		try {

			if (statisticId2 != 0 && seconds != 0) {
				stmtGetAggMinMaxMeasurementsByStatistic.setInt(1, statisticId2);
				stmtGetAggMinMaxMeasurementsByStatistic.setInt(2, seconds);
				rs = stmtGetAggMinMaxMeasurementsByStatistic.executeQuery();
			} else {
				rs = stmtGetAggMinMaxMeasurementsByStatistic.executeQuery();
				if (!rs.isLast())
					throw new SimDBException(
							"There is more than one statistic. Statistic must be specified.");
			}
			if (rs.isAfterLast())
				throw new SimDBException("No such Statistic.");
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select all experiments.
	 * 
	 * @return the result set
	 * @throws SimDBException
	 */
	protected ResultSet selectAllExperiments() throws SimDBException {
		try {
			ResultSet rs;
			rs = stmtGetAllExperiments.executeQuery();
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}
	}

	/**
	 * Select label by statistic id.
	 * 
	 * @param statisticId2
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectLabelByStatisticId(final int statisticId2)
			throws SimDBException, IOException {
		ResultSet rs;
		try {

			if (statisticId2 != 0) {
				stmtGetLabelByStatisticId.setInt(1, statisticId2);
				rs = stmtGetLabelByStatisticId.executeQuery();
			} else {
				rs = stmtGetLabelByStatisticId.executeQuery();
				if (!rs.isLast())
					throw new SimDBException(
							"There is more than one statistic. Statistic must be specified.");
			}
			if (rs.isAfterLast()) {
				throw new SimDBException("No such Statistic.");
			}
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select label by statistic name.
	 * 
	 * @param statisticId2
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectLabelByStatisticName(
			final String statisticId2) throws SimDBException, IOException {
		ResultSet rs;
		try {
			if ((statisticId2 != null) && (!statisticId2.isEmpty())) {
				stmtGetLabelByStatisticName.setString(1, statisticId2);
				rs = stmtGetLabelByStatisticName.executeQuery();
			} else {
				rs = stmtGetLabelByStatisticName.executeQuery();
			}
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select measurements.
	 * 
	 * @param statisticId2
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectMeasurements(final String statisticId2)
			throws SimDBException, IOException {
		ResultSet rs;
		try {
			if ((statisticId2 != null) && (!statisticId2.isEmpty())) {
				stmtGetMeasurementsByStatisticName.setString(1, statisticId2);
				rs = stmtGetMeasurementsByStatisticName.executeQuery();
			} else {
				rs = stmtGetMeasurementsByStatisticName.executeQuery();
			}
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select measurements by statistic.
	 * 
	 * @param statisticId2
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectMeasurementsByStatistic(
			final int statisticId2) throws SimDBException, IOException {
		ResultSet rs;
		try {

			if (statisticId2 != 0) {
				stmtGetMeasurementsByStatistic.setInt(1, statisticId2);
				rs = stmtGetMeasurementsByStatistic.executeQuery();
			} else {
				rs = stmtGetMeasurementsByStatistic.executeQuery();
				if (!rs.isLast())
					throw new SimDBException(
							"There is more than one statistic. Statistic must be specified.");
			}
			if (rs.isAfterLast())
				throw new SimDBException("No such Statistic.");
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select min max measurements.
	 * 
	 * @param statisticId2
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectMinMaxMeasurements(final String statisticId2)
			throws SimDBException, IOException {
		ResultSet rsMax;
		try {
			if ((statisticId2 != null) && (!statisticId2.isEmpty())) {
				stmtGetMinMaxMeasurementsByStatisticName.setString(1,
						statisticId2);
				rsMax = stmtGetMinMaxMeasurementsByStatisticName.executeQuery();
			} else {
				rsMax = stmtGetMinMaxMeasurementsByStatisticName.executeQuery();
			}
			return rsMax;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}
	}

	/**
	 * Select min max measurements by statistic.
	 * 
	 * @param statisticId2
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectMinMaxMeasurementsByStatistic(
			final int statisticId2) throws SimDBException, IOException {
		ResultSet rs;
		try {

			if (statisticId2 != 0) {
				stmtGetMinMaxMeasurementsByStatistic.setInt(1, statisticId2);
				rs = stmtGetMinMaxMeasurementsByStatistic.executeQuery();
			} else {
				rs = stmtGetMinMaxMeasurementsByStatistic.executeQuery();
				if (!rs.isLast())
					throw new SimDBException(
							"There is more than one statistic. Statistic must be specified.");
			}
			if (rs.isAfterLast())
				throw new SimDBException("No such Statistic.");
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select secondary statistics.
	 * 
	 * @param StatisticName
	 * @return the result set
	 * @throws SimDBException
	 */
	protected final ResultSet selectSecondaryStatistics(
			final String StatisticName, final int experiment)
			throws SimDBException {
		try {
			ResultSet rs;
			if ((StatisticName != null) && (!StatisticName.isEmpty())
					&& (experiment != 0)) {
				stmtGetSecondaryStatistics.setString(1, StatisticName);
				stmtGetSecondaryStatistics.setInt(2, experiment);
				rs = stmtGetSecondaryStatistics.executeQuery();
			} else {
				rs = stmtGetSecondaryStatistics.executeQuery();
				if (!rs.isLast())
					throw new SimDBException(
							"There is more than one statistic. Experiment ID must be specified.");// CAMBIAR
																									// ESTTO
			}
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}
	}

	/**
	 * Select statistics.
	 * 
	 * @param experimentId
	 * @return the result set
	 * @throws SimDBException
	 */
	protected final ResultSet selectStatistics(final String experimentId)
			throws SimDBException {
		try {
			ResultSet rs;
			if ((experimentId != null) && (!experimentId.isEmpty())) {
				stmtGetStatisticByExperimentId.setString(1, experimentId);
				rs = stmtGetStatisticByExperimentId.executeQuery();
			} else {
				rs = stmtGetStatisticByExperimentId.executeQuery();
				if (!rs.isLast())
					throw new SimDBException(
							"There is more than one statistic. Experiment ID must be specified.");// CAMBIAR
																									// ESTTO
			}
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}
	}

	/**
	 * Select upper label.
	 * 
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectUpperLabel() throws SimDBException,
			IOException {
		ResultSet rs;
		try {

			rs = stmtGetUpperLabel.executeQuery();

			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select upper label by statistic id.
	 * 
	 * @param statisticId2
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectUpperLabelByStatisticId(
			final int statisticId2) throws SimDBException, IOException {
		ResultSet rs;
		try {

			if (statisticId2 != 0) {
				stmtGetUpperLabelByStatisticId.setInt(1, statisticId2);
				rs = stmtGetUpperLabelByStatisticId.executeQuery();
			} else {
				rs = stmtGetUpperLabelByStatisticId.executeQuery();
				if (!rs.isLast())
					throw new SimDBException(
							"There is more than one statistic. Statistic must be specified.");
			}
			if (rs.isAfterLast())
				throw new SimDBException("No such Statistic.");
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	/**
	 * Select upper label by statistic name.
	 * 
	 * @param statisticId2
	 * @return the result set
	 * @throws SimDBException
	 * @throws IOException
	 */
	protected final ResultSet selectUpperLabelByStatisticName(
			final String statisticId2) throws SimDBException, IOException {
		ResultSet rs;
		try {
			if ((statisticId2 != null) && (!statisticId2.isEmpty())) {
				stmtGetUpperLabelByStatisticName.setString(1, statisticId2);
				rs = stmtGetUpperLabelByStatisticName.executeQuery();
			} else {
				rs = stmtGetUpperLabelByStatisticName.executeQuery();
			}
			return rs;
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}

	}

	public PreparedStatement getStmtGetExperimentByName() {
		return stmtGetExperimentByName;
	}

	public void setStmtGetExperimentByName(
			PreparedStatement stmtGetExperimentByName) {
		this.stmtGetExperimentByName = stmtGetExperimentByName;
	}

	public PreparedStatement getStmtGetExperiments() {
		return stmtGetExperiments;
	}

	public void setStmtGetExperiments(PreparedStatement stmtGetExperiments) {
		this.stmtGetExperiments = stmtGetExperiments;
	}

	public PreparedStatement getStmtGetDumpByName() {
		return stmtGetDumpByName;
	}

	public void setStmtGetDumpByName(PreparedStatement stmtGetDumpByName) {
		this.stmtGetDumpByName = stmtGetDumpByName;
	}

	public PreparedStatement getStmtGetDumps() {
		return stmtGetDumps;
	}

	public void setStmtGetDumps(PreparedStatement stmtGetDumps) {
		this.stmtGetDumps = stmtGetDumps;
	}

	public PreparedStatement getStmtGetDumpTimes() {
		return stmtGetDumpTimes;
	}

	public void setStmtGetDumpTimes(PreparedStatement stmtGetDumpTimes) {
		this.stmtGetDumpTimes = stmtGetDumpTimes;
	}

	public PreparedStatement getStmtGetDumpNodes() {
		return stmtGetDumpNodes;
	}

	public void setStmtGetDumpNodes(PreparedStatement stmtGetDumpNodes) {
		this.stmtGetDumpNodes = stmtGetDumpNodes;
	}

	public PreparedStatement getStmtGetDumpDataByTime() {
		return stmtGetDumpDataByTime;
	}

	public void setStmtGetDumpDataByTime(PreparedStatement stmtGetDumpDataByTime) {
		this.stmtGetDumpDataByTime = stmtGetDumpDataByTime;
	}

	public PreparedStatement getStmtGetDumpDataByNode() {
		return stmtGetDumpDataByNode;
	}

	public void setStmtGetDumpDataByNode(PreparedStatement stmtGetDumpDataByNode) {
		this.stmtGetDumpDataByNode = stmtGetDumpDataByNode;
	}
}
