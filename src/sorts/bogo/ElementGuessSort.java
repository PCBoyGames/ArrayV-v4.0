package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 *
 */
public final class ElementGuessSort extends BogoSorting {

    public ElementGuessSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Element Guess");
        this.setRunAllSortsName("Element Guess Sort");
        this.setRunSortName("Element Guess Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(7);
        this.setBogoSort(false);
    }

    boolean arePerms(int[] a, int[] b, int len) {
        for (int i = 0; i < len; i++) {
            int val = a[i];
            int count1 = 0, count2 = 0;
            for (int j = 0; j < len; j++) {
                if (Reads.compareIndexValue(a, j, val, 0, false) == 0) count1++;
                if (Reads.compareIndexValue(b, j, val, 0, false) == 0) count2++;
            }
            if (count1 != count2) return false;
        }
        return true;
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        if (isArraySorted(array, sortLength)) return;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < sortLength; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int[] init = Writes.createExternalArray(sortLength);
        Writes.arraycopy(array, 0, init, 0, sortLength, delay, false, true);
        boolean finalized = false;
        for (int i = 0; i < sortLength; i++) Writes.write(array, i, min, delay, true, false);
        while (!finalized) {
            for (int pos = 0; pos < sortLength; ++pos) {
                if (array[pos] < max) {
                    Writes.write(array, pos, array[pos] + 1, this.delay, true, false);
                    break;
                } else
                    Writes.write(array, pos, min, this.delay, true, false);
            }
            if (isArraySorted(array, sortLength))
                finalized = arePerms(array, init, sortLength);
        }
        Writes.deleteExternalArray(init);

    }

}
