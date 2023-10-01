package sorts.esoteric;

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
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2);
        this.setBogoSort(true);
    }

    public void bStooge(int[] array, int a, int b, int d) {
        int i = a % arrayVisualizer.getCurrentLength(),
            j = b % arrayVisualizer.getCurrentLength();
        if (Reads.compareValues(array[i], array[j]) > 0) Writes.swap(array, i, j, 0.005, true, false);
        if (b - a >= 3) {
            Writes.recordDepth(d++);
            Writes.recursion();
            bStooge(array, a, b-1, d);
            Writes.recursion();
            bStooge(array, a+1, b, d);
            Writes.recursion();
            bStooge(array, a, b-1, d);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        bStooge(array, 0, (int) Math.pow(3, currentLength), 0);
    }
}
