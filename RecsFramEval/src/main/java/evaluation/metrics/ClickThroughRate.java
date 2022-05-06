package evaluation.metrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
 *         The second type considers each recommended article individually. We
 *         refer to these impressions as *item impressions*.
 */
public class ClickThroughRate extends HypothesisTestableMetric {

	private static final long serialVersionUID = 1L;

	// result storage
	int validResultCounter = 0;
	int recommendedItemsCounter = 0;

	// ---------------------
	String timeUnitKey; // hour or month

	static Date date;

	Double averageCtr;

	// Hourly result storage
	private Map<String, Double> resultsByTime = new HashMap<String, Double>();

	private Map<String, Double> clicksByTime = new HashMap<String, Double>();

	private Map<String, Double> recommendedItemsByTime = new HashMap<String, Double>();

	// ---------------------

	// result storage
	private DoubleArrayList results = new DoubleArrayList();

	public void evaluate(Long requestTimeStamp, LongArrayList recommendations, LongOpenHashSet userTransactions) {
		// if there is no ground truth, there is nothing to evaluate

		if (userTransactions == null || userTransactions.isEmpty()) {
			return;
		}
		int clicks = 0;

		// if the algorithm does not return any recommendations, count it as
		// nonValid Results * k
		if (recommendations.isEmpty()) {
			results.add(0);
			return;
		}

		int realK = Math.min(k, userTransactions.size());

		recommendedItemsCounter += realK;

		// Result list having less items than the requested number of
		// recommendations are extended by adding invalid article
		// recommendations.
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
		double result = 0.0;
		// iterate over relevant items and recommendations to calculate the
		// intersection
		for (int i = 0; i < realK; i++) {

			for (LongIterator iterator = userTransactions.iterator(); iterator.hasNext();) {
				long itemID = iterator.nextLong();

				if (itemID == recommendations.getLong(i)) {
					validResultCounter++;
					clicks++;
					break;
				}

			}

		}

		result = (double) validResultCounter / (double) (recommendedItemsCounter);
		
		results.add(result);

		// ----------------------------------------------

		date = new Date(requestTimeStamp);
		timeUnitKey = sdf.format(date).toString();

		if (clicksByTime.get(timeUnitKey) == null) {
			clicksByTime.put(timeUnitKey, 0.0);
		}
		clicksByTime.put(timeUnitKey, clicksByTime.get(timeUnitKey) + clicks);

		if (recommendedItemsByTime.get(timeUnitKey) == null) {
			recommendedItemsByTime.put(timeUnitKey, 0.0);
		}
		recommendedItemsByTime.put(timeUnitKey, recommendedItemsByTime.get(timeUnitKey) + realK);

		// ------------------------------------------------

	}

	@Override
	public double getResults() {
		// return the CTR
		// double ctr = 100.0 * validResultCounter / (recommendedItemsCounter);

		double ctr = (double) validResultCounter / (double) (recommendedItemsCounter);

		return ctr;
	}


	@Override
	public DoubleArrayList getDetailedResults() {
		return null;
	}
}
