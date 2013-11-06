package edu.trafficsim.model.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.trafficsim.model.Composition;

public class Randoms {

	public final static <T> T randomElement(Composition<T> composition,
			Random rand) {
		double threshold = rand.nextDouble();
		if (composition != null && composition.total() > 0) {
			T key = null;
			double sum = 0;
			for (T otherkey : composition.keys()) {
				key = otherkey;
				sum += composition.probability(otherkey);
				if (threshold <= sum)
					break;
			}
			return key;
		} else if (composition.keys().size() > 0) {
			randomElement(composition.keys(), rand);
		}
		return null;
	}

	public final static <T> T randomElement(Collection<T> c, Random rand) {
		if (c == null || c.isEmpty())
			return null;
		List<T> shuffledList = new ArrayList<T>(c);
		Collections.shuffle(shuffledList, rand);
		return shuffledList.get(0);
	}

	public final static double uniform(double min, double max, Random rand) {
		return min + (max - min) * rand.nextDouble();
	}
}
