package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class TriSearchGnomeSort2 extends Sort {
    public TriSearchGnomeSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("TriSearch Gnome (New)");
        this.setRunAllSortsName("TriSearch Gnome Sort");
        this.setRunSortName("TriSearch Gnomesort");
        this.setCategory("Exchange Sorts");
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
    
    public void triGnomeSort(int[] array, int a, int b, double rSleep, double wSleep) {
        for (int i = a + 1; i < b; i++) {
            int dest = triSearch(array, a, i, array[i], rSleep);
            int pos = i;
            while (pos > dest) {
                Writes.swap(array, pos, pos - 1, wSleep, true, false);
                pos--;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        triGnomeSort(array, 0, sortLength, 40, 1);
    }
}
