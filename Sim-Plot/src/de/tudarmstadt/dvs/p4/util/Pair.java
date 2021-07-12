package de.tudarmstadt.dvs.p4.util;

/**
 * The Class Pair.
 *
 * @param <A> the generic type
 * @param <B> the generic type
 */
public class Pair<A, B> {

	/** The a. */
	private final A a;

	/** The b. */
	private final B b;

	/**
	 * Instantiates a new pair.
	 *
	 * @param a the a
	 * @param b the b
	 */
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Gets the a.
	 *
	 * @return the a
	 */
	public A getA() {
		return a;
	}

	/**
	 * Gets the b.
	 *
	 * @return the b
	 */
	public B getB() {
		return b;
	}

}
