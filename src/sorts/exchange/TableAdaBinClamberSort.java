package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class TableAdaBinClamberSort extends Sort {

    public TableAdaBinClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Table Adaptive Binary Clamber");
        this.setRunAllSortsName("Table Adaptive Binary Clamber Sort");
        this.setRunSortName("Table Adaptive Binary Clambersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean stableComp(int[] array, int[] table, int a, int b) {
        int comp = Reads.compareIndices(array, table[a], table[b], 0.125, true);
        return comp < 0 || (comp == 0 && Reads.compareOriginalIndices(table, a, b, 0, false) < 0);
    }

    protected int binarySearch(int[] array, int[] table, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(4, a);
            Highlights.markArray(3, m);
            Highlights.markArray(5, b);
            Delays.sleep(1);
            if (stableComp(array, table, value, m)) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] table = Writes.createExternalArray(currentLength);
        for (int i = 0; i < currentLength; i++) Writes.write(table, i, i, 0.5, true, true);
        int left = 0;
        int right = 1;
        while (right < currentLength) {
            if (!stableComp(array, table, right - 1, right)) {
                left = binarySearch(array, table, 0, right - 1, right);
                Highlights.clearAllMarks();
                while (left < right) {
                    Writes.swap(table, left, right, 0.2, true, true);
                    left++;
                }
            }
            right++;
        }
        Highlights.clearAllMarks();
        for(int i = 0; i < currentLength; i++) {
            Highlights.markArray(2, i);
            if(Reads.compareOriginalValues(i, table[i]) != 0) {
                int t = array[i];
                int j = i, next = table[i];
                do {
                    Writes.write(array, j, array[next], 1, true, false);
                    Writes.write(table, j, j, 1, true, true);
                    j = next;
                    next = table[next];
                }
                while(Reads.compareOriginalValues(next, i) != 0);
                Writes.write(array, j, t, 1, true, false);
                Writes.write(table, j, j, 1, true, true);
            }
        }
        Writes.deleteExternalArray(table);
    }
}