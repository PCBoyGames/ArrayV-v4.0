package sorts.distribute;

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

i'm 100% sure someone can make it so much worse

 */

public class DeterministicMaxGoofSort extends BogoSorting {

    int min;

    public DeterministicMaxGoofSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Deterministic Max Goof");
        this.setRunAllSortsName("Deterministic Max Goof Sort");
        this.setRunSortName("Deterministic Max Goofsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(9);
        this.setBogoSort(false);
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

    public void dMaxGoof(int[] array, int a, int b) {
        min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = a; i < b; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int[] aux = Writes.createExternalArray(b - a);
        Writes.arraycopy(array, a, aux, 0, b - a, 0, true, true);
        for (int i = a; i < b; i++) Writes.write(array, i, max, delay, true, false);
        while (!arePerms(array, aux, b - a)) {
            Writes.write(array, a, array[a]-1, delay, true, false);
            for (int i = a; i < b; i++) {
                if (array[i] == min-1) {
                    Writes.write(array, i, max, delay, true, false);
                    Writes.write(array, i+1, array[i+1]-1, delay, true, false);
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!isArraySorted(array, currentLength)) {
            dMaxGoof(array, 0, currentLength);
        }
    }
}
