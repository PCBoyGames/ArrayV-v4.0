package sorts.bogo;

import java.util.BitSet;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Meme Man

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan - implementation of the sort
 * @author Meme Man - key ideas / concepts
 *
 */
public class DecrogoSort extends BogoSorting {

    public DecrogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Decrogo");
        this.setRunAllSortsName("Decrogo Sort");
        this.setRunSortName("Decrogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
        this.setBogoSort(true);
    }

    boolean isPermutation(int[] a, int[] b, int len) {
        BitSet flags = new BitSet(len);
        boolean permutation = true;
        for (int i = 0; i < len; i++) {
            int select = 0;
            boolean any = false;
            for (int j = 0; j < len; j++) {
                if (flags.get(j)) continue;
                if (Reads.compareValues(a[j], b[i]) == 0) {
                    select = j;
                    any = true;
                    break;
                }
            }
            if (any) flags.set(select);
            else {
                permutation = false;
                break;
            }
        }
        return permutation;
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
        for (int i = 0; i < sortLength; i++) Writes.write(array, i, max, delay, true, false);
        while (!finalized) {
            int j = BogoSorting.randInt(0, sortLength);
            if (array[j] == min) Writes.write(array, j, max, this.delay, true, false);
            else Writes.write(array, j, array[j] - 1, this.delay, true, false);
            if (isArraySorted(array, sortLength))
                finalized = isPermutation(array, init, sortLength);
        }
        Writes.deleteExternalArray(init);

    }

}
