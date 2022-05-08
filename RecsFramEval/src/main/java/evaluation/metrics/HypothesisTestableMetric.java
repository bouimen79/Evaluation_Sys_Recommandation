package evaluation.metrics;

import java.io.Serializable;




import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

/**
 * A metric that can provide test results for every sample point
 * so that statistical test can be performed.
 * @author MJ
 *
 */
public abstract class HypothesisTestableMetric extends Metric implements Serializable{
	private static final long serialVersionUID = -3343868792390624219L;
	/**
	 * Return the detailed results of every single recommendation list evaluation,
	 * so that later on the null-hypothesis (that the means of two different algorithms
	 * are the same) can be tested.
	 * @return A list of all individual evaluation results
	 */
	public abstract DoubleArrayList getDetailedResults();
	
		
}
