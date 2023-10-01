package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

pseudo-parallel circlesort

 */

public class ConeSortIterative extends Sort {
    public ConeSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cone (Iterative)");
        this.setRunAllSortsName("Iterative Cone Sort");
        this.setRunSortName("Iterative Conesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private boolean compSwap(int[] array, int a, int b) {
		if (Reads.compareIndices(array, a, b, 0.5, true) > 0) {
			Writes.swap(array, a, b, 0.5, true, false);
			return true;
		}
		return false;
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
		int n = 1;
    	for (; n < currentLength; n *= 2);

		for (boolean s = true; s;) {
			s = false;
			for (int k = 0; k < n/2; k++)
				for (int j = n; j > 1 && k < j-1-k; j /= 2)
					for (int i = 0; i+j-1-k < currentLength; i += j)
						s |= this.compSwap(array, i+k, i+j-1-k);
		}
    }
}