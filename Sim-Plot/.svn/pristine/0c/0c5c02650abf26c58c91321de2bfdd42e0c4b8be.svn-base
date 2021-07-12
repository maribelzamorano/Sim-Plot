package de.tudarmstadt.dvs.p4.simdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import de.tudarmstadt.dvs.p4.util.Pair;

/**
 * The Class Dump.
 */
public class Dump {

	//
	// fields
	//

	/**
	 * The Class DumpsByNodeIterator.
	 */
	protected class DumpsByNodeIterator implements
			Iterator<Pair<Integer, String>> {

		/** The rs. */
		private final ResultSet rs;

		/**
		 * Instantiates a new dumps by node iterator.
		 *
		 * @param rs the rs
		 */
		protected DumpsByNodeIterator(final ResultSet rs) {
			this.rs = rs;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			try {
				return !rs.isAfterLast();
			} catch (SQLException e) {
				return false;
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Pair<Integer, String> next() {
			try {
				if (!hasNext())
					throw new NoSuchElementException();
				final int node = rs.getInt(1);
				final String text = rs.getString(2);
				if (!rs.next())
					rs.close();
				return new Pair<Integer, String>(node, text);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * The Class DumpsByTimeIterator.
	 */
	protected class DumpsByTimeIterator implements Iterator<Pair<Date, String>> {

		/** The rs. */
		private final ResultSet rs;

		/**
		 * Instantiates a new dumps by time iterator.
		 *
		 * @param rs the rs
		 */
		protected DumpsByTimeIterator(final ResultSet rs) {
			this.rs = rs;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			try {
				return !rs.isAfterLast();
			} catch (SQLException e) {
				return false;
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Pair<Date, String> next() {
			try {
				if (!hasNext())
					throw new NoSuchElementException();
				final String time = rs.getString(1);
				final String text = rs.getString(2);
				if (!rs.next())
					rs.close();
				return new Pair<Date, String>(dbDateFormat.parse(time), text);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * The Class NodesIterator.
	 */
	protected class NodesIterator implements
			Iterator<Pair<Integer, Iterable<DumpEntry>>> {

		/** The rs. */
		private final ResultSet rs;

		/**
		 * Instantiates a new nodes iterator.
		 *
		 * @throws SimDBException the sim db exception
		 */
		protected NodesIterator() throws SimDBException {
			try {
				db.getStmtGetDumpNodes().setInt(1, dumpId);
				rs = db.getStmtGetDumpNodes().executeQuery();
			} catch (SQLException e) {
				throw new SimDBException(e.getMessage(), e);
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			try {
				return !rs.isAfterLast();
			} catch (SQLException e) {
				return false;
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Pair<Integer, Iterable<DumpEntry>> next() {
			try {
				final int node = rs.getInt(1);
				if (!rs.next())
					rs.close();

				Iterable<DumpEntry> it = new Iterable<DumpEntry>() {
					@Override
					public Iterator<DumpEntry> iterator() {
						try {
							db.getStmtGetDumpDataByNode().setInt(1, dumpId);
							db.getStmtGetDumpDataByNode().setInt(2, node);
							return new DumpEntryIterator(
									db.getStmtGetDumpDataByNode().executeQuery());
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				};
				return new Pair<Integer, Iterable<DumpEntry>>(node, it);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * The Class TimesIterator.
	 */
	protected class TimesIterator implements
			Iterator<Pair<Date, Iterable<DumpEntry>>> {

		/** The rs. */
		private final ResultSet rs;

		/**
		 * Instantiates a new times iterator.
		 *
		 * @throws SimDBException the sim db exception
		 */
		protected TimesIterator() throws SimDBException {
			try {
				db.getStmtGetDumpTimes().setInt(1, dumpId);
				rs = db.getStmtGetDumpTimes().executeQuery();
			} catch (SQLException e) {
				throw new SimDBException(e.getMessage(), e);
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			try {
				return !rs.isAfterLast();
			} catch (SQLException e) {
				return false;
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Pair<Date, Iterable<DumpEntry>> next() {
			try {
				final String time = rs.getString(1);
				if (!rs.next())
					rs.close();

				Iterable<DumpEntry> it = new Iterable<DumpEntry>() {
					@Override
					public Iterator<DumpEntry> iterator() {
						try {
							db.getStmtGetDumpDataByTime().setInt(1, dumpId);
							db.getStmtGetDumpDataByTime().setString(2, time);
							return new DumpEntryIterator(
									db.getStmtGetDumpDataByTime().executeQuery());
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				};
				return new Pair<Date, Iterable<DumpEntry>>(
						dbDateFormat.parse(time), it);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/** The db. */
	private final SimDBReader db;

	/** The dump id. */
	private final int dumpId;

	//
	// public methods
	//

	/** The name. */
	private final String name;

	/** The prefix. */
	private final String prefix;

	/** The suffix. */
	private final String suffix;

	/** The Constant dbDateFormat. */
	private static final DateFormat dbDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * Instantiates a new dump.
	 *
	 * @param simDBReader the sim db reader
	 * @param experimentId the experiment id
	 * @param name the name
	 * @throws SimDBException the sim db exception
	 */
	protected Dump(final SimDBReader simDBReader, final int experimentId,
			final String name) throws SimDBException {
		try {
			this.db = simDBReader;
			ResultSet rs;
			if ((name != null) && (!name.isEmpty())) {
				simDBReader.getStmtGetDumpByName().setInt(1, experimentId);
				simDBReader.getStmtGetDumpByName().setString(2, name);
				rs = simDBReader.getStmtGetDumpByName().executeQuery();
			} else {
				rs = simDBReader.getStmtGetExperiments().executeQuery();
				if (!rs.isLast())
					throw new SimDBException(
							"There is more than one dump. Name must be specified.");
			}
			this.dumpId = rs.getInt(1);
			this.name = rs.getString(2);
			this.prefix = rs.getString(3);
			this.suffix = rs.getString(4);
		} catch (SQLException e) {
			throw new SimDBException(e.getMessage(), e);
		}
	}

	//
	// internal methods
	//

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	//
	// inner classes
	//

	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 * @throws SimDBException the sim db exception
	 */
	public Iterator<Pair<Integer, Iterable<DumpEntry>>> getNodes()
			throws SimDBException {
		return new NodesIterator();
	}

	/**
	 * Gets the prefix.
	 *
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Gets the suffix.
	 *
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Gets the times.
	 *
	 * @return the times
	 * @throws SimDBException the sim db exception
	 */
	public Iterator<Pair<Date, Iterable<DumpEntry>>> getTimes()
			throws SimDBException {
		return new TimesIterator();
	}

}
