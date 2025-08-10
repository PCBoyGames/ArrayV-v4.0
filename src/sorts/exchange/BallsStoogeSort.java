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

stooge sortb ut ballsk

 */

public class BallsStoogeSort extends Sort {
    public BallsStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Balls Stooge");
        this.setRunAllSortsName("Balls Stooge Sort");
        this.setRunSortName("Balls Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2);
        this.setBogoSort(true);
    }

    public void bStooge(int[] array, int a, int b, int k, int l, int d) {
        Writes.recordDepth(d++);
        int i = a % (l-k+1), j = b % (l-k+1);
        if (i != j) if (Reads.compareIndices(array, i, j, 1, true) == (i < j ? 1 : -1))
            Writes.swap(array, i, j, 1, true, false);
        if ((b-a+1) >= 3) {
            Writes.recursion();
            bStooge(array, a, b-1, k, l, d);
            Writes.recursion();
            bStooge(array, a+1, b, k, l, d);
            Writes.recursion();
            bStooge(array, a, b-1, k, l, d);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        bStooge(array, 0, (int) Math.pow(3, currentLength)-1, 0, currentLength-1, 0);
    }
}
