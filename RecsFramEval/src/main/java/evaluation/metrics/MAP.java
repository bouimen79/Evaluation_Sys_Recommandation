package evaluation.metrics;

import java.util.ArrayList;
import java.util.Map;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

/**
 * A class that can calculate the Click Through Rate
 * 
 * @author Hemza
 *
 */
public class MAP extends HypothesisTestableMetric {

	private static final long serialVersionUID = 1L;

	// ---------------------

	// result storage
	private DoubleArrayList results = new DoubleArrayList();


	public void evaluate(Long requestTimeStamp, LongArrayList recommendations, LongOpenHashSet userTransactions) {
		// if there is no ground truth, there is nothing to evaluate

		if (userTransactions == null || userTransactions.isEmpty()) {
			return;
		}

		// if the algorithm does not return any recommendations, count it as
		// nonValid Results * k
		if (recommendations.isEmpty()) {
			results.add(0);
			return;
		}

		// Result list having less items than the requested number of
		// recommendations are extended by adding invalid article
		// recommendations.
		
		int realK = Math.min(k, userTransactions.size());
		
		// Result lists having more than the requested number of entries are cut
		// to the requested length.

		if (recommendations.size() < realK) {
			ArrayList<Long> recommendationsA = new ArrayList<Long>();
			recommendationsA.addAll(recommendations);
			Long i = 0L;
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

		double ap = 0;
		double relCount = 0;
		double result = 0.0;
		double itemRelevance;
		// iterate over relevant items and recommendations to calculate the
		// intersection

		
		int rank = 0;
		for (int i = 0; i < realK; i++) {
			rank = i + 1;
			itemRelevance = 0;
			for (LongIterator iterator = userTransactions.iterator(); iterator.hasNext();) {
				long itemID = iterator.nextLong();

				if (itemID == recommendations.getLong(i)) {
					relCount++;
					itemRelevance = 1;
					break;
				}

			}
			if (itemRelevance>0) {
				 ap += relCount / (double) rank;
			}
           
		}

		result = (double) ap / (double) userTransactions.size();

		results.add(result);

	}


	@Override
	public double getResults() {
		return getAvg(results);

	}

	@Override
	public DoubleArrayList getDetailedResults() {
		// return the detailed results
		return results;
	}

}
