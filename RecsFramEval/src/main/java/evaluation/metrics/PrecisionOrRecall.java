package evaluation.metrics;

import java.util.ArrayList;
import java.util.Map;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

/**
 * A class that replaces that can calculate both Precision and Recall
 * 
 * @author MJ
 *
 */
public class PrecisionOrRecall extends HypothesisTestableMetric {
	private static final long serialVersionUID = 5757499847554728701L;
	// result storage
	private DoubleArrayList results = new DoubleArrayList();
	// the type (Precision or Recall)
	private Type type = Type.Undefined;
	public void evaluate(Long requestTimeStamp, LongArrayList recommendations, LongOpenHashSet userTransactions) {
		// if there is no ground truth, there is nothing to evaluate
		if (userTransactions == null || userTransactions.isEmpty()) {
			return;
		}
		// if the algorithm does not return any recommendations, count it as 0
		if (recommendations.isEmpty()) {
			results.add(0);
			return;
		}

		// if the ground truth size is less than k recommendations, we calculate
				// the real k value for this case
		int realK = Math.min(k, userTransactions.size());
		
		// Result list having less items than the requested number of
				// recommendations are extended by adding invalid article
				// recommendations.
				// Result lists having more than the requested number of entries are cut
				// to the requested length.

		if (recommendations.size() < realK) {
			ArrayList<Long> recommendationsA = new ArrayList<Long>();
			recommendationsA.addAll(recommendations);
			Long i =0L;
			while (recommendationsA.size() < realK) {	
				i--;
				recommendationsA.add(i);
			}
			
			recommendations = new LongArrayList(recommendationsA);
		}

		// check duplicates
		LongOpenHashSet uniqueRecs = new LongOpenHashSet();
		for (int i = 0; i < realK; i++) {
			if (!uniqueRecs.add(recommendations.getLong(i))) {
				throw new RuntimeException("Duplicate recommendation.");
			}
		}

		// calculate the precision
		double result = 0;
		// iterate over relevant items and recommendations to calculate the
		// intersection
		for (LongIterator iterator = userTransactions.iterator(); iterator.hasNext();) {
			long itemID = iterator.nextLong();
			for (int i = 0; i < realK; i++) {
				if (itemID == recommendations.getLong(i)) {
					result++;
				}
			}
		}

		// determine the divider of the fraction (different for precision and
		// recall)
		double divider;
		if (type == Type.Precision) {
			divider = realK;
		} else if (type == Type.Recall) {
			divider = userTransactions.size();
		} else {
			throw new RuntimeException("Neither precision nor recall defined.");
		}
		// store the precision/Recall
		results.add(result / divider);
	}

	@Override
	public double getResults() {
		// return the average results
		return getAvg(results);
	}

	/**
	 * The type of this metric (Precision or Recall). Set via JSON config.
	 * 
	 * @param type
	 *            the type to set
	 */
	void setType(Type type) {
		this.type = type;
	}

	/**
	 * An enum that represents the type of this metric (Precision or Recall)
	 * 
	 * @author MJ
	 *
	 */
	public static enum Type {
		Undefined, Precision, Recall
	}

	@Override
	public DoubleArrayList getDetailedResults() {
		// return the detailed results
		return results;
	}

}
