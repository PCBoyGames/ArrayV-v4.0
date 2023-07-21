package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Look at all those recursions!

 */

public final class BadSortGooflang extends Sort {
    public BadSortGooflang(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gooflang's Bad");
        this.setRunAllSortsName("Gooflang's Bad Sort");
        this.setRunSortName("Gooflang's Badsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(7);
        this.setBogoSort(false);
    }

    public int nOmegaMinSearch(int O, int[] array, int a, int b, double delay, int depth) {
        int r = a;
        if (a != b) {
            Writes.recordDepth(depth);
            if (a > b) {
                Writes.recursion();
                r = nOmegaMinSearch(O, array, b, a, delay, depth + 1);
            }
            if (O == 0) {
                int min = a;
                for (int j = a + 1; j < b; j++) {
                    Highlights.markArray(2, j);
                    Delays.sleep(delay);
                    if (Reads.compareValues(array[j], array[min]) < 0) {
                        min = j;
                        Highlights.markArray(1, min);
                        Delays.sleep(delay);
                    }
                    r = min;
                }
            } else {
                for (int i = a; i < b; i++) {
                    Writes.recursion();
                    r = nOmegaMinSearch(O - 1, array, a, b, delay, depth + 1);
                }
            }
        }
        return r;
    }

    public void nOmegaSwap(int O, int[] array, int a, int b, int depth) {
        if (a != b) {
            Writes.recordDepth(depth);
            int temp = array[a];
            if (O == 1) {
                if (b - a > 0) {
                    for (int i = 0; i < b - a; i++) {
                        Writes.swap(array, a, b, 0.1, true, false);
                    }
                } else {
                    for (int i = 0; i < a - b; i++) {
                        Writes.swap(array, a, b, 0.1, true, false);
                    }
                }
            } else {
                if (b - a > 0) {
                    for (int i = 0; i < b - a; i++) {
                        Writes.recursion();
                        nOmegaSwap(O - 1, array, a, b, depth + 1);
                    }
                } else {
                    for (int i = 0; i < a - b; i++) {
                        Writes.recursion();
                        nOmegaSwap(O - 1, array, a, b, depth + 1);
                    }
                }
            }
            while (array[b] != temp) {
                Writes.swap(array, a, b, 0.1, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength; i++) {
            nOmegaSwap(currentLength, array, i, nOmegaMinSearch(currentLength, array, i, currentLength, 0.05, 0), 0);
        }
    }
}
