package evaluation.metrics;


import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.text.SimpleDateFormat;

public abstract class Metric {

	private String name;

	protected SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH");

	// evaluation parameters
	protected int k = 10;

	protected Metric() {
		name = this.getClass().getSimpleName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The number of items to consider from the top of the recommendation list.
	 *
	 * @param k -
	 */
	public void setK(int k) {
		this.k = k;
	}

	public int getK() {
		return k;
	}


	/**
	 * The method called by sender class
	 *
	 * @param requestTimeStamp      the current transaction
	 * @param recommendations  the list of recommendations which is a child of Item
	 *                         class
	 * @param userTransactions list of transactions for the current user which shows
	 *                         user's next clicks
	 */

	public abstract void evaluate(Long requestTimeStamp, LongArrayList recommendations,
			LongOpenHashSet userTransactions);

	/**
	 * return result of evaluation
	 * 
	 * @return the result of the evaluation (e.g. mean F1, mean MRR, etc.)
	 */
	public abstract double getResults();

	/**
	 * Averages the results of multiple scores
	 *
	 * @param results -
	 * @return the average of a list of evaluation scores
	 */
	protected double getAvg(DoubleArrayList results) {
		SummaryStatistics avg = new SummaryStatistics();
		for (Double val : results) {
			avg.addValue(val);
		}
		return avg.getMean();
	}

}
