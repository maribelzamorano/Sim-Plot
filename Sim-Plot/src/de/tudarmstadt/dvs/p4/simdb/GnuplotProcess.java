package de.tudarmstadt.dvs.p4.simdb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * The Class GnuplotProcess runs gnuplot through a process and gets some
 * properties values like grid,xrange and yrange.
 */
public class GnuplotProcess {

	/** The process of gnuplot. */
	private Process p = null;

	/** The std input. */
	private BufferedReader stdInput = null;

	/** The std output. */
	private BufferedWriter stdOutput = null;

	/** The std error. */
	private BufferedReader stdError = null;

	/** The builder. */
	private ProcessBuilder builder;

	/**
	 * Default Constructor.
	 * @param gnuplot the gnuplot
	 */
	public GnuplotProcess(final String gnuplot) {

		try {

			// run gnuplot through a process
			p = Runtime.getRuntime().exec(gnuplot);

			// Get the Process standart input
			stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			// Get the Process standart output
			stdOutput = new BufferedWriter(new OutputStreamWriter(
					p.getOutputStream()));

			// Get the Process standart output error
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	/**
	 * Close.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final void close() throws IOException {
		stdInput.close();
		stdOutput.close();
		if(stdError!=null)
		{
			stdError.close();
		}
		p.destroy();

	}

	/**
	 * Send command to Gnuplot.
	 * @param cmd Gnuplot shell command.
	 */
	public final void cmd(final String cmd) {

		try {
			stdOutput.write(cmd + "\n");
			stdOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Get Gnuplot current outputstream line.
	 * @return the line
	 * @throws InterruptedException the interrupted exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final String getLine() throws InterruptedException, IOException {
		String aws = null;

		try {

			aws = stdError.readLine();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		return aws;
	}

	/**
	 * Get next no empty outputstream line.
	 * @return the no empty line
	 * @throws InterruptedException the interrupted exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String getNoEmptyLine() throws InterruptedException, IOException {

		String aws = getLine();

		if (aws != null) {
			if (aws.equals("")) {
				aws = getNoEmptyLine();
			}
		}
		return aws;
	}

	/**
	 * Get Gnuplot variable.
	 * @param var Gnuplot variable name.
	 * @return the var
	 * @throws InterruptedException the interrupted exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final String getVar(final String var) throws InterruptedException, IOException {

		cmd("show " + var);
		String aws = "";

		stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		if (var == "xr") {
			aws = stdError.readLine();
			aws += stdError.readLine();
			if (aws.contains("gnuplot")) {

				aws = stdError.readLine();
				aws = stdError.readLine();
				aws = stdError.readLine();
				aws = stdError.readLine();
				aws = stdError.readLine();
			}
			if (aws.contains("grid")) {
				aws = stdError.readLine();
				aws = stdError.readLine();
				aws = stdError.readLine();
				aws = stdError.readLine();
			}

			aws += stdError.readLine();
			aws += stdError.readLine();
		}
		if (var == "yr") {
			aws = stdError.readLine();
			aws += stdError.readLine();
			aws += stdError.readLine();
		}
		if (var == "grid") {

			aws = stdError.readLine();
			aws = stdError.readLine();

			if (aws.contains("OFF")) {
				aws += stdError.readLine();
			} else {
				aws += stdError.readLine();

				aws += stdError.readLine();
			}
			if (aws.contains("gnuplot")) {
				if (aws.contains("errorbars")) {
					aws = stdError.readLine();
					aws = stdError.readLine();
					aws += stdError.readLine();

				} else {
					aws = stdError.readLine();
					aws = stdError.readLine();
					aws = stdError.readLine();
					aws += stdError.readLine();
					aws += stdError.readLine();
					aws += stdError.readLine();
				}
				if (aws.contains("set yrange")) {
					aws += stdError.readLine();
					aws += stdError.readLine();

					if (aws.contains("errorbars")) {
						aws += stdError.readLine();
						aws += stdError.readLine();
						aws += stdError.readLine();
						aws += stdError.readLine();
						aws += stdError.readLine();

					}
				}

			}
		}

		if (aws.equals("None")) {
			aws = null;
		}

		return aws;
	}

	public ProcessBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(final ProcessBuilder builder) {
		this.builder = builder;
	}

}
