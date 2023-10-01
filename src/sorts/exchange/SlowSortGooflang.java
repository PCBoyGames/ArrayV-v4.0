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

 */

public class SlowSortGooflang extends Sort {
    public SlowSortGooflang(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gooflang's Slow");
        this.setRunAllSortsName("Gooflang's Slow Sort");
        this.setRunSortName("Gooflang's Slowsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(5);
        this.setBogoSort(false);
    }

    public void nOmegaSwap(int O, int[] array, int a, int b, int depth) {
        if (a != b) {
            Writes.recordDepth(depth);
            int temp = array[a];
            if (O == 1) {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    Writes.swap(array, a, b, 0.1, true, false);
                }
            } else {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    Writes.recursion();
                    nOmegaSwap(O - 1, array, a, b, depth + 1);
                }
            }
            while (array[b] != temp) {
                Writes.swap(array, a, b, 0.1, true, false);
            }
        }
    }

    private void goofSlow(int O, int[] array, int a, int b, int d) {
        if (a >= b || a > arrayVisualizer.getCurrentLength() || b > arrayVisualizer.getCurrentLength()) return;
        Writes.recordDepth(d++);
        if (O == 1) {
            Writes.recursion();
            goofSlow(O, array, a, b-1, d);
            Writes.recursion();
            goofSlow(O, array, a+1, b, d);
            do nOmegaSwap(arrayVisualizer.getCurrentLength(), array, a, b, 0); while (Reads.compareValues(array[a], array[b]) > 0);
            Writes.recursion();
            goofSlow(O, array, a, b-1, d);
        } else {
            Writes.recursion();
            goofSlow(O, array, a, b-1, d);
            Writes.recursion();
            goofSlow(O, array, a+1, b, d);
            do goofSlow(O-1, array, a, b, d); while (Reads.compareValues(array[a], array[b]) > 0);
            Writes.recursion();
            goofSlow(O, array, a, b-1, d);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        goofSlow(currentLength, array, 0, currentLength-1, 0);
    }
}
