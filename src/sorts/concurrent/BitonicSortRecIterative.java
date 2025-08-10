package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 ,_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_.
 * ~~~~~ Rec.Iterative Bitonicsort ~~~~~ *
 |              Part of the              |
 *        "Dissort Can You Make"         *
 |                series                 |
 '*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'
 */

public class BitonicSortRecIterative extends Sort {
    public BitonicSortRecIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bitonic (Recursive/Iterative)");
        this.setRunAllSortsName("Recursive-Iterative Bitonic Sort");
        this.setRunSortName("Recursive-Iterative Bitonic Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void bitonicSort(int[] A, int lo, int n, boolean dir)
    {
        if (n > 1)
        {
            int m = n / 2;
            this.bitonicSort(A, lo, m, !dir);
            this.bitonicSort(A, lo + m, n - m, dir);

            int g = 1 << (31 - Integer.numberOfLeadingZeros(n - 1));
            for(int j = g; j > 0; j /= 2) {
            	for(int i = 0; i < n; i++) {
            		if(i % (j * 2) < j && (i + j) < n && (dir ^ Reads.compareIndices(A, lo + i, lo + i + j, 1, true) > 0)) {
            			Writes.swap(A, lo + i, lo + i + j, 1, true, false);
            		}
            	}
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.bitonicSort(array, 0, sortLength, false);
    }
}