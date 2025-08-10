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

you think you're slick, huh?

 */

public class SlickSort extends Sort {
    public SlickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Slick");
        this.setRunAllSortsName("Slick Sort");
        this.setRunSortName("Slicksort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void slick(int[] array, int a, int b, int d) {
        Writes.recordDepth(d++);
        if (a >= b) return;
        int ip = (a+b)/2;
        int p = array[ip];
        int i = a;
        int j = b;
        while (i <= j) {
            while (Reads.compareValues(array[i], p) < 0) {
                Highlights.markArray(1, i++);
                Delays.sleep(0.5);
            }
            while (Reads.compareValues(array[j], p) > 0) {
                Highlights.markArray(2, j--);
                Delays.sleep(0.5);
            }
            if (i <= j) {
                if (i == ip) Highlights.markArray(3, j);
                if (j == ip) Highlights.markArray(3, i);
                Writes.swap(array, i, j, 1, true, false);
                Writes.recursion();
                slick(array, i++, j--, d);
            }
        }
        if (a < j) {
            Writes.recursion();
            slick(array, a, j, d);
        }
        if (i < b) {
            Writes.recursion();
            slick(array, i, b, d);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        slick(array, 0, currentLength-1, 0);
    }
}
