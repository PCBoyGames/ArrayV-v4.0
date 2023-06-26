package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Whoops, I Goofed Up™!

 */

public final class UnfixedMaxGoofSort extends BogoSorting {
    public UnfixedMaxGoofSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Unfixed Max Goof");
        this.setRunAllSortsName("Unfixed Max Goof Sort");
        this.setRunSortName("Unfixed Max Goofsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2);
        this.setBogoSort(true);
    }

    // From Element Guess Sort by Ayako-chan.
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
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (!isArraySorted(array, currentLength)) {
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < currentLength; i++) if (array[i] > max) max = array[i];
            int[] init = Writes.createExternalArray(currentLength);
            Writes.arraycopy(array, 0, init, 0, currentLength, this.delay, true, true);
            boolean finalized = false;
            for (int i = 0; i < currentLength; i++) Writes.write(array, i, max, this.delay, true, false);
            while (!finalized) {
                int j = BogoSorting.randInt(0, currentLength);
                Writes.write(array, j, array[j] - 1, this.delay, true, false);
                if (isArraySorted(array, currentLength)) finalized = arePerms(array, init, currentLength);
            }
        }
    }
}
