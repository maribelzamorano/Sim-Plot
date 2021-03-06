package de.tudarmstadt.dvs.p4.simdb;

/**
 * The Class DBQuery sets and gets all the different queries used to retrieve
 * statistics in the GUI.
 */
public class DBQuery {

	/** This query is used to retrieve regular statistics. */
	private static String statisticsQuery;

	/** This query is used to retrieve statistics with aggregation. */
	private static String aggregateStatisticsQuery;

	/**
	 * This query is used to retrieve regular statistics together with min and
	 * max values.
	 */
	private static String minMaxStatisticsQuery;

	/**
	 * This query is used to retrieve statistics in which the node value is -1.
	 */
	private static String groupNodeStatisticsQuery;

	/**
	 * This query is used to retrieve statistics in which the node value is -1
	 * using aggregation.
	 */
	private static String groupNodeAggregateStatisticsQuery;

	/**
	 * This query is used to retrieve statistics in which the node value is -1
	 * and also the respective min and max.
	 */
	private static String groupNodeMinMaxStatisticsQuery;

	/**
	 * This query is used to retrieve statistics in which the node value is -1
	 * using aggregation and also the respective min and max.
	 */
	private static String groupNodeAggregateMinMaxStatisticsQuery;

	/** This query is used to retrieve multiple statistics. */
	private static String severalStatisticsQuery;

	/** This query is used to retrieve multiple statistics using aggregation. */
	private static String severalAggregateStatisticsQuery;

	/**
	 * This query is used to retrieve multiple statistics in which the node value
	 * is -1.
	 */
	private static String groupNodeSeveralStatisticsQuery;

	/**
	 * This query is used to retrieve multiple statistics in which the node value
	 * is -1 using aggregation.
	 */
	private static String groupNodeSeveralAggregateStatisticsQuery;

	/**
	 * @return the aggregateStatisticsQuery
	 */
	public final String getAggregateStatisticsQuery() {

		return aggregateStatisticsQuery;
	}

	/**
	 * @return groupNodeAggregateMinMaxStatisticsQuery
	 */
	public final String getGroupNodeAggregateMinMaxStatisticsQuery() {

		return groupNodeAggregateMinMaxStatisticsQuery;
	}

	/**
	 * @return groupNodeAggregateStatisticsQuery
	 */
	public final String getGroupNodeAggregateStatisticsQuery() {

		return groupNodeAggregateStatisticsQuery;
	}

	/**
	 * @return groupNodeMinMaxStatisticsQuery
	 */
	public final String getGroupNodeMinMaxStatisticsQuery() {

		return groupNodeMinMaxStatisticsQuery;
	}

	/**
	 * @return groupNodeSeveralAggregateStatisticsQuery
	 */
	public final String getGroupNodeSeveralAggregateStatisticsQuery() {

		return groupNodeSeveralAggregateStatisticsQuery;
	}

	/**
	 * @return groupNodeSeveralStatisticsQuery
	 */
	public final String getGroupNodeSeveralStatisticsQuery() {

		return groupNodeSeveralStatisticsQuery;
	}

	/**
	 * @return groupNodeStatisticsQuery
	 */
	public final String getGroupNodeStatisticsQuery() {

		return groupNodeStatisticsQuery;
	}

	/**
	 * @return minMaxStatisticsQuery
	 */
	public final String getMinMaxStatisticsQuery() {

		return minMaxStatisticsQuery;
	}

	/**
	 * @return severalAggregateStatisticsQuery
	 */
	public final String getSeveralAggregateStatisticsQuery() {

		return severalAggregateStatisticsQuery;
	}

	/**
	 * @return severalStatisticsQuery
	 */
	public final String getSeveralStatisticsQuery() {

		return severalStatisticsQuery;
	}

	/**
	 * @return statisticsQuery
	 */
	public final String getStatisticsQuery() {

		return statisticsQuery;
	}

	/**
	 * Sets the aggregate statistics query.
	 * @param statisticId
	 * @param seconds
	 */
	public final void setAggregateStatisticsQuery(int statisticId, int seconds) {
		aggregateStatisticsQuery = "SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var FROM measurements WHERE statistic="
				+ statisticId
				+ " group by round(strftime(\\\"%J\\\",time)*24*60*60/"
				+ seconds + ")";
	}

	/**
	 * Sets the groupNodeAggregateMinMaxStatistics Query.
	 * @param StatisticId
	 * @param seconds
	 */
	public void setGroupNodeAggregateMinMaxStatisticsQuery(int StatisticId,
			final int seconds) {
		groupNodeAggregateMinMaxStatisticsQuery = "SELECT time(time), max, min  FROM measurements WHERE statistic="
				+ StatisticId
				+ " group by round(strftime(\\\"%J\\\",time)*24*60*60/"
				+ seconds + ")";
	}

	/**
	 * Sets the groupNodeAggregateStatistics Query.
	 * @param StatisticId
	 * @param seconds
	 */
	public final void setGroupNodeAggregateStatisticsQuery(int StatisticId,
			final int seconds) {
		groupNodeAggregateStatisticsQuery = "SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var  FROM measurements WHERE statistic="
				+ StatisticId
				+ " group by round(strftime(\\\"%J\\\",time)*24*60*60/"
				+ seconds + ")";
	}

	/**
	 * Sets the groupNodeMinMaxStatistics Query.
	 * @param StatisticId
	 *            the new group node min max statistics query
	 */
	public final void setGroupNodeMinMaxStatisticsQuery(int StatisticId) {
		groupNodeMinMaxStatisticsQuery = "SELECT time(time), max, min FROM measurements WHERE statistic="
				+ StatisticId + " and node=-1";

	}

	/**
	 * Sets the groupNodeSeveralAggregateStatistics Query.
	 * @param statistic
	 * @param seconds
	 */
	public final void setGroupNodeSeveralAggregateStatisticsQuery(final int statistic,
			final int seconds) {
		groupNodeSeveralAggregateStatisticsQuery = "SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var  FROM measurements WHERE statistic="
				+ statistic
				+ " group by round(strftime(\\\"%J\\\",time)*24*60*60/"
				+ seconds + ")";
	}

	/**
	 * Sets the groupNodeSeveralStatistics Query.
	 * @param statistic
	 *            the new group node several statistics query
	 */
	public final void setGroupNodeSeveralStatisticsQuery(final int statistic) {
		groupNodeSeveralStatisticsQuery = "SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var FROM measurements WHERE statistic="
				+ statistic;
	}

	/**
	 * Sets the groupNodeStatistics Query.
	 * @param statisticId
	 *            the new group node statistics query
	 */
	public final void setGroupNodeStatisticsQuery(final int statisticId) {
		groupNodeStatisticsQuery = "SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var  FROM measurements WHERE statistic="
				+ statisticId;
	}

	/**
	 * Sets the minMaxStatistics Query.
	 * @param statisticId
	 *            the new min max statistics query
	 */
	public final void setMinMaxStatisticsQuery(final int statisticId) {
		minMaxStatisticsQuery = "SELECT time(time), max, min FROM measurements WHERE statistic="
				+ statisticId;
	}

	/**
	 * Sets the severalAggregateStatistics Query.
	 * @param statisticId
	 * @param seconds
	 */
	public final void setSeveralAggregateStatisticsQuery(final int statisticId, final int seconds) {
		severalAggregateStatisticsQuery = "SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var FROM measurements WHERE statistic="
				+ statisticId
				+ " group by round(strftime(\\\"%J\\\",time)*24*60*60/"
				+ seconds + ")";
	}

	/**
	 * Sets thesetSeveralStatistics Query.
	 * @param statisticId the new several statistics query
	 */
	public final void setSeveralStatisticsQuery(final int statisticId) {
		severalStatisticsQuery = "SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var FROM measurements WHERE statistic="
				+ statisticId;
	}

	/**
	 * Sets the statistics query.
	 * @param statisticId
	 *            the new statistics query
	 */
	public final void setStatisticsQuery(final int statisticId) {
		statisticsQuery = "SELECT time(time), sum/count, (count*sum2-sum*sum)/(count*count) as var FROM measurements WHERE statistic="
				+ statisticId;
	}
}
