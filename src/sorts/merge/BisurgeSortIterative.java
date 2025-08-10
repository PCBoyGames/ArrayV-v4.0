package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
extending code by Lancewer

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Haruki
 * @author Lancewer
 *
 */
public class BisurgeSortIterative extends Sort {
    public BisurgeSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bisurge (Iterative)");
        this.setRunAllSortsName("Iterative Bisurge Sort");
        this.setRunSortName("Iterative Bisurgesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void bisurgeInsert(int[] array, int start, int mid, int end, double compSleep, double writeSleep) {
        int lastPos = start, binStart = start;
        for (int i = mid; i < end; i++) {
            if (binStart - i == 1 && Reads.compareIndices(array, binStart, i, compSleep, true) > 0) break;
            int temp = array[i];
            int a = lastPos, b = i;

            while (a < b) {
                int m = a + ((b - a) / 2); // avoid int overflow!
                Highlights.markArray(1, a);
                Highlights.markArray(2, m);
                Highlights.markArray(3, b);

                Delays.sleep(compSleep);

                if (Reads.compareValues(temp, array[m]) < 0) { // do NOT move equal elements to right of inserted
                                                               // element; this maintains stability!
                    b = m;
                } else {
                    a = m + 1;
                }
            }
            lastPos = a;
            binStart = a;
            Highlights.clearMark(3);

            // item has to go into position lo

            int j = i - 1;

            while (j >= a) {
                Writes.write(array, j + 1, array[j], writeSleep, true, false);
                j--;
            }
            if (j + 1 < i) Writes.write(array, a, temp, writeSleep, true, false);
            Highlights.clearAllMarks();
        }
    }

    public void sort(int[] array, int a, int b, double sleep) {
        for (int j = 1; j < b - a; j *= 2)
            for (int i = a; i + j < b; i += 2 * j)
                bisurgeInsert(array, i, i + j, Math.min(i + 2 * j, b), sleep, sleep * 0.5);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength, 0.25);
    }
}
