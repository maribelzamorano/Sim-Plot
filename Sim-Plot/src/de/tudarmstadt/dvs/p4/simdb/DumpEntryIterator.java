package de.tudarmstadt.dvs.p4.simdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The Class DumpEntryIterator.
 */
public class DumpEntryIterator implements Iterator<DumpEntry> {

	/** The Constant dbDateFormat. */
	private static final DateFormat dbDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/** The rs. */
	private final ResultSet rs;

	/**
	 * Instantiates a new dump entry iterator.
	 *
	 * @param resultset rs
	 */
	public DumpEntryIterator(final ResultSet rs) {
		this.rs = rs;
	}

	/**
	 * Close.
	 */
	public void close() {
		try {
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
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
	public DumpEntry next() {
		try {
			if (!hasNext())
				throw new NoSuchElementException();
			final String time = rs.getString(1);
			final int node = rs.getInt(2);
			final String text = rs.getString(3);
			if (!rs.next())
				rs.close();
			return new DumpEntry(dbDateFormat.parse(time), node, text);
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
