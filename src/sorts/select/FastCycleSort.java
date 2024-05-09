package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

import java.util.Random;

/*
 *
Copyright (c) rosettacode.org
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.3
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".
 *
 */

final public class FastCycleSort extends Sort {
    public FastCycleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fast Cycle");
        this.setRunAllSortsName("Cyclesort");
        this.setRunSortName("Cyclesort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int n, int bucketCount) {
		Random r = new Random();

        for (int j = 0; j < n-1; ) {
			Highlights.markArray(1, j);
			int k = j;

			for (int i = j+1; i < n; i++) if (array[i] < array[j]) k++;
			Reads.setComparisons(Reads.getComparisons().intValue() + n-j-1);

			Highlights.markArray(2, j + r.nextInt(n-j));
			Delays.sleep((double)(n-j)/n);

			if (k == j) j++;

			else {
				while (array[k] == array[j]) { k++; }
				Writes.swap(array, j, k, (double)(n-j)/n, true, false);
			}
		}
    }
}