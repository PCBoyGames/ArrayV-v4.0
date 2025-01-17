package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OptimizedRoomSort extends Sort {

    int first;
    int last;

    public OptimizedRoomSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Room");
        this.setRunAllSortsName("Optimized Room Sort");
        this.setRunSortName("Optimized Roomsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int binarySearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(0.015);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    protected boolean binsert(int[] array, int start, int gap, int currentLength) {
        boolean inserts = false;
        boolean firstfound = false;
        for (int i = start + 1; i < currentLength; i++) {
            if (Reads.compareIndices(array, i - 1, i, 0.015, true) > 0) {
                inserts = true;
                int left = i - gap > start ? i - gap : start;
                if (Reads.compareIndices(array, left, i, 0.015, true) <= 0) left = binarySearch(array, left + 1, i - 1, array[i]);
                Writes.insert(array, last = i, left, 0.015, true, false);
                if (!firstfound) {
                    first = left;
                    firstfound = true;
                }
            }
        }
        return inserts;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        first = 0;
        last = currentLength;
        boolean inserts = true;
        int end = currentLength;
        int g = (int) Math.sqrt(currentLength) + 1;
        while (inserts) {
            inserts = binsert(array, first, g, end);
            end = last;
            first = first - g - 1 > 0 ? first - g - 1 : 0;
        }
    }
}