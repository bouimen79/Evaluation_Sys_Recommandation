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
public class NDCG extends HypothesisTestableMetric {

	private static final long serialVersionUID = 1L;

	// ---------------------

	// result storage
	private DoubleArrayList results = new DoubleArrayList();

	/**
	 * Compute the normalized discounted cumulative gain (NDCG) of a list of ranked
	 * items.
	 *
	 * @return the NDCG for the given data
	 */

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

		double dcg = 0;
		double idcg = calculateIDCG(realK);

		if (idcg == 0) {
			results.add(0);
			return;
		}

		double result = 0.0;
		// iterate over relevant items and recommendations to calculate the
		// intersection

		double itemRelevance;
		int rank = 0;
		for (int i = 0; i < realK; i++) {
			rank = i + 1;
			itemRelevance = 0;
			for (LongIterator iterator = userTransactions.iterator(); iterator.hasNext();) {
				long itemID = iterator.nextLong();

				if (itemID == recommendations.getLong(i)) {
					itemRelevance = 1;
					break;
				}

			}
			dcg += (Math.pow(2, itemRelevance) - 1.0) * (Math.log(2) / Math.log(rank + 1));

		}

		result = (double) dcg / (double) (idcg);

		results.add(result);

	}

	/**
	 * Calculates the iDCG
	 * 
	 * @param n size of the expected resource list
	 * @return iDCG
	 */
	public static double calculateIDCG(int k) {
		double idcg = 0;
		// if can get relevance for every item should replace the relevance score at
		// this point, else
		// every item in the ideal case has relevance of 1
		int itemRelevance = 1;

		for (int i = 0; i < k; i++) {
			idcg += (Math.pow(2, itemRelevance) - 1.0) * (Math.log(2) / Math.log(i + 2));
		}

		return idcg;
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
