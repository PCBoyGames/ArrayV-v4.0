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

This was a pain to fix...

 */

public class HeadPullSortGooflang extends Sort {
    public HeadPullSortGooflang(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gooflang's Head Pull");
        this.setRunAllSortsName("Gooflang's Head Pull Sort");
        this.setRunSortName("Gooflang's Head Pull Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
        this.setBogoSort(false);
    }

    public void nOmegaMultiSwap(int O, int[] array, int a, int b, int depth) {
        if (a != b) {
            int temp = array[a];
            Writes.recordDepth(depth);
            if (O == 1) {
                if (b - a > 0) {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.multiSwap(array, a, b, 0.1, true, false);
                    }
                } else {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.multiSwap(array, a, b, 0.1, true, false);
                    }
                }
            } else {
                if (b - a > 0) {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.recursion();
                        nOmegaMultiSwap(O - 1, array, a, b, depth + 1);
                    }
                } else {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.recursion();
                        nOmegaMultiSwap(O - 1, array, a, b, depth + 1);
                    }
                }
            }
            while (array[b] != temp) {
                Writes.multiSwap(array, a, b, 0.1, true, false);
            }
        }
    }


    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 1;
        while (i + 1 <= currentLength) {
            nOmegaMultiSwap(currentLength, array, i, 0, 0);
            if (Reads.compareIndices(array, i - 1, i, 0.1, true) > 0) {
                nOmegaMultiSwap(currentLength, array, i, 0, 0);
                i = 1;
            } else i++;
        }
    }
}
