package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
extending code by thatsOven

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Haruki
 * @author thatsOven
 * 
 */
public class TriSearchInsertionSort2 extends Sort {

    public TriSearchInsertionSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("TriSearch Insertion 2");
        this.setRunAllSortsName("Haruki's TriSearch Insertion Sort");
        this.setRunSortName("Haruki's TriSearch Insertion Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public int triSearch(int[] array, int a, int b, int val, double sleep) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(1, a);
            Highlights.markArray(2, m);
            Highlights.markArray(3, b - 1);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[a]) < 0) break;
            if (Reads.compareValues(val, array[b - 1]) >= 0) {
                Highlights.clearAllMarks();
                return b;
            }
            if (Reads.compareValues(val, array[m]) < 0) {
                a = a + 1;
                b = m;
            } else {
                a = m + 1;
                b = b - 1;
            }
        }
        Highlights.clearAllMarks();
        return a;
    }
    
    public void triInsertSort(int[] array, int a, int b, double rSleep, double wSleep) {
        for (int i = a + 1; i < b; i++) {
            int current = array[i];
            int dest = triSearch(array, a, i, current, rSleep);
            int pos = i;
            while (pos > dest) {
                Writes.write(array, pos, array[pos - 1], wSleep, true, false);
                pos--;
            }
            if (pos < i) Writes.write(array, pos, current, wSleep, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        triInsertSort(array, 0, sortLength, 40, 1);

    }

}
