package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Optimized Stoogesort's backwards function as its own sort.

(also https://youtu.be/u0vV6QUQNb8)

 */

public class TriangleInsertionSort extends Sort {
    public TriangleInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Triangle Insertion");
        this.setRunAllSortsName("Triangle Insertion Sort");
        this.setRunSortName("Triangle Insertsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void compRev(int[] array, int a, int b, double delay) {
        while (a < b) {
            if (Reads.compareValues(array[a], array[b]) > 0) {
                Writes.swap(array, a, b, delay, true, false);
            }
            a++;
            b--;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength-1; i++) {
            compRev(array, 0, i, 0.05);
        }
        for (int i = 0; i < currentLength; i++) {
            compRev(array, i, currentLength-1, 0.05);
        }
    }
}
