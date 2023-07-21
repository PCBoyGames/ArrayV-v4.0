package sorts.exchange;

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

Another brick in the wall of Another brick in the wall of Another brick in the wall of Another brick in the wall of ⋯

 */

public final class GradualStoogeSortGooflang extends Sort {

    int h;

    public GradualStoogeSortGooflang(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gooflang's Gradually More & More Terrifying Stooge");
        this.setRunAllSortsName("Gooflang's Gradually More & More Terrifying Stooge Sort");
        this.setRunSortName("Gooflang's G.M&M.T. Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(7);
        this.setBogoSort(false);
    }

    private void goofStooge(int O, int[] array, int a, int b, int depth) {
        Writes.recordDepth(depth++);
        do nOmegaSwap(h, array, a, b, 0); while (Reads.compareValues(array[a], array[b]) > 0);
        if (O == 1) {
            if (b - a + 1 >= 3) {
                Writes.recursion();
            	goofStooge(1, array, a, b - 1, depth);
                Writes.recursion();
                goofStooge(1, array, a + 1, b, depth);
                Writes.recursion();
                goofStooge(1, array, a, b - 1, depth);
            }
        } else {
            if (a < b) {
                Writes.recursion();
                goofStooge(O, array, a, b - 1, depth);
                Writes.recursion();
                goofStooge(O, array, a + 1, b, depth);
                Writes.recursion();
                goofStooge(O, array, a, b - 1, depth);
                Writes.recursion();
                goofStooge(O - 1, array, a, b, depth);
            }
        }
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
        h = currentLength;
        for (int i = 0; i < currentLength; i++) {
            goofStooge(currentLength, array, 0, i, 0);
        }
    }
}
