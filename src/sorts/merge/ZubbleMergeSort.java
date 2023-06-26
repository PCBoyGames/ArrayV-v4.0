package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Distray

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author Distray
 *
 */
public final class ZubbleMergeSort extends Sort {

    public ZubbleMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Zubble Merge");
        setRunAllSortsName("Zubble Merge Sort");
        setRunSortName("Zubble Mergesort");
        setCategory("Merge Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    public void zubble(int[] array, int a, int b) {
        int length = b - a;
        for (int i = length - 1, cons = 1, first = 1; i >= first; i-=cons) {
            boolean firstset = false;
            for (int j = Math.max(first - 1, 0); j < i; j++) {
                int k = j;
                boolean swap = false;
                while (j < i && Reads.compareValues(array[a + k], array[a + j + 1]) > 0) {
                    j++;
                    swap = true;
                }
                if (swap) {
                    Writes.swap(array, a + k, a + j, 1, true, false);
                    cons = 1;
                    if (!firstset)
                        first = k;
                    firstset = true;
                } else {
                    cons++;
                }
            }
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        if (b - a < 2)
            return;
        int m = a + (b - a) / 2;
        mergeSort(array, a, m);
        mergeSort(array, m, b);
        zubble(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
